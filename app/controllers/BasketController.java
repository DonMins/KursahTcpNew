package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import javax.inject.Inject;
import java.util.*;

import static controllers.WineController.searchParametrs;
import static scala.collection.JavaConverters.asScalaBuffer;

public class BasketController extends Controller {
    @Inject
    FormFactory formFactory;
    private int numberSql;
    protected final byte NO_ERROR = 0;
    protected final byte ERROR_LOGIN_OR_PASSWORD = 1;
    protected final byte NO_BUTTON_BUY_PRODUCT = 3;



    public void setNumber(int numberSql){
        this.numberSql = numberSql;
    }
    public  int getNumberSql(){
        return  numberSql;
    }

    public Result basketPage(){
        final byte IF_BASKET = 0;
        final byte IF_FAVORITE = 1;
        String login = AuxiliaryController.getSessionLogin();
        boolean isAdmin = AuxiliaryController.getSessionAdmin();

        DynamicForm dynamicForm = formFactory.form().bindFromRequest();

        if ((dynamicForm.get("numberSql"))!=null) {
            setNumber(Integer.parseInt(dynamicForm.get("numberSql")));
        }

        String sqlRequest ="";
        if(numberSql == IF_BASKET) {
            sqlRequest = " select distinct wine.id_product from wine, Basket,user" +
                    " where wine.id_product in(select Basket.id_product " +
                    "from public.Basket, public.user where public.Basket.login = '" + login + "' and favorite =false);";
        }
        if(numberSql == IF_FAVORITE) {
            sqlRequest = " select distinct wine.id_product from wine, Basket,user" +
                    " where wine.id_product in(select Basket.id_product " +
                    "from public.Basket, public.user where public.Basket.login = '" + login + "' and favorite =true);";
        }
        String wineRating = "";
        SqlQuery sqlQuery = Ebean.createSqlQuery(sqlRequest);
        List<SqlRow> sqlQueryList = sqlQuery.findList();
        List<wine> winList = null;
        List<String> nameColomn = new ArrayList<>();
        try {
            String parametrs = null;
            for (SqlRow row : sqlQueryList) {
                Set<String> keyset = row.keySet();
                for (String s : keyset) {
                    parametrs = row.getString(s);
                    wineRating = wineRating + parametrs + " ";
                }
            }
            String[] splitWineRating = wineRating.split(" ");
            List<Integer> idForBasket = new ArrayList<>();
            for (String term : splitWineRating) {
                Integer tmp = Integer.parseInt(term);
                idForBasket.add(tmp);
            }
            winList = Ebean.find(wine.class).where().in("id_product", idForBasket).
                    setDistinct(true).findList();
            for (int i = 0; i < idForBasket.size(); ++i) {
                winList.get(i).setIdProduct(idForBasket.get(i));
            }
            winList.sort(new Comparator<wine>() {
                @Override
                public int compare(wine o1, wine o2) {
                        if(o2.getIdProduct()  <=o1.getIdProduct())
                            return -1;
                        return 1;
                }
            });
            wine us = new wine();
            nameColomn = us.getNameColomn();
        }
        catch (NullPointerException| NumberFormatException ex){
           nameColomn = null;
        }
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);

        if (numberSql==IF_FAVORITE) {
            return ok(views.html.favoritePage.render(login, isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                    , asScalaBuffer(winList), loginForm, userForm, NO_ERROR));
        }
        return ok(views.html.basketPage.render(login, isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                , asScalaBuffer(winList), loginForm, userForm, NO_ERROR));
    }

    public Result addIn(int id){
        String login = AuxiliaryController.getSessionLogin();
        String sql = "select max(id_basket) from public.Basket";
        int maxIdBasket;
        try {
            maxIdBasket= Integer.parseInt(searchParametrs(sql));
        }
        catch (NumberFormatException e){
            maxIdBasket=0;
        }
        Basket basket = new Basket();
        basket.setIdBasket((maxIdBasket+1));
        basket.setIdProduct(id);
        basket.setLogin(login);
        basket.setFavorite(false);
        Ebean.save(basket);
        return redirect(routes.WineController.catalogPage());
    }
    public Result addInFavorite( int id){
        String login = AuxiliaryController.getSessionLogin();
        String sqlRequest = "select max(id_basket) from public.Basket";
        int maxIdBasket;
        try {
            maxIdBasket = Integer.parseInt(searchParametrs(sqlRequest));
        }
        catch (NumberFormatException e){
            maxIdBasket = 0;
        }
        Basket basket = new Basket();
        basket.setIdBasket((maxIdBasket+1));
        basket.setIdProduct(id);
        basket.setLogin(login);
        basket.setFavorite(true);
        Ebean.save(basket);

        return redirect(routes.WineController.catalogPage());
    }
    public Result deleteFromBasket(Integer id){
        String login = AuxiliaryController.getSessionLogin();
        String sqlRequest = "select id_basket from public.Basket where id_product="+id + "and login="+ "'" + login +"' and favorite=false" ;
        SqlQuery sqlQuery = Ebean.createSqlQuery(sqlRequest);
        List<SqlRow> sqlQueryList = sqlQuery.findList();
        int parametrs = 0;
        for (SqlRow row : sqlQueryList) {
            Set<String> keyset = row.keySet();
            for (String s : keyset) {
                parametrs = Integer.parseInt(row.getString(s));
            }
        }
        Basket.find.deleteById(parametrs);
        return redirect(routes.BasketController.basketPage());
    }
    public Result deleteFromFavorite(Integer id){
        String login = AuxiliaryController.getSessionLogin();
        String sqlRequest = "select id_basket from public.Basket where id_product="+id + "and login="+ "'" + login +"' and favorite=true" ;
        SqlQuery sqlQuery = Ebean.createSqlQuery(sqlRequest);
        List<SqlRow> sqlQueryList = sqlQuery.findList();
        int parametrs = 0;
        for (SqlRow row : sqlQueryList) {
            Set<String> keyset = row.keySet();
            for (String s : keyset) {
                parametrs = Integer.parseInt(row.getString(s));
            }
        }
        Basket.find.deleteById(parametrs);
        return redirect(routes.BasketController.basketPage());
    }
    public Result buyProducts(){
        String login = AuxiliaryController.getSessionLogin();
        String sqlRequest = "select sum(price) from public.wine where id_product in (select id_product from public.Basket where login="+ "'" + login +"'"+")";
        SqlQuery sqlQuery = Ebean.createSqlQuery(sqlRequest);
        List<SqlRow> sqlQueryList = sqlQuery.findList();
        double sumProduct  = 0;
        for (SqlRow row : sqlQueryList) {
            Set<String> keyset = row.keySet();
            for (String s : keyset) {
                sumProduct = Double.parseDouble(row.getString(s));
            }
        }
        Random rnd = new Random();
        int orderNumber = 100000 + rnd.nextInt(900000);
        String stringOrderNumber = "Оплачено "+ sumProduct+ "  рублей " +
                "№ заказа : "+login+String.valueOf(orderNumber);

        List<String> message = new ArrayList<String>();
        List<wine> win = new ArrayList<>();
        message.add(stringOrderNumber);
        Ebean.find(Basket.class).where().eq("login" , login).eq("favorite",false).delete();
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        return ok(views.html.basketPage.render(login, AuxiliaryController.getSessionAdmin(), JavaConverters.asScalaBuffer(message)
                ,JavaConverters.asScalaBuffer(win),loginForm,userForm, NO_BUTTON_BUY_PRODUCT));
    }
}

package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import javafx.print.Collation;
import models.*;
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
    protected String LOGIN ;
    protected Boolean ADMIN;
    @Inject
    FormFactory formFactory;

    public Result basketPage(){
        LOGIN = mainPageController.getSessionLogin();
        ADMIN = mainPageController.getSessionAdmin();
        String sql1 = " select distinct wine.id_product from wine, basket,user" +
                " where wine.id_product in(select basket.id_product " +
                "from public.basket, public.user where public.basket.login = '"+LOGIN+"');";

        String wineRating = "";
        SqlQuery maxId = Ebean.createSqlQuery(sql1);
        List<SqlRow> mId = maxId.findList();
        List<wine> winList = null;
        List<String> nameColomn = new ArrayList<>();
        try {
            String parametrs = null;
            for (SqlRow row2 : mId) {
                Set<String> keyset2 = row2.keySet();
                for (String s : keyset2) {
                    parametrs = row2.getString(s);
                    wineRating = wineRating + parametrs + " ";
                }
            }
            String[] splitS = wineRating.split(" ");

            List<Integer> idForBasket = new ArrayList<>();
            for (String split : splitS) {
                Integer tmp = Integer.parseInt(split);
                idForBasket.add(tmp);
            }

            winList = Ebean.find(wine.class).where().in("id_product", idForBasket).
                    setDistinct(true).findList();

            //Collections.reverse(idForBasket);
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
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);

        return ok(views.html.basketPage.render(LOGIN,ADMIN, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),form,form2,0));
    }
    public Result addIn(String login, boolean isAdmin,int id){

        String sql = "select max(id_basket) from public.basket";
        int idBasket;
        try {
            idBasket= Integer.parseInt(searchParametrs(sql));
        }
        catch (NumberFormatException e){
            idBasket=0;
        }
        basket bask = new basket();
        bask.setIdBasket((idBasket+1));
        bask.setIdProduct(id);
        bask.setLogin(login);
        Ebean.save(bask);
        return redirect(routes.WineController.catalogPage());
    }
    public Result deleteFromBasket(Integer id,String login,boolean isAdmin){
        String sql2 = "select id_basket from public.basket where id_product="+id + "and login="+ "'" + login +"'";
        SqlQuery maxId = Ebean.createSqlQuery(sql2);
        List<SqlRow> mId = maxId.findList();
        int parametrs = 0;
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = Integer.parseInt(row2.getString(s));
            }
        }
        basket.find.deleteById(parametrs);
        return redirect(routes.BasketController.basketPage());
    }

}

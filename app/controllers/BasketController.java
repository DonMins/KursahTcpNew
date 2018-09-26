package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import javafx.print.Collation;
import models.User;
import models.basket;
import models.rating;
import models.wine;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import java.util.*;

import static controllers.WineController.searchParametrs;
import static scala.collection.JavaConverters.asScalaBuffer;

public class BasketController extends Controller {
    public Result basketPage(String login, boolean isAdmin){
        String sql1 = " select distinct wine.id_product from wine, basket,user" +
                " where wine.id_product in(select basket.id_product " +
                "from public.basket, public.user where public.basket.login = '"+login+"');";

        String wineRating = "";
        SqlQuery maxId = Ebean.createSqlQuery(sql1);
        List<SqlRow> mId = maxId.findList();
        List<wine> winList = null;
        List<wine> winListtmp = null;
        ArrayList<String> nameColomn = new ArrayList<>();
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

            ArrayList<Integer> idForBasket = new ArrayList<>();
            for (String split : splitS) {
                Integer tmp = Integer.parseInt(split);
                idForBasket.add(tmp);
            }

            winList = Ebean.find(wine.class).where().in("id_product", idForBasket).
                    setDistinct(true).findList();

            Collections.reverse(idForBasket);
            for (int i = 0; i < idForBasket.size(); ++i) {
                winList.get(i).setId_product(idForBasket.get(i));
            }

            winList.sort(new Comparator<wine>() {
                @Override
                public int compare(wine o1, wine o2) {
                        if(o2.getId_product()  <=o1.getId_product())
                            return -1;
                        return 1;
                }
            });


            wine us = new wine();
            nameColomn = us.getNameColomn();
        }
        catch (NullPointerException| NumberFormatException ex){
            //winList =null;
            nameColomn = null;
        }

        return ok(views.html.basketPage.render(login,isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList)));
    }
    public Result addIn(String login, boolean isAdmin,int id){

        String sql = "select max(id_basket) from public.basket";
        Integer id_basket;


        try {
            id_basket= Integer.parseInt(searchParametrs(sql));


        }

        catch (NumberFormatException e){
            id_basket=0;
        }


        basket bask = new basket();
        bask.setId_basket((id_basket+1));
        bask.setId_product(id);
        bask.setLogin(login);
        Ebean.save(bask);
        return redirect(routes.WineController.catalogPage(login,isAdmin));
    }
    public Result deleteFromBasket(Integer id,String login,boolean isAdmin){
        System.out.print("fghfghfgh"+id);

        String sql2 = "select id_basket from public.basket where id_product="+id + "and login="+ "'" + login +"'";

        SqlQuery maxId = Ebean.createSqlQuery(sql2);
        List<SqlRow> mId = maxId.findList();
        Integer parametrs = 0;
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = Integer.parseInt(row2.getString(s));
            }
        }


        basket.find.deleteById(parametrs);
        return redirect(routes.BasketController.basketPage(login,isAdmin));
    }

}

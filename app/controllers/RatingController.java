package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.rating;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static scala.collection.JavaConverters.asScalaBuffer;

public class RatingController extends Controller {
    public Result ratingUserPage(String login, boolean isAdmin){
        String sql1 = "select rating.* from wine,public.rating," +
                " public.user where rating.id_user=id and login = '"+login+"'" +
                " and rating.id_product=wine.id_product";
        List<rating> ratingList = new ArrayList<>();
        List<String> nameColomn = new ArrayList<>();
        rating us = new rating();
        nameColomn = us.getNameColomn();
        String parametrs= null;
        SqlQuery maxId = Ebean.createSqlQuery(sql1);

        String wineRating = "";
        String[] splitS = null;

       //надо сделать класс объекта со всеми нужными полями - название вина, оценка
        List<SqlRow> mId = maxId.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
                wineRating = wineRating + parametrs + " ";
            }
        }
            splitS=wineRating.split(" ");
            for(int i=0; i<splitS.length;i=i+3)
            {
                rating us1 = new rating();
                us1.setIdProduct(Integer.parseInt(splitS[i]));
                us1.setIdUser(Integer.parseInt(splitS[i+1]));
                us1.setRating(Integer.parseInt(splitS[i+2]));
                ratingList.add(us1);
            }
        return ok(views.html.usersRatingPage.render(login,isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(ratingList)));
    }

}



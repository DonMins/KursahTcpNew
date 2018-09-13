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

        List<rating> ratingList = rating.find.all();
        ArrayList<String> nameColomn = new ArrayList<>();
        rating us = new rating();
        nameColomn = us.getNameColomn();




        List<String> ratingList1=null;
        String parametrs= null;
        String sql1 = "select wine.name from wine,public.rating, public.user where rating.id_user=id and login = '"+login+"' and rating.id_product=wine.id_product";
        SqlQuery maxId = Ebean.createSqlQuery(sql1);
        rating wine = new rating();

       //надо сделать класс объекта со всеми нужными полями - название вина, оценка
        List<SqlRow> mId = maxId.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
                ratingList1.add(parametrs);



            }
        }




        return ok(views.html.usersRatingPage.render(login,isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(ratingList),JavaConverters.asScalaBuffer(ratingList1)));
    }

}

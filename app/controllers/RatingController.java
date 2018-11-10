package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.Rating;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static controllers.AuxiliaryController.getSessionLogin;
import static scala.collection.JavaConverters.asScalaBuffer;

public class RatingController extends Controller {
    public static Integer error ;
    @Inject
    FormFactory formFactory;

    public Result ratingUserPage(String login, boolean isAdmin){

        String sqlRequest = "select Rating.* from wine,public.Rating," +
                " public.user where Rating.id_user=id and login = '"+login+"'" +
                " and Rating.id_product=wine.id_product";

        List<Rating> ratingList = new ArrayList<>();
        List<String> nameColomn = new ArrayList<>();
        Rating ra = new Rating();
        nameColomn = ra.getNameColomn();
        String parametrs= null;
        SqlQuery maxId = Ebean.createSqlQuery(sqlRequest);

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
                Rating us1 = new Rating();
                us1.setIdProduct(Integer.parseInt(splitS[i]));
                us1.setIdUser(Integer.parseInt(splitS[i+1]));
                us1.setRating(Integer.parseInt(splitS[i+2]));
                ratingList.add(us1);
            }
        return ok(views.html.usersRatingPage.render(login,isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(ratingList)));
    }
    public Result newRating(){
        String login  = getSessionLogin();
        DynamicForm form = formFactory.form().bindFromRequest();
        int idProduct= Integer.parseInt(form.get("getIdProduct"));
        int ratingNew= Integer.parseInt(form.get("Rating"));
        System.out.println("It's working");
        Rating newRating = new Rating();
        newRating.setIdProduct(idProduct);

        String parametrs= null;
        int id_user=0;

        String sql = "SELECT id FROM public.user where login ='"+login+"'";
        SqlQuery userID = Ebean.createSqlQuery(sql);

        List<SqlRow> mId = userID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);

            }
        }
        id_user=Integer.parseInt(parametrs);

        newRating.setIdUser(id_user);
        newRating.setRating(ratingNew);
        Ebean.save(newRating);

        return redirect(routes.WineController.catalogPage());
    }


}



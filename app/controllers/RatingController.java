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

    @Inject
    FormFactory formFactory;

    public Result ratingUserPage(){
        String login = AuxiliaryController.getSessionLogin();
        boolean isAdmin = AuxiliaryController.getSessionAdmin();

        String sqlRequest = "select Rating.* from wine,public.Rating," +
                " public.user where Rating.id_user=id and login = '"+login+"'" +
                " and Rating.id_product=wine.id_product";

        List<Rating> ratingList = new ArrayList<>();
        List<String> nameColomn = new ArrayList<>();
        Rating rating = new Rating();
        nameColomn = rating.getNameColomn();
        String parametrs= null;
        SqlQuery sqlQuery = Ebean.createSqlQuery(sqlRequest);

        String wineRating = "";
        String[] splitS = null;

       //надо сделать класс объекта со всеми нужными полями - название вина, оценка
        List<SqlRow> sqlQueryList = sqlQuery.findList();
        for (SqlRow row : sqlQueryList) {
            Set<String> keyset = row.keySet();
            for (String s : keyset) {
                parametrs = row.getString(s);
                wineRating = wineRating + parametrs + " ";
            }
        }
            splitS=wineRating.split(" ");
            for(int i=0; i<splitS.length;i+=3){
                rating.setIdProduct(Integer.parseInt(splitS[i]));
                rating.setIdUser(Integer.parseInt(splitS[i+1]));
                rating.setRating(Integer.parseInt(splitS[i+2]));
                ratingList.add(rating);
            }
        return ok(views.html.usersRatingPage.render(login,isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(ratingList)));
    }
    public Result newRating(){
        String login  = getSessionLogin();
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        int idProduct= Integer.parseInt(dynamicForm.get("getIdProduct"));
        int ratingNew= Integer.parseInt(dynamicForm.get("Rating"));
        Rating newRating = new Rating();
        newRating.setIdProduct(idProduct);
        String parametrs= null;
        int id_user=0;
        String sqlRequest = "SELECT id FROM public.user where login ='"+login+"'";
        SqlQuery userID = Ebean.createSqlQuery(sqlRequest);
        List<SqlRow> userIDList = userID.findList();
        for (SqlRow row : userIDList) {
            Set<String> keyset = row.keySet();
            for (String s : keyset) {
                parametrs = row.getString(s);
            }
        }
        id_user=Integer.parseInt(parametrs);
        newRating.setIdUser(id_user);
        newRating.setRating(ratingNew);
        Ebean.save(newRating);
        return redirect(routes.WineController.catalogPage());
    }
}



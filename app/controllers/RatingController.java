package controllers;

import io.ebean.*;
import models.Rating;
import models.wine;
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
        if(!(wineRating.equals(""))) {
            splitS = wineRating.split(" ");
            for (int i = 0; i < splitS.length; i += 3) {
                System.out.println(splitS[i] + " " + i);
                System.out.println(splitS[i + 1] + " " + i);
                System.out.println(splitS[i + 2] + " " + i);
                Rating rating1 = new Rating();
                rating1.setIdProduct(Integer.parseInt(splitS[i]));
                rating1.setIdUser(Integer.parseInt(splitS[i + 1]));
                rating1.setRating(Integer.parseInt(splitS[i + 2]));
                ratingList.add(rating1);
            }
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
        String sql = "SELECT AVG(rating) FROM rating where id_product=" + idProduct;
        SqlQuery averageID = Ebean.createSqlQuery(sql);
        Double parametr=0.;

        List<SqlRow> mId = averageID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                System.out.println("qwqwqwwqw " + row2.getString(s));

                if (row2.getString(s)==null)
                    parametr =0.;
                else{
                    System.out.println("qwqwqwwqw " + row2.getString(s));
                    parametr = Double.parseDouble(row2.getString(s));
                }
            }
        }

        String sql1 = "update wine set avgrating ="+parametr+" where id_product= "+idProduct;
        final Update update = Ebean.createUpdate(wine.class, sql1);
        update.setParameter("avgrating", parametr);
        update.execute();
        if (Boolean.parseBoolean(dynamicForm.get("onBasket"))) {
            BasketController basket = new BasketController();
            basket.setNumber(0);
            return redirect(routes.BasketController.basketPage());
        }
        if (Boolean.parseBoolean(dynamicForm.get("onFavorite"))) {
            BasketController basket = new BasketController();
            basket.setNumber(1);
            return redirect(routes.BasketController.basketPage());
        }

        return redirect(routes.WineController.catalogPage());
    }
}



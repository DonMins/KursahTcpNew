package controllers;

import models.User;
import models.wine;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.indexCatalogPage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static scala.collection.JavaConverters.asScalaBuffer;

public class WineController extends Controller {
    String loginn = mainPageController.LOGIN;
    boolean isAdminn = mainPageController.ADMIN;

    public Result catalogPage(String login,boolean isAdmin){

        List<wine> winList = wine.find.all();
        ArrayList<String> nameColomn = new ArrayList<>();
        wine us = new wine();
        nameColomn = us.getNameColomn();
        System.out.print("GFHFGJFGJ  "+login);
        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,isAdmin));
    }
    public Result deleteWine(Integer id,String login){
        wine.find.deleteById(id);
        return redirect(routes.WineController.catalogPage(login,true));
    }
}

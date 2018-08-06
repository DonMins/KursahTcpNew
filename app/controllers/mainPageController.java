package controllers;

import models.workDatabase;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.*;


import java.util.ArrayList;


public class mainPageController extends Controller {
    public static String LOGIN ;
    public static boolean ADMIN;
    public Result test(){
        return ok(indexProjectPage.render("",false));
    }

    public Result projectPage(String login,boolean isAdmin){
        LOGIN=login;
        ADMIN = isAdmin;
        return ok(indexProjectPage.render(login,isAdmin));
    }

//    public Result catalogPage(String login,boolean isAdmin){
//        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("wine");
//
//        return ok(indexCatalogPage.render(
//                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
//                JavaConverters.asScalaBuffer(stek.get(1)).toList(),login,isAdmin));
//    }
    public Result providerPage(String login,boolean isAdmin){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("provider");

        return ok(indexProviderPage.render(
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList(),login,isAdmin));
    }
    public Result contactPage(String login,boolean isAdmin)
    {
        return ok(indexContactPage.render(login,isAdmin));
    }

}

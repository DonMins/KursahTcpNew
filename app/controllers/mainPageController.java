package controllers;

import models.workDatabase;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.*;


import java.util.ArrayList;


public class mainPageController extends Controller {
    public Result test(){
        return ok(indexProjectPage.render(""));
    }

    public Result projectPage(String login){
      return ok(indexProjectPage.render(login));
    }

    public Result catalogPage(String login){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("wine");

        return ok(indexCatalogPage.render(
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList(),login));
    }
    public Result providerPage(String login){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("provider");

        return ok(indexProviderPage.render(
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList(),login));
    }
    public Result contactPage(String login){
        return ok(indexContactPage.render(login));
    }

}

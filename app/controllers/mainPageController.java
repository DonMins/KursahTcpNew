package controllers;

import models.workDatabase;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.indexCatalogPage;
import views.html.indexContactPage;
import views.html.indexProjectPage;
import views.html.indexProviderPage;


import java.util.ArrayList;

public class mainPageController extends Controller {
    public Result projectPage(){

        return ok(indexProjectPage.render());
    }
    public Result catalogPage(){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("wine");

        return ok(indexCatalogPage.render(
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList()));
    }
    public Result providerPage(){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("provider");

        return ok(indexProviderPage.render(
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList()));
    }
    public Result contactPage(){
        return ok(indexContactPage.render());
    }
    public Result personalKab(){
        return ok(indexContactPage.render());
    }
}

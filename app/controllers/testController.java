package controllers;

import models.workDatabase;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.indexCatalogPage;
import views.html.indexStartPage;

import java.util.ArrayList;

public class testController extends Controller {
    public Result test(){

        return ok(indexStartPage.render());
    }
    public Result catalogPage(){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues();

        return ok(indexCatalogPage.render(
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList()));
    }
    public Result providerPage(){
       return ok(indexProviderPage.render());
    }
    public Result contactPage(){
        return ok(indexContactPage.render());
    }
}

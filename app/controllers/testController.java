package controllers;

import models.workDatabase;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.index;
import views.html.indexXml;

import java.util.ArrayList;

public class testController extends Controller {
    public Result test(){
        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues();

        return ok(indexXml.render("FUCK",
                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
                JavaConverters.asScalaBuffer(stek.get(1)).toList()));
    }
}

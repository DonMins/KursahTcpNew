package controllers;

import models.rating;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import java.util.ArrayList;
import java.util.List;

import static scala.collection.JavaConverters.asScalaBuffer;

public class RatingController extends Controller {
    public Result ratingUserPage(String login, boolean isAdmin){

        List<rating> ratingList = rating.find.all();
        ArrayList<String> nameColomn = new ArrayList<>();
        rating us = new rating();
        nameColomn = us.getNameColomn();

        return ok(views.html.usersRatingPage.render(login,isAdmin, JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(ratingList)));
    }

}

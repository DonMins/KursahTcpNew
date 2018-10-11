package controllers;

import models.LoginForm;
import models.User;
import models.contact;
import models.workDatabase;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.*;


import javax.inject.Inject;
import java.util.ArrayList;


public class mainPageController extends Controller {
    public static String LOGIN ;
    public static boolean ADMIN;
    public static Integer error ;
    @Inject
    FormFactory formFactory;

        public Result test(){

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        //System.out.println("GGGGGGGGGGGGGGGG "+ error);
        return ok(indexProjectPage.render("",false,form,form2,error));
    }

    public Result projectPage(String login,boolean isAdmin){
        LOGIN=login;
        ADMIN = isAdmin;
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexProjectPage.render(login,isAdmin,form,form2,error));
    }

//    public Result catalogPage(String login,boolean isAdmin){
//        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("wine");
//
//        return ok(indexCatalogPage.render(
//                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
//                JavaConverters.asScalaBuffer(stek.get(1)).toList(),login,isAdmin));
//    }
//    public Result providerPage(String login,boolean isAdmin){
//        ArrayList<ArrayList<String>> stek = workDatabase.getTableValues("provider");
//
//        return ok(indexProviderPage.render(
//                JavaConverters.asScalaBuffer(stek.get(0)).toList(),
//                JavaConverters.asScalaBuffer(stek.get(1)).toList(),login,isAdmin));
//    }
    public Result contactPage(String login,boolean isAdmin)
    {
        contact new_contact = new contact();
        new_contact.setAdress("г.Самара, ул. Любительская, д. 13, 1 этаж");
        new_contact.setContact_number("555-55-55");
        new_contact.setEmail("DonMins@yandex.ru");

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexContactPage.render(login,isAdmin,form,form2,error));
    }
    public Result ccs()
    {
        return ok(views.html.test.render());
    }
}

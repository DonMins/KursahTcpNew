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
    protected static String LOGIN ;
    protected static boolean ADMIN;
    protected static Integer error ;
    @Inject
    FormFactory formFactory;

    public Result test(){
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexProjectPage.render("",false,form,form2,error));
    }

    public Result projectPage(String login,boolean isAdmin){
        LOGIN=login;
        ADMIN = isAdmin;
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexProjectPage.render(login,isAdmin,form,form2,error));
    }

    public Result contactPage(String login,boolean isAdmin)
    {
        contact new_contact = new contact();
        new_contact.setAdress("г.Самара, ул. Любительская, д. 13, 1 этаж");
        new_contact.setContactNumber("555-55-55");
        new_contact.setEmail("DonMins@yandex.ru");

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexContactPage.render(login,isAdmin,form,form2,error));
    }

}

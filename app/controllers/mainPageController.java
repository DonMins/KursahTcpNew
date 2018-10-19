package controllers;

import models.LoginForm;
import models.User;
import models.contact;
import models.workDatabase;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.*;


import javax.inject.Inject;
import java.util.ArrayList;


public class mainPageController extends Controller {
    protected static String LOGIN  = LoginController.login;
    protected static boolean ADMIN = LoginController.isAdmin;

    protected static Integer error ;
    @Inject
    FormFactory formFactory;

    public Result test(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexProjectPage.render("",false,form,form2,error,requestData));
    }
    public Result test2(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        String firstname = requestData.get("login");

        return ok("Hello " + firstname );
    }

    public Result projectPage(){
        DynamicForm requestData = formFactory.form().bindFromRequest();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        if (session().get("login")!=null) {
            LOGIN = session().get("login");
            ADMIN = Boolean.parseBoolean(session().get("isAdmin"));
            System.out.println("ВОЙ ЛОГИН " + LOGIN);
        }

        else{
            LOGIN="";
            ADMIN=false;
        }
        return ok(indexProjectPage.render(LOGIN,ADMIN,form,form2,error,requestData));
    }

    public Result projectPage2(){
        return redirect(routes.mainPageController.projectPage());
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

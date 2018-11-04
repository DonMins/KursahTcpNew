package controllers;

import models.LoginForm;
import models.User;
import models.contact;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;


import javax.inject.Inject;


public class mainPageController extends Controller {
    protected static String LOGIN ;
    protected static boolean ADMIN ;
    protected static Integer error ;

    @Inject
    FormFactory formFactory;

    public static String getSessionLogin() {
        String login;
        if (session().get("login") != null) {
            login = session().get("login");

        } else {
            login = "";

        }
        return  login;
    }
    public static Boolean getSessionAdmin(){
        boolean isAdmin;
        if (session().get("login") != null) {
            isAdmin = Boolean.parseBoolean(session().get("isAdmin"));

        } else {
            isAdmin = false;

        }
        return  isAdmin;
    }

    public Result test() {

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexProjectPage.render("", false, form, form2, error));
    }


    public Result projectPage() {

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        LOGIN = getSessionLogin();
        ADMIN = getSessionAdmin();
        error=0;
        return ok(indexProjectPage.render(LOGIN,ADMIN,form,form2,error));
    }

    public Result projectPage2(){
        return redirect(routes.mainPageController.projectPage());
    }

    public Result contactPage()
    {   contact newContact = new contact();
        newContact.setAdress("г.Самара, ул. Любительская, д. 13, 1 этаж");
        newContact.setContactNumber("555-55-55");
        newContact.setEmail("DonMins@yandex.ru");
        LOGIN = getSessionLogin();
        ADMIN = getSessionAdmin();

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        return ok(indexContactPage.render(LOGIN,ADMIN,form,form2,error));
    }

}

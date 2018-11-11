package controllers;

import models.LoginForm;
import models.User;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.*;
import javax.inject.Inject;

public class AuxiliaryController extends Controller {

    protected static int ERROR ;
    protected final int NO_ERROR = 0;
    protected final int ERROR_LOGIN_OR_PASSWORD = 1;
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

    public Result ifGuest() {
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        return ok(indexProjectPage.render("", false, loginForm, userForm, ERROR));
    }

    public Result projectPage() {

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        return ok(indexProjectPage.render(login,admin,form,form2,NO_ERROR));
    }

    public Result projectPage2(){
        return redirect(routes.AuxiliaryController.projectPage());
    }
}

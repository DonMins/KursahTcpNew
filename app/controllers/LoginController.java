package controllers;

import io.ebean.Ebean;
import models.User;
import models.LoginForm;
import org.apache.commons.codec.digest.DigestUtils;

import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;


public class LoginController extends Controller {

    @Inject
    FormFactory formFactory;

    public Result renderLoginForm(){

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        return ok(views.html.login.render(form));
    }

    public Result checkingLoginForm(){
        Integer error;
        Form<LoginForm> logForm = formFactory.form(LoginForm.class).bindFromRequest();

        if(logForm.hasGlobalErrors()||logForm.hasErrors() ){
            error = 1; // неверный логин или пароль

            mainPageController.error=error;

            return redirect(routes.mainPageController.test());
        }
        LoginForm log = logForm.get();
        List<User> users = Ebean.find(User.class).where().eq("login", log.getLogin()).findList();
        String passwordHex = DigestUtils.md5Hex(log.getPassword()).toUpperCase();
        User user = users.get(0);

        if( user.getPassword().equals(passwordHex)) {
            error=0;
            mainPageController.error=error; // все хорошо, ошибок нет
            session().put("login", log.getLogin());
            session().put("isAdmin", String.valueOf(user.getAdmin()));
            return redirect(routes.mainPageController.projectPage());
        }

        error = 1; // неверный логин или пароль
        mainPageController.error=error;
        return redirect(routes.mainPageController.test());
    }

    public Result logout(){
        session().remove("login");
        session().remove("isAdmin");
        return redirect(routes.mainPageController.projectPage());
    }

}

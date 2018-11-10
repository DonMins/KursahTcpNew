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
    protected final byte NO_ERROR = 0;
    protected final byte ERROR_LOGIN_OR_PASSWORD = 1;

    public Result renderLoginForm(){
        Form<LoginForm> loginFor = formFactory.form(LoginForm.class);
        return ok(views.html.login.render(loginFor));
    }

    public Result checkingLoginForm(){
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class).bindFromRequest();

        if(loginForm.hasGlobalErrors()||loginForm.hasErrors() ){
            AuxiliaryController.ERROR = ERROR_LOGIN_OR_PASSWORD;
            return redirect(routes.AuxiliaryController.ifGuest());
        }
        LoginForm loginFormForm = loginForm.get();
        List<User> userList = Ebean.find(User.class).where().eq("login", loginFormForm.getLogin()).findList();
        String passwordHex = DigestUtils.md5Hex(loginFormForm.getPassword()).toUpperCase();
        User user = userList.get(0);

        if( user.getPassword().equals(passwordHex)) {
            AuxiliaryController.ERROR=NO_ERROR; // все хорошо, ошибок нет
            session().put("login", loginFormForm.getLogin());
            session().put("isAdmin", String.valueOf(user.getAdmin()));
            return redirect(routes.AuxiliaryController.projectPage());
        }
        AuxiliaryController.ERROR = ERROR_LOGIN_OR_PASSWORD;
        return redirect(routes.AuxiliaryController.ifGuest());
    }

    public Result logout(){
        session().remove("login");
        session().remove("isAdmin");
        return redirect(routes.AuxiliaryController.projectPage());
    }

}

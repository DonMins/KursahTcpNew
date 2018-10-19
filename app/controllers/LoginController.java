package controllers;

import io.ebean.Ebean;
import models.User;
import models.LoginForm;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

/**
 * Controller that render login form and hanlde all request for login and logout.
 */
public class LoginController extends Controller {
    protected static String login;
    protected static boolean isAdmin;
    @Inject
    FormFactory formFactory;
    private static Logger.ALogger logger = Logger.of(LoginController.class);
    /**
     * Render login form
     * @return Response with form in it
     */
    public Result renderLoginForm(){

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        return ok(views.html.login.render(form));
    }
    /**
     * Handle form with data, parse it and serching for such user in database.
     * If success it redirects to page depends on user`s role and put username in session.
     * If there were errors it redirects to loginPage again.
     * @return Responses with redirects to pages by calling their controllers.
     */
    public Result checkingLoginForm(){
        Integer error;
        Form<LoginForm> logForm = formFactory.form(LoginForm.class).bindFromRequest();

        if(logForm.hasGlobalErrors()||logForm.hasErrors() ){
            error = 1; // неверный логин или пароль

            mainPageController.error=error;

            return redirect(routes.mainPageController.test());
        }
        LoginForm log = logForm.get();
        System.out.println(log.toString());
        List<User> users = Ebean.find(User.class).where().eq("login", log.getLogin()).findList();

        User user = users.get(0);

        if( user.getPassword().equals(log.getPassword())) {
            error=0;
            mainPageController.error=error; // все хорошо, ошибок нет
            logger.info("Пользователь " + user.getLogin() + " успешно авторизовался");
            session().put("login", log.getLogin());
            session().put("isAdmin", String.valueOf(user.getAdmin()));
            logger.info("Пользователь " + user.getLogin() + " авторизовался ");
            return redirect(routes.mainPageController.projectPage());
        }

        error = 1; // неверный логин или пароль
        mainPageController.error=error;
        return redirect(routes.mainPageController.test());
    }

    /**
     * Handle logout request and remove user`s name form session.
     * @return Response with redirect to login page.
     */
    public Result logout(){
        logger.info("Пользователь " + session().get("username") + " разлогинился");
        session().remove("login");
        session().remove("isAdmin");
        return redirect(routes.mainPageController.projectPage());
    }

}

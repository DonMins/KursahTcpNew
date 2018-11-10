package controllers;

import io.ebean.Ebean;

import models.Contacts;
import models.LoginForm;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.List;
import static controllers.mainPageController.getSessionAdmin;
import static controllers.mainPageController.getSessionLogin;
import static scala.collection.JavaConverters.asScalaBuffer;

public class ContactController extends Controller {

    protected final byte NO_ERROR = 0;
    @Inject
    FormFactory formFactory;

    public Result contactPage()
    {
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        List<Contacts> contactsList = Contacts.find.all();
        return ok(views.html.contactPage.render(login,admin,loginForm,userForm,NO_ERROR,asScalaBuffer(contactsList)));
    }
    public Result changeContact(Integer idContact)
    {
        DynamicForm dyForm = formFactory.form().bindFromRequest();
        String phone= dyForm.get("getPhone");
        String email= dyForm.get("getEmail");
        String adress= dyForm.get("getAdress");
        String workHours= dyForm.get("getWorkHours");
        Contacts contacts = Contacts.find.byId(idContact);
        contacts.setAdress(adress);
        contacts.setEmail(email);
        contacts.setWorkHours(workHours);
        contacts.setPhone(phone);
        Ebean.update(contacts);
        return redirect(routes.ContactController.contactPage());

    }
}

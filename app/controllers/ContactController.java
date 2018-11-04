package controllers;

import io.ebean.Ebean;
import models.Contact;
import models.LoginForm;
import models.Sale;
import models.User;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.contactPage;


import javax.inject.Inject;

import java.util.List;

import static controllers.mainPageController.getSessionAdmin;
import static controllers.mainPageController.getSessionLogin;
import static scala.collection.JavaConverters.asScalaBuffer;

public class ContactController extends Controller {
    String LOGIN;
    Boolean ADMIN;
    public static Integer error ;
    @Inject
    FormFactory formFactory;
    public Result contactPage()
    {
        LOGIN = getSessionLogin();
        ADMIN = getSessionAdmin();

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        List<Contact> contactList = Contact.find.all();
        return ok(views.html.contactPage.render(LOGIN,ADMIN,form,form2,error,asScalaBuffer(contactList)));
    }
    public Result changeContact(Integer idContact)
    {
        LOGIN = getSessionLogin();
        ADMIN = getSessionAdmin();
        DynamicForm dyForm = formFactory.form().bindFromRequest();
        String phone= dyForm.get("getPhone");
        String email= dyForm.get("getEmail");
        String adress= dyForm.get("getAdress");
        String workHours= dyForm.get("getWorkHours");

        Contact contact = Contact.find.byId(idContact);

        contact.setAdress(adress);
        contact.setEmail(email);
        contact.setWorkHours(workHours);
        contact.setPhone(phone);

        Ebean.update(contact);

        return redirect(routes.ContactController.contactPage());

    }
}

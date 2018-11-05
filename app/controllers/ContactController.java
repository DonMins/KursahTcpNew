package controllers;

import io.ebean.Ebean;


import models.contact;
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

    public static Integer error ;
    @Inject
    FormFactory formFactory;
    public Result contactPage()
    {
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        List<contact> contactList = contact.find.all();
        return ok(views.html.contactPage.render(login,admin,form,form2,error,asScalaBuffer(contactList)));
    }
    public Result changeContact(Integer idContact)
    {
        DynamicForm dyForm = formFactory.form().bindFromRequest();
        String phone= dyForm.get("getPhone");
        String email= dyForm.get("getEmail");
        String adress= dyForm.get("getAdress");
        String workHours= dyForm.get("getWorkHours");

        contact contact = models.contact.find.byId(idContact);

        contact.setAdress(adress);
        contact.setEmail(email);
        contact.setWorkHours(workHours);
        contact.setPhone(phone);

        Ebean.update(contact);

        return redirect(routes.ContactController.contactPage());

    }
}

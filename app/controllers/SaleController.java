package controllers;

import models.LoginForm;
import models.Sale;
import models.User;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import javax.inject.Inject;

import java.util.List;

import static controllers.mainPageController.getSessionAdmin;
import static controllers.mainPageController.getSessionLogin;
import static scala.collection.JavaConverters.asScalaBuffer;

public class SaleController extends Controller {
    String LOGIN;
    Boolean ADMIN;
    public static Integer error ;
    @Inject
    FormFactory formFactory;
    public Result showSales(){
        LOGIN = getSessionLogin();
        ADMIN = getSessionAdmin();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Sale sale = new Sale();
        List<Sale> saleList = Sale.find.all();
        return ok(views.html.salesPage.render(asScalaBuffer(saleList),LOGIN,ADMIN,form,form2,sale,error));
    }

}

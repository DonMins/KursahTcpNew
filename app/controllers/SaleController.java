package controllers;

import io.ebean.Ebean;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;

import java.io.File;
import java.util.List;
import java.util.Map;

import static controllers.WineController.searchParametrs;
import static controllers.AuxiliaryController.getSessionAdmin;
import static controllers.AuxiliaryController.getSessionLogin;
import static scala.collection.JavaConverters.asScalaBuffer;

public class SaleController extends Controller {

    public static Integer error ;
    @Inject
    FormFactory formFactory;
    public Result showSales(){
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<Sale> saleForm = formFactory.form(Sale.class);
        Sale sale = new Sale();
        List<Sale> saleList = Sale.find.all();

        return ok(views.html.salesPage.render(asScalaBuffer(saleList),login,admin,form,form2,sale,error,saleForm));
    }
    public Result changeSale(Integer idSale)
    {
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        DynamicForm dyForm = formFactory.form().bindFromRequest();
        String text= dyForm.get("getText");
        String head= dyForm.get("getHead");

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<Sale> saleForm = formFactory.form(Sale.class);
        Sale sale = Sale.find.byId(idSale);
        List<Sale> saleList = Sale.find.all();
        Sale sale1 = new Sale();

        if(form.hasErrors() || form.hasGlobalErrors()){
            return ok(views.html.salesPage.render(asScalaBuffer(saleList),login,admin,form,form2,sale1,1,saleForm));
        }
        sale.setText(text);
        sale.setHead(head);
        Ebean.update(sale);
        return redirect(routes.SaleController.showSales());
    }
    public Result deleteSale(Integer idSale){
        Ebean.find(Sale.class).where().eq("id_sale" , idSale).delete();
        return redirect(routes.SaleController.showSales());
    }
    public Result newSale(){
        String sql = "select max(id_sale) from public.sales";
        int id;
        try {
            id = Integer.parseInt(searchParametrs(sql));
        }
        catch (NumberFormatException e){
            id=0;
        }
        Form<Sale> sale = formFactory.form(Sale.class).bindFromRequest();
        Map<String, String> rawdata = sale.rawData();
        Sale sale1 = new Sale();
        sale1.setIdSales((id+1));
        sale1.setHead(rawdata.get("head"));
        sale1.setText(rawdata.get("text"));
        Ebean.save(sale1);
        return redirect(routes.SaleController.showSales());
    }
    public Result upload(Integer idSales){
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        if (picture != null) {
            File file = picture.getFile();
            File newFile = new File(play.Play.application().path().toString() + "//public//images//sales//"+ idSales+ ".png" );
            file.renameTo(newFile);
            return redirect(routes.SaleController.showSales());
        } else {
            flash("error", "Missing file");
            return badRequest();
        }

    }

}
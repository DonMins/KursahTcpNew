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
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import static controllers.WineController.searchParametrs;
import static controllers.AuxiliaryController.getSessionAdmin;
import static controllers.AuxiliaryController.getSessionLogin;
import static java.nio.file.Files.deleteIfExists;
import static scala.collection.JavaConverters.asScalaBuffer;

public class SaleController extends Controller {

    public static Integer error ;
    @Inject
    FormFactory formFactory;

    public Result showSales(){
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        Form<Sale> saleForm = formFactory.form(Sale.class);
        Sale sale = new Sale();
        List<Sale> saleList = Sale.find.all();
        return ok(views.html.salesPage.render(asScalaBuffer(saleList),login,admin,loginForm,userForm,sale,error,saleForm));
    }

    public Result changeSale(Integer idSale){
        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        DynamicForm dynamicForm = formFactory.form().bindFromRequest();
        String text= dynamicForm.get("getText");
        String head= dynamicForm.get("getHead");
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        Form<Sale> saleForm = formFactory.form(Sale.class);
        Sale sale = Sale.find.byId(idSale);
        List<Sale> saleList = Sale.find.all();
        Sale saleTmp = new Sale();
        if(loginForm.hasErrors() || loginForm.hasGlobalErrors()){
            return ok(views.html.salesPage.render(asScalaBuffer(saleList),login,admin,loginForm,userForm,saleTmp,1,saleForm));
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
        String sqlRequest = "select max(id_sale) from public.sales";
        int id;
        try {
            id = Integer.parseInt(searchParametrs(sqlRequest));
        }
        catch (NumberFormatException e){
            id=0;
        }
        Form<Sale> sale = formFactory.form(Sale.class).bindFromRequest();
        Map<String, String> rawdata = sale.rawData();
        Sale saleTmp = new Sale();
        saleTmp.setIdSales((id+1));
        saleTmp.setHead(rawdata.get("head"));
        saleTmp.setText(rawdata.get("text"));
        Ebean.save(saleTmp);
        return redirect(routes.SaleController.showSales());
    }

    public Result upload(Integer idSales) {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");

        if (picture != null) {
            File file = picture.getFile();
            File newFile = new File(play.Play.application().path().toString() + "//public//images//sales//"+ idSales+ ".png" );
            Path path = newFile.toPath();

            try {
                deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            file.renameTo(newFile);

            return redirect(routes.SaleController.showSales());
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

    /*public Result upload(Integer idSales){
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

    }*/

}
package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.*;
import play.data.DynamicForm;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.collection.JavaConverters;

import javax.inject.Inject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static controllers.WineController.searchParametrs;
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
        String name = "";

        List< String> wineList =new ArrayList<String>();

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<Sale> saleForm = formFactory.form(Sale.class);
        Sale sale = new Sale();
        List<Sale> saleList = Sale.find.all();
        for(int i =0 ; i<saleList.size();i++)
        {
            name=saleList.get(i).getWine(saleList.get(i).getIdProduct());
            System.out.println(name);
            wineList.add(i,name);
        }


        return ok(views.html.salesPage.render(asScalaBuffer(saleList),LOGIN,ADMIN,form,form2,sale,error,saleForm,asScalaBuffer(wineList)));
    }
    public Result changeSale(Integer idSale, Integer idProduct)
    {
        LOGIN = getSessionLogin();
        ADMIN = getSessionAdmin();
        DynamicForm dyForm = formFactory.form().bindFromRequest();
        String text= dyForm.get("getText");

        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<Sale> saleForm = formFactory.form(Sale.class);
        Sale sale = Sale.find.byId(idSale);
        List<Sale> saleList = Sale.find.all();
        Sale sale1 = new Sale();


        if(form.hasErrors() || form.hasGlobalErrors()){
            return ok(views.html.salesPage.render(asScalaBuffer(saleList),LOGIN,ADMIN,form,form2,sale1,1,saleForm,null));
        }
        sale.setText(text);
        sale.setIdProduct(idProduct);

        Ebean.update(sale);

        return redirect(routes.SaleController.showSales());



    }
    public Result deleteSale(Integer idSale)
    {
        Sale.find.deleteById(idSale);
        return redirect(routes.SaleController.showSales());
    }
    public Result newSale(){
        LOGIN=getSessionLogin();
        ADMIN = getSessionAdmin();
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
        sale1.setIdProduct(Integer.parseInt(rawdata.get("idProduct")));
        sale1.setText(rawdata.get("text"));
        if ((rawdata.get("idProduct").isEmpty())){
            sale1.setIdProduct(null);
        }
                Ebean.save(sale1);


        return redirect(routes.SaleController.showSales());
    }
    public Result upload(Integer idSales){
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
        System.out.println("WWOOOOORK");
        if (picture != null) {
            String fileName = picture.getFilename();
            String contentType = picture.getContentType();
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

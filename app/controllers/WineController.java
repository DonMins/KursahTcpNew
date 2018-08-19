package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;
import views.html.indexCatalogPage;

import javax.inject.Inject;
import java.util.*;

import static scala.collection.JavaConverters.asScalaBuffer;

public class WineController extends Controller {
    String loginn = mainPageController.LOGIN;
    boolean isAdminn = mainPageController.ADMIN;
    public static Integer error ;
    @Inject
    FormFactory formFactory;

    public Result catalogPage(String login,boolean isAdmin){
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class);
        Form<UpdateWine> updateform = formFactory.form(UpdateWine.class).bindFromRequest();

        List<wine> winList = wine.find.all();
        ArrayList<String> nameColomn = new ArrayList<>();
        wine us = new wine();
        nameColomn = us.getNameColomn();

        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,isAdmin,form,form2,error,wineForm,updateform,us));
    }
    public Result deleteWine(Integer id,String login){
        wine.find.deleteById(id);
        return redirect(routes.WineController.catalogPage(login,true));
    }


    public Result renderAddWine(String login){

        Form<wine> form = formFactory.form(wine.class);

        return ok(views.html.createWine.render(form,login));
    }

    public Result addingWine(String login){
        SqlQuery maxId = Ebean.createSqlQuery("select max(id_product) from public.wine");
        Integer id =0;
        List<SqlRow> mId = maxId.findList();
        try {
            for (SqlRow row2 : mId) {
                Set<String> keyset2 = row2.keySet();
                for (String s : keyset2) {
                    id = Integer.parseInt(row2.getString(s));

                }
            }
        }
        catch (NumberFormatException e){
            id=0;
        }


        Form<wine> win = formFactory.form(wine.class).bindFromRequest();
        if(win.hasErrors() || win.hasGlobalErrors()){
            return ok(views.html.createWine.render(win,login));
        }
        Map<String, String> rawdata = win.rawData();
        wine Win = new wine();
        //User user = userForm.get();
        Win.setId_product((id+1));
        System.out.println("IDDDDDDDDD "+ Win.getId_product());
        Win.setName(rawdata.get("name"));
        Win.setColour(rawdata.get("colour"));
        Win.setCountry(rawdata.get("country"));
        Win.setBrand(rawdata.get("brand"));
        Win.setShelf_life(rawdata.get("shelf_life"));
        Win.setSugar(rawdata.get("sugar"));
        Win.setGrape_sort(rawdata.get("grape_sort"));
        Win.setPrice(Double.valueOf(rawdata.get("price")));
        Win.setValue(Double.valueOf(rawdata.get("value")));
        Win.setDegree(Double.valueOf(rawdata.get("degree")));

        List<wine> winne = Ebean.find(wine.class).where().eq("id_product", Win.getId_product()).findList();
        if(winne.isEmpty()){
            try{
                Ebean.save(Win);
            }catch (Exception ex){

                System.out.println("ГОАНООООЭ");

                return redirect(routes.WineController.renderAddWine(login));
            }

            return redirect(routes.WineController.catalogPage(login,true));

        }

        return redirect(routes.WineController.renderAddWine(login));
    }
    public Result renderUpdateWineInfo(Integer id,String login){
        wine win = wine.find.byId(id);
        UpdateWine update = new UpdateWine(win.getName(),win.getColour(),win.getCountry(),win.getBrand(),
                win.getShelf_life(),win.getSugar(),win.getGrape_sort(),win.getPrice(),
                win.getValue(),win.getDegree());

        Form<UpdateWine> updateForm = formFactory.form(UpdateWine.class).fill(update);
        return ok(views.html.updateWine.render(updateForm, win,login));

    }
    public Result updateWineInfo(Integer id,String login){
        wine Win = wine.find.byId(id);
        List<wine> winList = wine.find.all();
        ArrayList<String> nameColomn = new ArrayList<>();
        wine us = new wine();
        nameColomn = us.getNameColomn();
        Form<UpdateWine> updateform = formFactory.form(UpdateWine.class).bindFromRequest();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class);
        if(form.hasErrors() || form.hasGlobalErrors()){
            return ok(views.html.catalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                    ,asScalaBuffer(winList),login,isAdminn,form,form2,1,wineForm,updateform, Win));
        }
        Map<String, String> rawdata = form.rawData();

        Win.setName(rawdata.get("name"));
        Win.setColour(rawdata.get("colour"));
        Win.setCountry(rawdata.get("country"));
        Win.setBrand(rawdata.get("brand"));
        Win.setShelf_life(rawdata.get("shelf_life"));
        Win.setSugar(rawdata.get("sugar"));
        Win.setGrape_sort(rawdata.get("grape_sort"));
        Win.setPrice(Double.valueOf(rawdata.get("price")));
        Win.setValue(Double.valueOf(rawdata.get("value")));
        Win.setDegree(Double.valueOf(rawdata.get("degree")));
        Ebean.update(Win);

        return redirect(routes.WineController.catalogPage(login,true));
    }

}

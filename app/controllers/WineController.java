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
import static scala.collection.JavaConverters.seqAsJavaList;

public class WineController extends Controller {
    String loginn = mainPageController.LOGIN;
    boolean isAdminn = mainPageController.ADMIN;
    public static Integer error ;
    public List<wine> searchList = null;
    @Inject
    FormFactory formFactory;

    public Result catalogPage(String login,boolean isAdmin){
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class);
        Form<search> searchForm = formFactory.form(search.class).bindFromRequest();
        search searchParam = searchForm.get();


        Form<UpdateWine> updateform = formFactory.form(UpdateWine.class).bindFromRequest();

        List<wine> winList = wine.find.all();

        ArrayList<String> nameColomn = new ArrayList<>();
        wine us = new wine();

        nameColomn = us.getNameColomn();

        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,isAdmin,form,form2,error,wineForm,updateform,us,searchForm));
    }


    public Result sortCatalogPage(String login,boolean isAdmin,int sortNumber){
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class).bindFromRequest();
        Form<search> searchForm = formFactory.form(search.class).bindFromRequest();

        Form<UpdateWine> updateform = formFactory.form(UpdateWine.class).bindFromRequest();
        List<wine> winList = null;
        if (searchList!=null)
            winList = searchList;
        else {
            winList = wine.find.all();
        }
        winList.sort(new Comparator<wine>() {
            @Override
            public int compare(wine o1, wine o2) {
                if(sortNumber ==0){
                    if(o1.getPrice() <= o2.getPrice())
                        return -1;
                    return 1;
                }
                if(sortNumber ==1){
                    int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
                    if (res == 0) {
                        res = o1.getName().compareTo(o2.getName());
                    }
                    return res;
                }
                if (sortNumber ==2)
                    return 0;

                return -1;

            }
        });
        ArrayList<String> nameColomn = new ArrayList<>();
        wine us = new wine();

        nameColomn = us.getNameColomn();

        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,isAdmin,form,form2,error,wineForm,updateform,us,searchForm));
    }

    public Result searchCatalogPage(String login,boolean isAdmin){
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);

        Form<UpdateWine> updateform = formFactory.form(UpdateWine.class);

        Form<wine> wineForm = formFactory.form(wine.class).bindFromRequest();
        Form<search> searchForm = formFactory.form(search.class).bindFromRequest();
        wine winParam= wineForm.get();
        search searchParam = searchForm.get();

        List<wine> winList = null;

        if (searchParam.getMinprice() == null){
            searchParam.setMinprice(0.0);
        }
        if (searchParam.getMaxprice() == null){
            String sql = "select max(price) from public.wine";
            searchParam.setMaxprice(Double.parseDouble(searchParametrs(sql)));
        }

        if (!(winParam.getName().isEmpty()) && searchParam.getMinprice() != null && searchParam.getMaxprice() != null) {
            winList = Ebean.find(wine.class).where().eq("name", winParam.getName()).
                    between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                    setDistinct(true).findList();
                    }
        if(winParam.getName().isEmpty()){
            winList = Ebean.find(wine.class).where().
                    between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                    setDistinct(true).findList();
        }
        searchList = winList;

        ArrayList<String> nameColomn = new ArrayList<>();
        wine us = new wine();

        nameColomn = us.getNameColomn();

        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,isAdmin,form,form2,error,wineForm,updateform,us,searchForm));
    }
    public Result deleteWine(Integer id,String login){
        wine.find.deleteById(id);
        return redirect(routes.WineController.catalogPage(login,true));
    }


    public Result renderAddWine(String login){

        Form<wine> form = formFactory.form(wine.class);

        return ok(views.html.createWine.render(form,login));
    }

    public static String searchParametrs(String sql){
        String parametrs = null;
        SqlQuery maxId = Ebean.createSqlQuery(sql);
        List<SqlRow> mId = maxId.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
            }
        }
       return parametrs;

    }
    public Result addingWine(String login){
        String sql = "select max(id_product) from public.wine";
        Integer id;

        try {
         id = Integer.parseInt(searchParametrs(sql));

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
        Form<search> searchForm = formFactory.form(search.class);
        if(form.hasErrors() || form.hasGlobalErrors()){
            return ok(views.html.catalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                    ,asScalaBuffer(winList),login,isAdminn,form,form2,1,wineForm,updateform, Win,searchForm));
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

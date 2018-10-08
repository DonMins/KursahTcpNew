package controllers;

import io.ebean.Ebean;
import io.ebean.Expr;
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
                if(sortNumber ==5){
                    if(o2.getPrice() <= o1.getPrice())
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
                if(sortNumber ==2){
                    int res = String.CASE_INSENSITIVE_ORDER.compare(o2.getName(), o1.getName());
                    if (res == 0) {
                        res = o2.getName().compareTo(o1.getName());
                    }
                    return res;
                }
                if(sortNumber ==3){
                    if(o1.getDegree() <= o2.getDegree())
                        return -1;
                    return 1;
                }
                if(sortNumber ==4) {
                    if (o2.getDegree() <= o1.getDegree())
                        return -1;
                    return 1;
                }

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
        Map<String, String> rawdata = searchForm.rawData();


        List<wine> winList = null;
        if(wineForm.hasErrors() || wineForm.hasGlobalErrors()||
                searchForm.hasErrors() || searchForm.hasGlobalErrors()){
            ArrayList<String> nameColomn = new ArrayList<>();
            wine us = new wine();

            nameColomn = us.getNameColomn();
            winList = wine.find.all();

            return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                    ,asScalaBuffer(winList),login,isAdmin,form,form2,error,wineForm,updateform,us,searchForm));
        }
        wine winParam= wineForm.get();
        search searchParam = searchForm.get();
        String colourWin ="";
        String typeWin="";

        if (rawdata.get("dry")==null){
            searchParam.setDry(false);
        }
        else {
            searchParam.setDry(true);
            typeWin="Сухое";
        }

        if (rawdata.get("semidry")==null){
            searchParam.setSemidry(false);
        }
        else {
            searchParam.setSemidry(true);
            typeWin="Полусухое";
        }

        if (rawdata.get("semisweet")==null){
            searchParam.setSemisweet(false);
        }
        else {
            searchParam.setSemisweet(true);
            typeWin="Полусладкое";
        }

        if (rawdata.get("sweet")==null){
            searchParam.setSweet(false);
        }
        else {
            searchParam.setSweet(true);
            typeWin="Сладкое";
        }

        if (rawdata.get("anySugar")==null){
            searchParam.setAnySugar(false);
        }
        else {
            searchParam.setAnySugar(true);
        }


        if (rawdata.get("anyColour")==null){
            searchParam.setAnyColour(false);
        }
        else {
            searchParam.setAnyColour(true);
        }

        if (rawdata.get("redColour")==null){
            searchParam.setRedColour(false);
        }
        else {
            searchParam.setRedColour(true);
            colourWin="Красное";
        }
        if (rawdata.get("pinkColour")==null){
            searchParam.setPinkColour(false);
        }
        else {
            searchParam.setPinkColour(true);
            colourWin = "Розовое";
        }
        if (rawdata.get("whiteColour")==null){
            searchParam.setWhiteColour(false);
        }
        else {
            searchParam.setWhiteColour(true);
            colourWin = "Белое";
        }

        if (searchParam.getMinprice() == null){
            searchParam.setMinprice(0.0);
        }
        if (searchParam.getMaxprice() == null){
            String sql = "select max(price) from public.wine";
            searchParam.setMaxprice(Double.parseDouble(searchParametrs(sql)));
        }

        boolean notColour = false ;
        boolean notSugar = false;
        if(!searchParam.isPinkColour() && !searchParam.isWhiteColour() &&
                !searchParam.isRedColour()){
            notColour = true;
        }
        if (!searchParam.isDry() && !searchParam.isSemidry() &&
                !searchParam.isSemisweet()&& !searchParam.isSweet() ){
            notSugar = true;
        }


        // если все пусто и только фильтр на тип
        if((winParam.getName().isEmpty()) && (winParam.getCountry().isEmpty()) &&
                (notColour)){
            // тип вина
            if (searchParam.isDry() || searchParam.isSemidry() ||
                    searchParam.isSemisweet()|| searchParam.isSweet() ){
                winList = Ebean.find(wine.class).where().
                        between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                        eq("sugar",typeWin).
                        setDistinct(true).findList();
            }
            if (searchParam.isAnySugar()){
                winList = Ebean.find(wine.class).where().
                        between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                        setDistinct(true).findList();
            }
        }
        // если все пусто кроме цвета вина
        if((winParam.getName().isEmpty()) && (winParam.getCountry().isEmpty()) &&
                (notSugar || searchParam.isAnySugar())){
            // цвет вина
            if (searchParam.isPinkColour() || searchParam.isWhiteColour() ||
            searchParam.isRedColour()){
            winList = Ebean.find(wine.class).where().
                    between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                    eq("colour",colourWin).
                    setDistinct(true).findList();
            }
            if (searchParam.isAnyColour()){
                winList = Ebean.find(wine.class).where().
                        between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                        setDistinct(true).findList();
            }
            // если вообще нет фильра на вино
            if (!searchParam.isPinkColour() && !searchParam.isWhiteColour() &&
                    !searchParam.isRedColour())
                winList = Ebean.find(wine.class).where().
                        between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                        setDistinct(true).findList();
        }
        // если все пусто кроме цвета вина и типа
        if((winParam.getName().isEmpty()) && (winParam.getCountry().isEmpty()) &&
                (!notSugar) && (!notColour) ){
            winList = Ebean.find(wine.class).where().
                    between("price",searchParam.getMinprice(),searchParam.getMaxprice()).
                    eq("colour",colourWin).eq("sugar",typeWin).
                    setDistinct(true).findList();
        }

        // если все данные в фильтр введены
        if (!(winParam.getName().isEmpty()) && searchParam.getMinprice() != null && searchParam.getMaxprice() != null
        && (!(winParam.getCountry().isEmpty()))) {
          // если введены цвет и сахар
            if ((!notColour) && (!notSugar)) {
                winList = Ebean.find(wine.class).where().contains("name", winParam.getName()).
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice())
                        .eq("country", winParam.getCountry()).eq("colour", colourWin)
                        .eq("sugar", typeWin).setDistinct(true).findList();
            }
            //если  ввели цвет и любой сахар
            if ((!notColour) && (searchParam.isAnySugar())){
                winList = Ebean.find(wine.class).where().contains("name", winParam.getName()).
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice())
                        .eq("country", winParam.getCountry()).eq("colour", colourWin)
                        .setDistinct(true).findList();
            }
            // если ввели сахар и любой цвет
            if ((!notSugar) && (searchParam.isAnyColour())){
                winList = Ebean.find(wine.class).where().contains("name", winParam.getName()).
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice())
                        .eq("country", winParam.getCountry()).eq("sugar", typeWin)
                        .setDistinct(true).findList();
            }
            // если ввели цвет, но не ввели сахар
            if ((notSugar) && (!notColour)){
                winList = Ebean.find(wine.class).where().contains("name", winParam.getName()).
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice())
                        .eq("country", winParam.getCountry()).eq("colour", colourWin)
                        .setDistinct(true).findList();
            }
            // если ввели сахар, но не ввели цвет
            if ((notSugar) && (!notColour)){
                winList = Ebean.find(wine.class).where().contains("name", winParam.getName()).
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice())
                        .eq("country", winParam.getCountry()).eq("sugar", typeWin)
                        .setDistinct(true).findList();
            }
            // если оба любые
            if(searchParam.isAnySugar() && searchParam.isAnyColour()){
                winList = Ebean.find(wine.class).where().contains("name", winParam.getName()).
                        between("price",searchParam.getMinprice(),searchParam.getMaxprice())
                        .eq("country",winParam.getCountry()).setDistinct(true).findList();
            }
        }

        // если введено только название
        if(!(winParam.getName().isEmpty()) && winParam.getCountry().isEmpty()) {
            // если нет цвета и типа
            if (notColour && notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name",winParam.getName()).setDistinct(true).findList();
            }
            // если нет цвета , но есть тип
            if (notColour && !notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name", winParam.getName()).
                        eq("sugar", typeWin).setDistinct(true).findList();
            }
            // если нет типа, но есть цвет
            if (!notColour && notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name", winParam.getName()).
                        eq("colour", colourWin).setDistinct(true).findList();
            }
            //если есть тип и цвет
            if (!notColour && !notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name", winParam.getName()).
                        eq("colour", colourWin).
                        eq("sugar", typeWin).setDistinct(true).findList();
            }
            // если есть цвет и любой сахар
            if (!notColour && searchParam.isAnySugar()) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name", winParam.getName()).
                        eq("colour", colourWin).setDistinct(true).findList();
            }
            // если есть сахар и любой цвет
            if (!notSugar && searchParam.isAnyColour()) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name", winParam.getName()).
                        eq("sugar", typeWin).setDistinct(true).findList();
            }
            // если есть любой цвет и любой сахар
            if (searchParam.isAnySugar() && searchParam.isAnyColour()) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        contains("name", winParam.getName()).
                        setDistinct(true).findList();
            }
        }


        //если введена только страна
        if((winParam.getName().isEmpty()) && (!(winParam.getCountry().isEmpty()))){
            // если нет цвета и типа
            if (notColour && notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).setDistinct(true).findList();
            }
            // если нет цвета , но есть тип
            if (notColour && !notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).
                        eq("sugar", typeWin).setDistinct(true).findList();
            }
            // если нет типа, но есть цвет
            if (!notColour && notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).
                        eq("colour", colourWin).setDistinct(true).findList();
            }
            //если есть тип и цвет
            if (!notColour && !notSugar) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).
                        eq("colour", colourWin).
                        eq("sugar", typeWin).setDistinct(true).findList();
            }
            // если есть цвет и любой сахар
            if (!notColour && searchParam.isAnySugar()) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).
                        eq("colour", colourWin).setDistinct(true).findList();
            }
            // если есть сахар и любой цвет
            if (!notSugar && searchParam.isAnyColour()) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).
                        eq("sugar", typeWin).setDistinct(true).findList();
            }
            // если есть любой цвет и любой сахар
            if (searchParam.isAnySugar() && searchParam.isAnyColour()) {
                winList = Ebean.find(wine.class).where().
                        between("price", searchParam.getMinprice(), searchParam.getMaxprice()).
                        eq("country", winParam.getCountry()).
                        setDistinct(true).findList();
            }
        }

//

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

        Win.setId_product((id+1));
        Win.setName(rawdata.get("name"));
        Win.setColour(rawdata.get("colour"));
        Win.setCountry(rawdata.get("country"));
        Win.setBrand(rawdata.get("brand"));
        Win.setShelf_life(rawdata.get("shelf_life"));
        Win.setSugar(rawdata.get("sugar"));
        Win.setGrape_sort(rawdata.get("grape_sort"));
        if ((rawdata.get("price").isEmpty())){
                Win.setPrice(null);
            }
            else{
                Win.setPrice(Double.valueOf(rawdata.get("price")));
            }

        if ((rawdata.get("value").isEmpty())){
            Win.setValue(null);
        }
        else{
        Win.setValue(Double.valueOf(rawdata.get("value")));
        }
        if (rawdata.get("degree").isEmpty()){
            Win.setDegree(null);
        }
        else {
            Win.setDegree(Double.valueOf(rawdata.get("degree")));
        }

        List<wine> winne = Ebean.find(wine.class).where().eq("id_product", Win.getId_product()).findList();
        if(winne.isEmpty()){
            try{
                Ebean.save(Win);
            }catch (Exception ex){



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
        Map<String, String> rawdata = updateform.rawData();
        Win.setName(rawdata.get("name"));
        Win.setColour(rawdata.get("colour"));
        Win.setCountry(rawdata.get("country"));
        Win.setBrand(rawdata.get("brand"));
        Win.setShelf_life(rawdata.get("shelf_life"));
        Win.setSugar(rawdata.get("sugar"));
        Win.setGrape_sort(rawdata.get("grape_sort"));
        if ((rawdata.get("value").isEmpty())){
            Win.setValue(null);
        }
        else{
            Win.setValue(Double.valueOf(rawdata.get("value")));
        }
        if (rawdata.get("degree").isEmpty()){
            Win.setDegree(null);
        }
        else {
            Win.setDegree(Double.valueOf(rawdata.get("degree")));
        }

         if ((rawdata.get("price").isEmpty())){
                Win.setPrice(null);
            }
            else{
                Win.setPrice(Double.valueOf(rawdata.get("price")));
            }
        Ebean.update(Win);

        return redirect(routes.WineController.catalogPage(login,true));
    }
    public Result newRating(Integer id_product,String login,Integer ratingNew){
        System.out.println("It's working");
        rating newRating = new rating();
        newRating.setId_product(id_product);

        String parametrs= null;
        int id_user=0;

        String sql = "SELECT id FROM public.user where login ='"+login+"'";
        SqlQuery userID = Ebean.createSqlQuery(sql);

        List<SqlRow> mId = userID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);

            }
        }
        id_user=Integer.parseInt(parametrs);

        newRating.setId_user(id_user);
        newRating.setRating(ratingNew);
        Ebean.save(newRating);

        return redirect(routes.WineController.catalogPage(login,true));
    }



}

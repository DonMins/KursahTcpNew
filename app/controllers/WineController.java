package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.*;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import scala.collection.JavaConverters;

import javax.inject.Inject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import static controllers.AuxiliaryController.getSessionLogin;
import static controllers.AuxiliaryController.getSessionAdmin;
import static java.nio.file.Files.deleteIfExists;
import static scala.collection.JavaConverters.asScalaBuffer;

public class WineController extends Controller {
    protected final int NO_ERROR = 0;
    protected final int ERROR_LOGIN_OR_PASSWORD = 1;
    public List<wine> searchList = null;
    @Inject

    FormFactory formFactory;

    public Result catalogPage(){

        String login = getSessionLogin();
        boolean admin = getSessionAdmin();
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class);
        Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
        Form<UpdateWine> updateWineForm = formFactory.form(UpdateWine.class).bindFromRequest();
        List<wine> winList = wine.find.all();
        winList.sort(new Comparator<wine>() {
            @Override
            public int compare(wine o1, wine o2) {
                    if(o1.getIdProduct() <= o2.getIdProduct())
                        return -1;
                    return 1;}});

        List<String> nameColomn = new ArrayList<>();
        wine tmp = new wine();
        nameColomn = tmp.getNameColomn();
        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,admin,loginForm,userForm,NO_ERROR,wineForm,updateWineForm,tmp,searchForm));
    }

    public Result upload(Integer idProduct) {
        Http.MultipartFormData<File> body = request().body().asMultipartFormData();
        Http.MultipartFormData.FilePart<File> picture = body.getFile("picture");
       
        if (picture != null) {
            File file = picture.getFile();
            File newFile = new File(play.Play.application().path().toString() + "//public//images//wines//"+ idProduct+ ".png" );
            Path path = newFile.toPath();

            try {
                deleteIfExists(path);
            } catch (IOException e) {
                e.printStackTrace();
            }

            file.renameTo(newFile);
            
            return redirect(routes.WineController.catalogPage());
        } else {
            flash("error", "Missing file");
            return badRequest();
        }
    }

    public Result sortCatalogPage(int sortNumber){
        final int ON_PRICE_UP=0;
        final int ON_PRICE_DOWN=5;
        final int ON_NAME_UP=1;
        final int ON_NAME_DOWN=2;
        final int ON_RATING_DOWN=4;
        final int ON_RATING_UP=3;

        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class).bindFromRequest();
        Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
        Form<UpdateWine> updateWineForm = formFactory.form(UpdateWine.class).bindFromRequest();
        List<wine> winList = null;
        if (searchList!=null)
            winList = searchList;
        else {
            winList = wine.find.all();
        }


         winList.sort(new Comparator<wine>() {
            @Override
            public int compare(wine o1, wine o2) {
                if(sortNumber ==ON_PRICE_UP){
                    if(o1.getPrice() <= o2.getPrice())
                        return -1;
                    return 1;
                }
                if(sortNumber ==ON_PRICE_DOWN){
                    if(o2.getPrice() <= o1.getPrice())
                        return -1;
                    return 1;
                }
                if(sortNumber ==ON_NAME_UP){
                    int res = String.CASE_INSENSITIVE_ORDER.compare(o1.getName(), o2.getName());
                    if (res == 0) {
                        res = o1.getName().compareTo(o2.getName());
                    }
                    return res;
                }
                if(sortNumber ==ON_NAME_DOWN){
                    int res = String.CASE_INSENSITIVE_ORDER.compare(o2.getName(), o1.getName());
                    if (res == 0) {
                        res = o2.getName().compareTo(o1.getName());
                    }
                    return res;
                }
                if(sortNumber == ON_RATING_DOWN) {

                    if (o2.getAvgRating() <= o1.getAvgRating())
                        return -1;
                    return 1;
                }
                if(sortNumber == ON_RATING_UP) {

                    if (o1.getAvgRating() <= o2.getAvgRating())
                        return -1;
                    return 1;
                }
                return -1;
            }
        });

        List<String> nameColomn = new ArrayList<>();
        wine tmp = new wine();
        nameColomn = tmp.getNameColomn();
        String login = getSessionLogin();
        boolean isAdmin = getSessionAdmin();

        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),login,isAdmin,loginForm,userForm,NO_ERROR,wineForm,updateWineForm,tmp,searchForm));
    }

    public Result searchCatalogPage(){
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        Form<UpdateWine> updateWineForm = formFactory.form(UpdateWine.class);

        Form<wine> wineForm = formFactory.form(wine.class).bindFromRequest();
        Form<Search> searchForm = formFactory.form(Search.class).bindFromRequest();
        Map<String, String> rawdata = searchForm.rawData();
        List<wine> winList = null;
        if(wineForm.hasErrors() || wineForm.hasGlobalErrors()||
                searchForm.hasErrors() || searchForm.hasGlobalErrors()){
            List<String> nameColomn = new ArrayList<>();
            wine tmp = new wine();
            nameColomn = tmp.getNameColomn();
            winList = wine.find.all();
            String login = getSessionLogin();
            boolean isAdmin = getSessionAdmin();
            return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                    ,asScalaBuffer(winList),login,isAdmin,loginForm,userForm,NO_ERROR,wineForm,updateWineForm,tmp,searchForm));
        }
        wine winParam= wineForm.get();
        Search searchParam = searchForm.get();
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

        searchList = winList;
        List<String> nameColomn = new ArrayList<>();
        wine tmp = new wine();
        nameColomn = tmp.getNameColomn();
        return ok(views.html.indexCatalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(winList),getSessionLogin(),getSessionAdmin(),loginForm,userForm,NO_ERROR,wineForm,updateWineForm,tmp,searchForm));
    }
    public Result deleteWine(Integer id){
        wine.find.deleteById(id);
        return redirect(routes.WineController.catalogPage());
    }

    public Result renderAddWine(){
        String login = getSessionLogin();
        Form<wine> form = formFactory.form(wine.class);
        return ok(views.html.createWine.render(form,login));
    }

    public static String searchParametrs(String sql){
        String parametrs = null;
        SqlQuery sqlQuery = Ebean.createSqlQuery(sql);
        List<SqlRow> sqlQueryList = sqlQuery.findList();
        for (SqlRow row : sqlQueryList) {
            Set<String> keySet = row.keySet();
            for (String s : keySet) {
                parametrs = row.getString(s);
            }
        }
       return parametrs;

    }

    public Result addingWine(){
        String sqlRequest = "select max(id_product) from public.wine";
        String login = getSessionLogin();
        int id;
        try {
         id = Integer.parseInt(searchParametrs(sqlRequest));
        }
        catch (NumberFormatException e){
            id=0;
        }
        Form<wine> wineForm = formFactory.form(wine.class).bindFromRequest();
        if(wineForm.hasErrors() || wineForm.hasGlobalErrors()){
            return ok(views.html.createWine.render(wineForm,login));
        }
        Map<String, String> rawdata = wineForm.rawData();
        wine winTmp = new wine();
        winTmp.setIdProduct((id+1));
        winTmp.setName(rawdata.get("name"));
        winTmp.setColour(rawdata.get("colour"));
        winTmp.setCountry(rawdata.get("country"));
        winTmp.setBrand(rawdata.get("brand"));
        winTmp.setShelfLife(rawdata.get("shelfLife"));
        winTmp.setSugar(rawdata.get("sugar"));
        winTmp.setGrapeSort(rawdata.get("grapeSort"));
        winTmp.setAvgRating(0.);
        if ((rawdata.get("price").isEmpty())){
                winTmp.setPrice(null);
            }
            else{
                winTmp.setPrice(Double.valueOf(rawdata.get("price")));
            }

        if ((rawdata.get("value").isEmpty())){
            winTmp.setValue(null);
        }
        else{
        winTmp.setValue(Double.valueOf(rawdata.get("value")));
        }
        if (rawdata.get("degree").isEmpty()){
            winTmp.setDegree(null);
        }
        else {
            winTmp.setDegree(Double.valueOf(rawdata.get("degree")));
        }

        List<wine> wineList = Ebean.find(wine.class).where().eq("id_product", winTmp.getIdProduct()).findList();
        if(wineList.isEmpty()){
            try{
                Ebean.save(winTmp);
            }catch (Exception ex){
                return redirect(routes.WineController.renderAddWine());
            }
            return redirect(routes.WineController.catalogPage());
        }
        return redirect(routes.WineController.renderAddWine());
    }
    public Result renderUpdateWineInfo(Integer id){
        String login = getSessionLogin();
        wine wineFind = wine.find.byId(id);
        UpdateWine update = new UpdateWine(wineFind.getName(),wineFind.getColour(),wineFind.getCountry(),wineFind.getBrand(),
                wineFind.getShelfLife(),wineFind.getSugar(),wineFind.getGrapeSort(),wineFind.getPrice(),
                wineFind.getValue(),wineFind.getDegree());

        Form<UpdateWine> updateForm = formFactory.form(UpdateWine.class).fill(update);
        return ok(views.html.updateWine.render(updateForm, wineFind,login));

    }
    public Result updateWineInfo(Integer id){

        wine wineFind = wine.find.byId(id);
        List<wine> winList = wine.find.all();
        List<String> nameColomn = new ArrayList<>();
        wine tmp = new wine();
        nameColomn = tmp.getNameColomn();
        Form<UpdateWine> updateWineForm = formFactory.form(UpdateWine.class).bindFromRequest();
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        Form<User> userForm = formFactory.form(User.class);
        Form<wine> wineForm = formFactory.form(wine.class);
        Form<Search> searchForm = formFactory.form(Search.class);

        if(wineForm.hasErrors() || wineForm.hasGlobalErrors()){
            return ok(views.html.catalogPage.render(JavaConverters.asScalaBuffer(nameColomn)
                    ,asScalaBuffer(winList),getSessionLogin(), getSessionAdmin(),loginForm,userForm,ERROR_LOGIN_OR_PASSWORD,wineForm,updateWineForm, wineFind,searchForm));
        }
        if(updateWineForm.hasErrors() || updateWineForm.hasGlobalErrors()){

            return ok(views.html.updateWine.render(updateWineForm, wineFind,getSessionLogin()));

        }
        Map<String, String> rawdata = updateWineForm.rawData();
        wineFind.setName(rawdata.get("name"));
        wineFind.setColour(rawdata.get("colour"));
        wineFind.setCountry(rawdata.get("country"));
        wineFind.setBrand(rawdata.get("brand"));
        wineFind.setShelfLife(rawdata.get("shelfLife"));
        wineFind.setSugar(rawdata.get("sugar"));
        wineFind.setGrapeSort(rawdata.get("grapeSort"));
        if ((rawdata.get("value").isEmpty())){
            wineFind.setValue(null);
        }
        else{
            wineFind.setValue(Double.valueOf(rawdata.get("value")));
        }
        if (rawdata.get("degree").isEmpty()){
            wineFind.setDegree(null);
        }
        else {
            wineFind.setDegree(Double.valueOf(rawdata.get("degree")));
        }

         if ((rawdata.get("price").isEmpty())){
                wineFind.setPrice(null);
            }
            else{
                wineFind.setPrice(Double.valueOf(rawdata.get("price")));
            }
        Ebean.update(wineFind);

        return redirect(routes.WineController.catalogPage());
    }

}




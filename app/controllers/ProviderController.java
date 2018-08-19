package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.UpdateProvid;
import models.UpdateWine;
import models.provider;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static scala.collection.JavaConverters.asScalaBuffer;

public class ProviderController extends Controller {

    String loginn = mainPageController.LOGIN;
    boolean isAdminn = mainPageController.ADMIN;

    public Result providerPage(String login, boolean isAdmin){

        List<provider> providerList = provider.find.all();
        ArrayList<String> nameColomn = new ArrayList<>();
        provider us = new provider();
        nameColomn = us.getNameColomn();

        return ok(views.html.indexProviderPage.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(providerList),login,isAdmin));
    }
    public Result deleteProvider(Integer id,String login){
        provider.find.deleteById(id);
        return redirect(routes.ProviderController.providerPage(login,true));
    }

    @Inject
    FormFactory formFactory;
    public Result renderAddProvider(String login){

        Form<provider> form = formFactory.form(provider.class);

        return ok(views.html.createProvider.render(form,login));
    }

    public Result addingProvider(String login){
        SqlQuery maxId = Ebean.createSqlQuery("select max(id_provider) from public.provider");
        Integer id =0;
        List<SqlRow> mId = maxId.findList();
        try{
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                id = Integer.parseInt(row2.getString(s));

            }
        }
        }
        catch (NumberFormatException e){
            id = 0;
        }


        Form<provider> prov = formFactory.form(provider.class).bindFromRequest();
        if(prov.hasErrors() || prov.hasGlobalErrors()){
            return ok(views.html.createProvider.render(prov,login));
        }
        Map<String, String> rawdata = prov.rawData();
        provider provid = new provider();

        provid.setId_provider((id+1));
        provid.setName(rawdata.get("name"));
        provid.setPhone(Long.valueOf(rawdata.get("phone")));
        provid.setAddress(rawdata.get("address"));

        List<provider> winne = Ebean.find(provider.class).where().eq("id_provider", provid.getId_provider()).findList();
        if(winne.isEmpty()){
            try{
                Ebean.save(provid);
            }catch (Exception ex){

                System.out.println("ГОАНООООЭ");

                return redirect(routes.ProviderController.renderAddProvider(login));
            }

            return redirect(routes.ProviderController.providerPage(login,true));

        }

        return redirect(routes.ProviderController.renderAddProvider(login));
    }
    public Result renderUpdateProviderInfo(Integer id,String login){
        provider win = provider.find.byId(id);
        UpdateProvid update = new UpdateProvid(win.getName(), win.getPhone(),win.getAddress());

        Form<UpdateProvid> updateForm = formFactory.form(UpdateProvid.class).fill(update);
        return ok(views.html.updateProvid.render(updateForm, win,login));

    }
    public Result updateProviderInfo(Integer id,String login){
        provider Win = provider.find.byId(id);
        Form<UpdateProvid> form = formFactory.form(UpdateProvid.class).bindFromRequest();
        if(form.hasErrors() || form.hasGlobalErrors()){
            return ok(views.html.updateProvid.render(form, Win,login));
        }
        Map<String, String> rawdata = form.rawData();

        Win.setName(rawdata.get("name"));
        Win.setPhone(Long.valueOf(rawdata.get("phone")));
        Win.setAddress(rawdata.get("address"));
        Ebean.update(Win);

        return redirect(routes.ProviderController.providerPage(login,true));
    }

}

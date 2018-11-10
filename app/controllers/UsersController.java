package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.LoginForm;
import models.User;
import models.UpdateForm;
import org.apache.commons.codec.digest.DigestUtils;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.JavaConverters;

import javax.inject.Inject;
import java.util.*;

import static scala.collection.JavaConverters.asScalaBuffer;

public class UsersController extends Controller {
    @Inject
    FormFactory formFactory;
    protected final byte NO_ERROR = 0;
    protected final byte ERROR_LOGIN_OR_PASSWORD = 1;

    public Result usersList(String login){
        List<User> users = User.find.all();
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1.getId() <= o2.getId())
                    return -1;
                return 1;
            }
        });
        List<String> nameColomn = new ArrayList<>();
        User us = new User();
        nameColomn = us.getNameColomn();

        return ok(views.html.users.render(JavaConverters.asScalaBuffer(nameColomn)
                  ,asScalaBuffer(users),login));
    }


    public Result deleteUser(Integer id,String login){
        User.find.deleteById(id);
        return redirect(routes.UsersController.usersList(login));
    }


    public Result renderAddUserForm(boolean admin,int error){
        String login = AuxiliaryController.getSessionLogin();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        Form<User> form2 = formFactory.form(User.class);

        return ok(views.html.createUser.render(form2,true,(byte)error,login));
    }

    public Result addingUser(boolean admin){
        int id=0;
        String login = AuxiliaryController.getSessionLogin();

        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        Form<LoginForm> form = formFactory.form(LoginForm.class);
        SqlQuery maxId = Ebean.createSqlQuery("select max(id) from public.user"); // для вывода данных таблиц
        List<SqlRow> mId = maxId.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
               id = Integer.parseInt(row2.getString(s));

            }
        }
        if(userForm.hasErrors() || userForm.hasGlobalErrors()){
            if (admin){
                return redirect(routes.UsersController.renderAddUserForm(admin,(int)ERROR_LOGIN_OR_PASSWORD));
            }
            return ok(views.html.indexProjectPage.render("",admin,form,userForm,ERROR_LOGIN_OR_PASSWORD));
        }
        Map<String, String> rawdata = userForm.rawData();

        Boolean isAdmin = Boolean.valueOf(String.valueOf(rawdata.get("isAdmin")));
        User user = new User();
        user.setId((id+1));
        user.setLogin(rawdata.get("login"));
        String passwordHex = DigestUtils.md5Hex(rawdata.get("password")).toUpperCase();
        user.setPassword(passwordHex);
        user.setAdmin(isAdmin);

        List<User> users = Ebean.find(User.class).where().eq("login", user.getLogin()).findList();

        if(users.isEmpty()){
            try{
                Ebean.save(user);
            }catch (Exception ex){
                return redirect(routes.UsersController.renderAddUserForm(admin,(int)NO_ERROR));
            }
            if(admin){
                return redirect(routes.UsersController.usersList(login));
            }
           else{
             return redirect(routes.AuxiliaryController.ifGuest());
           }
        }
        return redirect(routes.UsersController.renderAddUserForm(admin,(int)NO_ERROR));
    }
    public Result renderUpdateUserInfo(Integer id,String login){
        User user = User.find.byId(id);
        UpdateForm update = new UpdateForm(user.getPassword(), user.getAdmin());
        Form<UpdateForm> updateForm = formFactory.form(UpdateForm.class).fill(update);
        return ok(views.html.updateUser.render(updateForm, user,login));

    }

    public Result updateUserInfo(Integer id,String login){
        User user = User.find.byId(id);
        Form<UpdateForm> form = formFactory.form(UpdateForm.class).bindFromRequest();
        if(form.hasErrors() || form.hasGlobalErrors()){
            return ok();
        }
        Map<String, String> rawdata = form.rawData();
        Boolean isAdmin = Boolean.valueOf((rawdata.get("isAdmin")));
        user.setPassword(rawdata.get("password"));
        user.setAdmin(isAdmin);
        Ebean.update(user);
        return redirect(routes.UsersController.usersList(login));
    }
}


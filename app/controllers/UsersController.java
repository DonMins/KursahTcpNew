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
    protected final int NO_ERROR = 0;
    protected final int ERROR_LOGIN_OR_PASSWORD = 1;
    protected final int ERROR_LOGIN_IS_EXIST = 2;

    public Result usersList(){
        String login = AuxiliaryController.getSessionLogin();
        List<User> users = User.find.all();
        Form<User> userForm = formFactory.form(User.class);
        users.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                if(o1.getId() <= o2.getId())
                    return -1;
                return 1;
            }
        });
        List<String> nameColomn = new ArrayList<>();
        User tmp = new User();
        nameColomn = tmp.getNameColomn();
        return ok(views.html.users.render(JavaConverters.asScalaBuffer(nameColomn)
                  ,asScalaBuffer(users),login,NO_ERROR,userForm,true));
    }

    public Result deleteUser(Integer id){
        String login = AuxiliaryController.getSessionLogin();
        User.find.deleteById(id);
        return redirect(routes.UsersController.usersList());
    }

    public Result renderAddUserForm(int error){
        String login = AuxiliaryController.getSessionLogin();
        Form<User> userForm = formFactory.form(User.class);
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
        User tmp = new User();
        nameColomn = tmp.getNameColomn();

        return ok(views.html.users.render(JavaConverters.asScalaBuffer(nameColomn)
                ,asScalaBuffer(users),login,error,userForm,true));
    }

    public Result addingUser(){
        int id=0;
        String login = AuxiliaryController.getSessionLogin();
        Boolean admin = AuxiliaryController.getSessionAdmin();
        Form<User> userForm = formFactory.form(User.class).bindFromRequest();
        Form<LoginForm> loginForm = formFactory.form(LoginForm.class);
        SqlQuery maxId = Ebean.createSqlQuery("select max(id) from public.user"); // для вывода данных таблиц
        List<SqlRow> mId = maxId.findList();
        for (SqlRow row : mId) {
            Set<String> keyset = row.keySet();
            for (String s : keyset) {
               id = Integer.parseInt(row.getString(s));

            }
        }
        if(userForm.hasErrors() || userForm.hasGlobalErrors()){
            if (admin){
                return redirect(routes.UsersController.renderAddUserForm(ERROR_LOGIN_IS_EXIST));
            }
            return ok(views.html.indexProjectPage.render("",admin,loginForm,userForm,ERROR_LOGIN_IS_EXIST));
        }
        Map<String, String> rawdata = userForm.rawData();
        Boolean isAdmin = Boolean.valueOf(String.valueOf(rawdata.get("isAdmin")));
        User user = new User();
        user.setId((id+1));
        user.setLogin(rawdata.get("login"));
        String passwordHex = DigestUtils.md5Hex(rawdata.get("password")).toUpperCase();
        user.setPassword(passwordHex);
        user.setAdmin(isAdmin);
        List<User> userList = Ebean.find(User.class).where().eq("login", user.getLogin()).findList();
        if(userList.isEmpty()){
            try{
                Ebean.save(user);
            }catch (Exception ex){
                return redirect(routes.UsersController.renderAddUserForm(NO_ERROR));
            }
            if(admin){
                return redirect(routes.UsersController.usersList());
            }
           else{
             return redirect(routes.AuxiliaryController.ifGuest());
           }
        }
        return redirect(routes.UsersController.renderAddUserForm((int)NO_ERROR));
    }
    public Result renderUpdateUserInfo(Integer id){
        String login = AuxiliaryController.getSessionLogin();
        User user = User.find.byId(id);
        UpdateForm update = new UpdateForm(user.getPassword(), user.getAdmin());
        Form<UpdateForm> updateForm = formFactory.form(UpdateForm.class).fill(update);
        return ok(views.html.updateUser.render(updateForm, user,login));
    }

    public Result updateUserInfo(Integer id){

        User user = User.find.byId(id);
        Form<UpdateForm> updateForm = formFactory.form(UpdateForm.class).bindFromRequest();
        if(updateForm.hasErrors() || updateForm.hasGlobalErrors()){
            return ok();
        }
        Map<String, String> rawdata = updateForm.rawData();
        Boolean isAdmin = Boolean.valueOf((rawdata.get("isAdmin")));
        user.setPassword(rawdata.get("password"));
        user.setAdmin(isAdmin);
        Ebean.update(user);
        return redirect(routes.UsersController.usersList());

    }
}




package controllers;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import models.LoginForm;
import models.User;
import models.UpdateForm;
import play.Logger;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import scala.collection.JavaConverters;

import javax.inject.Inject;
import java.util.*;

import static scala.collection.JavaConverters.asScalaBuffer;


/**
 * UserController is responsible for every action with user administration.
 * It shows users list, render all adding, updating and registration forms and handling all get/post requests from them.
 */
public class UsersController extends Controller {
    private static Logger.ALogger logger = Logger.of(UsersController.class);


    /**
     * Render table with all registered users.
     * @return Response with params to be displayed in table:
     * list of users and users fields names
     */
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
        ArrayList<String> nameColomn = new ArrayList<>();
        User us = new User();
        nameColomn = us.getNameColomn();

          return ok(views.html.users.render(JavaConverters.asScalaBuffer(nameColomn)
                  ,asScalaBuffer(users),login));
    }

    /**
     *
     * @param id identificator of user to delete
     * @return Response with updated list of users
     */

    public Result deleteUser(Integer id,String login){
        User.find.deleteById(id);
        return redirect(routes.UsersController.usersList(login));
    }

    @Inject
    FormFactory formFactory;



    /**
     * Render registration page
     * @return Response with registration form to registration page
     */
    public Result renderAddUserForm(boolean admin){
        Form<LoginForm> form = formFactory.form(LoginForm.class);

        Form<User> form2 = formFactory.form(User.class);

        return ok(views.html.indexProjectPage.render("",admin,form,form2));
    }

    /**
     * Render adding new user page for admin
     * @return Response with adding user form
     */

    public Result renderAdminForm(){

        Form<User> form = formFactory.form(User.class);

        return ok();
    }

    /**
     * Handle post request with form in it, parse it, and trying to add new user to database.
     * After it redirects to login page or stays on current page if there was some errors.
     * @return Redirects to other pages by calling theirs controllers.
     */
    public Result addingUser(boolean admin){
        logger.info("Регистрация регистрации нового пользователя");
        Integer id=0;
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
        System.out.print("EEEEEEEE"+id);
        if(userForm.hasErrors() || userForm.hasGlobalErrors()){
            return ok(views.html.indexProjectPage.render("",admin,form,userForm));
        }
        Map<String, String> rawdata = userForm.rawData();

        Boolean isAdmin = Boolean.valueOf(String.valueOf(rawdata.get("isAdmin")));
        User user = new User();
        //User user = userForm.get();
        user.setId((id+1));
        user.setLogin(rawdata.get("login"));
        user.setPassword(rawdata.get("password"));
        user.setAdmin(isAdmin);
        List<User> users = Ebean.find(User.class).where().eq("login", user.getLogin()).findList();
        if(users.isEmpty()){
            try{
                Ebean.save(user);
            }catch (Exception ex){
                logger.info("При регистрации пользователя произошла ошибка");
                System.out.println("Something went wrong");
                return redirect(routes.UsersController.renderAddUserForm(admin));
            }
            if(admin){
                return redirect(routes.UsersController.usersList(user.getLogin()));
            }
           else{
            return redirect(routes.mainPageController.test());
           }

        }
        logger.info("Произошла какая-то неведома ошибка");
        return redirect(routes.UsersController.renderAddUserForm(admin));
    }
    public Result renderUpdateUserInfo(Integer id,String login){
        User user = User.find.byId(id);

        UpdateForm update = new UpdateForm(user.getPassword(), user.getAdmin());
        Form<UpdateForm> updateForm = formFactory.form(UpdateForm.class).fill(update);
        return ok(views.html.updateUser.render(updateForm, user,login));

    }


    /**
     * Handle form with data, parse it and try to update user`s info in database for admin.
     * @param id identificator of user to be updated
     * @return redirects to o userslist page if its ok and stays on current page if there were some errors
     */

    public Result updateUserInfo(Integer id,String login){
        User user = User.find.byId(id);
        Form<UpdateForm> form = formFactory.form(UpdateForm.class).bindFromRequest();
        if(form.hasErrors() || form.hasGlobalErrors()){
            logger.info("Пользователь: " + session().get("username") + " при обновлении информации о " + user.getLogin() + " одно или несколько полей остались пустыми");
            return ok();
        }
        Map<String, String> rawdata = form.rawData();

        Boolean isAdmin = Boolean.valueOf((rawdata.get("isAdmin")));
        user.setPassword(rawdata.get("password"));

        user.setAdmin(isAdmin);
        System.out.println("some errors " + isAdmin);
        Ebean.update(user);
        logger.info("Пользователь: " + session().get("username") + " данные о пользователе " + user.getLogin() + " успешно обновлены");
        return redirect(routes.UsersController.usersList(login));
    }
}


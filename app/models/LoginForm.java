package models;

import io.ebean.Ebean;
import models.User;
import play.Logger;
import play.data.validation.Constraints;

import java.util.List;

@Constraints.Validate
/**
 * The login and password class for the authorization page
 */
public class LoginForm implements Constraints.Validatable<String> {

    @Constraints.Required
    private String login;
    @Constraints.Required
    private String password;

    @Override
    public String toString() {
        return "LoginForm{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public LoginForm(){}

    public LoginForm(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {

        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    @Override
    public String validate() {
        System.out.println("form validate");
        if(login == null || password == null){
            return null;
        }
        List<User> users = Ebean.find(User.class).where().eq("login", login).findList();

        if(users.isEmpty()){
            System.out.println("Неправильный логин или пароль");
            return "Неправильный логин или пароль";

        }
        User user = users.get(0);
        System.out.println(user.getLogin());
        if( !user.getPassword().equals(password)){

            return "Неправильный логин или пароль";
        }
        return null;
    }
}

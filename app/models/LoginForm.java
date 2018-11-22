package models;
import io.ebean.Ebean;
import org.apache.commons.codec.digest.DigestUtils;
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

        if(login == null || password == null){
            return null;
        }
        List<User> users = Ebean.find(User.class).where().eq("login", login).findList();

        if(users.isEmpty()){

            return "Неправильный логин или пароль";
        }
        User user = users.get(0);
        String passwordHex = DigestUtils.md5Hex(password).toUpperCase();

        if( !user.getPassword().equals(passwordHex)){
            return "Неправильный логин или пароль";
        }
        return null;
    }
}

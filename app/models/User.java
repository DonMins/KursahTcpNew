package models;


import io.ebean.Ebean;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Constraints.Validate
@Entity
@Table(name = "user", schema = "public")
/**
 * Class c user parameters (login, password id, admin rights)
 */
public class User implements Constraints.Validatable<String> {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "public.user_id_seq")
    private Integer id;
    @Constraints.Required
    private String login;
    @Constraints.Required
    private String password;
    @Column(name = "isadmin")
    private boolean isAdmin;

    public User(){}

    public User(String login, String password, Boolean isAdmin,Integer id){
        this.login = login;
        this.password = password;
        this.isAdmin = isAdmin;
        this.id = id;
    }

    public List<String> getNameColomn(){
        List<String > nameColomn = new ArrayList<>();
        nameColomn.add("Id");
        nameColomn.add("Логин");
        nameColomn.add("Пароль");
        nameColomn.add("Учетная запись админа");
        return nameColomn;
    }

    public static Finder<Integer, User> find = new Finder<>(User.class);

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

   public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String validate() {
        System.out.println("user validate");
        List<User> users = Ebean.find(User.class).where().eq("login", login).findList();
        if(users.isEmpty()){
            return null;
        }
        return "Пользователь с таким логином уже существует";
    }
}

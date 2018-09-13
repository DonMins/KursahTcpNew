package models;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Constraints.Validate
@Entity
@Table(name = "rating", schema = "public")

public class rating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.rating_id_user_id_seq")
    private Integer id_user ;


    //        @Constraints.Required
//    private String name;
    @Constraints.Required
    private Integer id_product;
    @Constraints.Required
    private Integer rating;

    public ArrayList<String> getNameColomn(){
        ArrayList<String > nameColomn = new ArrayList<>();
        nameColomn.add("Продукт");
        nameColomn.add("Пользователь");
        nameColomn.add("Оценка");

        return nameColomn;
    }

    public static Finder<Integer, rating> find = new Finder<>(rating.class);
    public rating(){}

    public rating(@Constraints.Required Integer id_product,
                    @Constraints.Required Integer id_user, @Constraints.Required Integer rating) {
        this.id_product = id_product;
        this.id_user = id_user;
        this.rating = rating;
    }

    public Integer getId_product() {
        return id_product;
    }

    public void setId_product(Integer id_product) {
        this.id_product = id_product;
    }

    public Integer getId_user() {
        return id_user;
    }

    public void setId_user(Integer id_user) {
        this.id_user = id_user;
    }
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getWine(Integer id_product,String login) {
        String parametrs= null;
        String sql1 = "select wine.name from wine,public.rating, public.user where rating.id_user=id and login = '"+login+"' and rating.id_product=wine.id_product";
        SqlQuery maxId = Ebean.createSqlQuery(sql1);

        List<SqlRow> mId = maxId.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
                System.out.println("LOOOOK!"+parametrs);

            }
        }

        return parametrs;

    }


//    public String getUser(Integer id_user) {
//        String parametrs2= null;
//        String sql2="Select login from public.user, public.rating where id=" + id_user;
//        SqlQuery userId = Ebean.createSqlQuery(sql2);
//
//        List<SqlRow> mId = userId.findList();
//        for (SqlRow row2 : mId) {
//            Set<String> keyset2 = row2.keySet();
//            for (String s : keyset2) {
//                parametrs2 = row2.getString(s);
//
//            }
//        }
//        return parametrs2;
//
//    }

}

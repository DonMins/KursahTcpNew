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
@Table(name = "Rating", schema = "public")

public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.rating_id_user_id_seq")

    @Column(name = "id_user")
    private Integer idUser ;

    @Column(name = "id_product")
    private Integer idProduct;

    private Integer rating;

    public List<String> getNameColomn(){
        List<String > nameColomn = new ArrayList<>();
        nameColomn.add("Продукт");
        nameColomn.add("Оценка");
        nameColomn.add("Пользователь");
        return nameColomn;
    }

    public static Finder<Integer, Rating> find = new Finder<>(Rating.class);
    public Rating(){}

    public Rating(Integer idProduct, Integer idUser, Integer rating) {
        this.idProduct = idProduct;
        this.idUser = idUser;
        this.rating = rating;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }
    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getWine(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.name from public.wine, public.Rating where" +
                " wine.id_product=Rating.id_product and Rating.id_product ="+idProduct;
        SqlQuery maxId = Ebean.createSqlQuery(sql1);

        List<SqlRow> mId = maxId.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
            }
        }
        return parametrs;
    }

}

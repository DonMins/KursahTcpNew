package models;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;

@Constraints.Validate
@Entity
@Table(name = "basket", schema = "public")
public class basket {
   // @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "public.basket_id_product_seq")


    private Integer id_product;
    private String login;
    @Id
    private Integer id_basket;

    public Integer getId_basket() {
        return id_basket;
    }

    public void setId_basket(Integer id_basket) {
        this.id_basket = id_basket;
    }

    public  basket(){}

    public basket(Integer id_product, String login) {
        this.id_product = id_product;
        this.login = login;
    }

    public Integer getId_product() {
        return id_product;
    }

    public void setId_product(Integer id_product) {
        this.id_product = id_product;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public static Finder<Integer, basket> find = new Finder<>(basket.class);
}

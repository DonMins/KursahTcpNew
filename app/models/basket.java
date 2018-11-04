package models;

import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;

@Constraints.Validate
@Entity
@Table(name = "basket", schema = "public")
public class basket {
   // @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "public.basket_id_product_seq")

    @Column(name = "id_product")
    private Integer idProduct;
    private String login;
    @Id
    @Column(name = "id_basket")
    private Integer idBasket;
    private  boolean favorite;

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Integer getIdBasket() {
        return idBasket;
    }

    public  basket(){}

    public void setIdBasket(Integer idBasket) {
        this.idBasket = idBasket;
    }

    public basket(Integer idProduct, String login) {
        this.idProduct = idProduct;
        this.login = login;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
    public static Finder<Integer, basket> find = new Finder<>(basket.class);
}

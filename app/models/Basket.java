package models;

import io.ebean.Finder;
import play.data.validation.Constraints;
import javax.persistence.*;

@Entity
@Table(name = "basket", schema = "public")
public class Basket {
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

    public Basket(){}

    public void setIdBasket(Integer idBasket) {
        this.idBasket = idBasket;
    }

    public Basket(Integer idProduct, String login) {
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

    public static Finder<Integer, Basket> find = new Finder<>(Basket.class);
}

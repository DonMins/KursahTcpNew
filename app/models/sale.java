package models;

import play.data.validation.Constraints;

import javax.persistence.*;

@Constraints.Validate
@Entity
@Table(name = "sales", schema = "public")
public class sale {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.rating_id_user_id_seq")

    @Column(name = "id_sale")
    private Integer idSales;

    @Column(name = "id_product")
    private Integer idProduct;

    @Column(name = "text")
    private String text;

    public sale(){}

    public sale(Integer idSales,Integer idProduct,  String text) {
        this.idProduct = idProduct;
        this.idSales = idSales;
        this.text = text;
    }

    public int getIdSales() {
        return idSales;
    }

    public void setIdSales(Integer idSales) {
        this.idSales = idSales;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

package models;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Constraints.Validate
@Entity
@Table(name = "sales", schema = "public")
public class Sale implements Constraints.Validatable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "public.rating_id_user_id_seq")

    @Column(name = "id_sale")
    private Integer idSales;

    @Column(name = "id_product")
    private Integer idProduct;

    @Column(name = "text")
    private String text;

    public Sale(){}

    public Sale(Integer idSales, Integer idProduct, String text) {
        this.idProduct = idProduct;
        this.idSales = idSales;
        this.text = text;
    }

    public static Finder<Integer, Sale> find = new Finder<>(Sale.class);

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
    public String getWine(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.name from public.wine, public.rating where" +
                " wine.id_product=rating.id_product and rating.id_product ="+idProduct;
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
    public String getLinkForProduct(Integer idSale)
    {
        String link="/assets/images/sales/"+idSale+".png";
        return link;
    }

    @Override
    public String validate() {
        List<Sale> sale = Ebean.find(Sale.class).where().eq("id_sale", idSales).findList();
        if (sale.isEmpty()) {
            return null;
        }
        return "акция с таким id уже существует";
    }
}

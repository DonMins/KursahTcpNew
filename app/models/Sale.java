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
    @Column(name = "id_sale")
    private Integer idSales;
    private String  head;
    private String text;

    public Sale(){}

    public Sale(Integer idSales, String head, String text) {
        this.head = head;
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

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head= head;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
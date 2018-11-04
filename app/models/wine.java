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
@Table(name = "wine", schema = "public")

public class wine implements Constraints.Validatable<String> {
    //        @Id
//        @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "public.wine_id_seq")
    @Id
    @Column(name = "id_product")
    private Integer idProduct;

    //  @Constraints.Required
    private String name;
    //     @Constraints.Required
    private String colour;
    private String country;
    private String brand;
    @Column(name = "shelf_life")
    private String shelfLife;
    private String sugar;
    @Column(name = "grape_sort")
    private String grapeSort;
    // @Constraints.Required
    private Double price;
    //@Constraints.Required
    private Double value;
    //@Constraints.Required
    private Double degree;


    public wine() {
    }

    public wine(Integer idProduct, String name, String colour, String country, String brand,
                String shelfLife, String sugar, String grapeSort, Double price,
                Double value, Double degree) {
        this.idProduct = idProduct;
        this.name = name;
        this.colour = colour;
        this.brand = brand;
        this.shelfLife = shelfLife;
        this.sugar = sugar;
        this.grapeSort = grapeSort;
        this.price = price;
        this.value = value;
        this.degree = degree;

    }

    public List<String> getNameColomn() {
        List<String> nameColomn = new ArrayList<>();

        String q = "SELECT pgd.description " +
                "from pg_catalog.pg_statio_all_tables as st " +
                "inner join pg_catalog.pg_description pgd on (pgd.objoid=st.relid) " +
                "right outer join information_schema.columns c on (pgd.objsubid=c.ordinal_position and  c.table_schema=st.schemaname and c.table_name=st.relname) " +
                "where table_schema = 'public' and table_name = 'wine'";

        SqlQuery query3 = Ebean.createSqlQuery(q);// для вывода комментарией к колонкам таблицы
        List<SqlRow> rows3 = query3.findList();
        if (rows3.isEmpty()) {
            return null;
        }
        for (SqlRow row3 : rows3) {
            Set<String> keyset = row3.keySet();
            for (String s : keyset) {
                nameColomn.add(row3.getString(s));
            }
        }
        return nameColomn;
    }

    public static Finder<String, wine> find1 = new Finder<>(wine.class);

    public static Finder<Integer, wine> find = new Finder<>(wine.class);

    public Integer getIdProduct() {
        return idProduct;
    }
    public String getLinkForProduct(Integer idProduct)
    {
        String link="/assets/images/wines/"+idProduct+".png";
        return link;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getShelfLife() {
        return shelfLife;
    }

    public void setShelfLife(String shelfLife) {
        this.shelfLife = shelfLife;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getGrapeSort() {
        return grapeSort;
    }

    public void setGrapeSort(String grape_sort) {
        this.grapeSort = grapeSort;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getDegree() {
        return degree;
    }

    public void setDegree(Double degree) {
        this.degree = degree;
    }

    @Override
    public String validate() {

        List<wine> win = Ebean.find(wine.class).where().eq("id_product", idProduct).findList();
        if (win.isEmpty()) {
            return null;
        }
        return "товар с таким id уже существует";
    }

    public double averageRatingOfTheProduct(Integer id_product) {
        String parametrs = null;
        double average = 0; // а вообще результат сюда пишется

        String sql = "SELECT AVG(rating) FROM rating where id_product=" + id_product;
        SqlQuery averageID = Ebean.createSqlQuery(sql);

        List<SqlRow> mId = averageID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
            }
        }
        if (parametrs == null)
            return 0;
        else {

            average = Double.parseDouble(parametrs);// тут получаем рейтинг средний

          //  formatedAverage = String.format("%.2f", average); //округление которое предлагаю я
            // измени функцию чтобы возврщала Double а не String
        }
        return Math.rint(average*10)/10;// поправить тут
    }

    public String isVote(Integer idProduct,String login)
    {
        String parametrs = null;

        String sql ="SELECT login FROM rating,public.user where rating.id_product=" + idProduct+" and login='"+login+"'and id=rating.id_user";
        SqlQuery averageID = Ebean.createSqlQuery(sql);

        List<SqlRow> mId = averageID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
            }
        }
        if (parametrs == null)
            return login; // может голосовать
        else
            return "";// не может голосовать

    }
    public String isAddingToBasket(Integer idProduct,String login)
    {
        String parametrs = null;

        String sql ="SELECT login FROM basket where id_product=" + idProduct+" and login='"+login+"'";
        SqlQuery averageID = Ebean.createSqlQuery(sql);

        List<SqlRow> mId = averageID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
            }
        }
        if (parametrs == null)
            return login; // может голосовать
        else
            return "";// не может голосовать

    }
    public Integer getUsersMark(Integer idProduct,String login)
    {
        String parametrs = null;

        String sql ="SELECT rating FROM rating,public.user where rating.id_product=" + idProduct+" and login='"+login+"'and id=rating.id_user";
        SqlQuery averageID = Ebean.createSqlQuery(sql);

        List<SqlRow> mId = averageID.findList();
        for (SqlRow row2 : mId) {
            Set<String> keyset2 = row2.keySet();
            for (String s : keyset2) {
                parametrs = row2.getString(s);
            }
        }
        if (parametrs == null)
            return 0; // может голосовать
        else
            return Integer.parseInt(parametrs) ;// не может голосовать

    }



}



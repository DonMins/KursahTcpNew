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
        @Id
        @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "public.wine_id_seq")
        private Integer id_product;

      //  @Constraints.Required
      private String name;
   //     @Constraints.Required
        private String colour;
        private String country;
        private String brand;
        private String shelf_life;
        private String sugar;
        private String grape_sort;
       // @Constraints.Required
        private Double price;
        //@Constraints.Required
        private Double value;
        //@Constraints.Required
        private Double degree;


    public wine(){}

    public wine(Integer id_product,String name, String colour,String country,String brand,
                String shelf_life,String sugar,String grape_sort,Double price,
                Double value,Double degree){
           this.id_product = id_product
           ;
           this.name = name;
           this.colour=colour;
           this.brand = brand;
           this.shelf_life = shelf_life;
           this.sugar = sugar;
           this.grape_sort = grape_sort;
           this.price= price;
           this.value = value;
           this.degree = degree;

        }

        public ArrayList<String> getNameColomn() {
            ArrayList<String> nameColomn = new ArrayList<>();

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
                    System.out.println(row3.getString(s));
                    nameColomn.add(row3.getString(s));
                }
            }
            nameColomn.add("Рейтинг");
            return nameColomn;
        }

    public static Finder<String, wine> find1 = new Finder<>(wine.class);

    public static Finder<Integer, wine> find = new Finder<>(wine.class);
    public Integer getId_product() {
        return id_product;
    }

    public void setId_product(Integer id_product) {
        this.id_product = id_product;
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

    public String getShelf_life() {
        return shelf_life;
    }

    public void setShelf_life(String shelf_life) {
        this.shelf_life = shelf_life;
    }

    public String getSugar() {
        return sugar;
    }

    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    public String getGrape_sort() {
        return grape_sort;
    }

    public void setGrape_sort(String grape_sort) {
        this.grape_sort = grape_sort;
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
            System.out.println("wine validate");
            List<wine>win = Ebean.find(wine.class).where().eq("id_product", id_product).findList();
            if(win.isEmpty()){
                return null;
            }
            return "товар с таким id уже существует";
        }


    }



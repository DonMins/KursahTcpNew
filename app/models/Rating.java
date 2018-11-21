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
        //   nameColomn.add("Рейтинг");
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

    public String getWineName(Integer idProduct) {
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
    public String getWineColour (Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.colour from public.wine, public.Rating where" +
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
    public String getWineCountry (Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.country from public.wine, public.Rating where" +
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

    public String getWineBrand (Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.brand from public.wine, public.Rating where" +
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
    public String getWineShelfLife (Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.shelf_life from public.wine, public.Rating where" +
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
    public String getWineSugar(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.sugar from public.wine, public.Rating where" +
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
    public String getWineGrapeSort(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.grape_sort from public.wine, public.Rating where" +
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
    public String getWineValue(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.value from public.wine, public.Rating where" +
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
    public String getWineDegree(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.degree from public.wine, public.Rating where" +
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
    public String getWinePrice(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.price from public.wine, public.Rating where" +
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
    public String getWineAvg(Integer idProduct) {
        String parametrs= null;
        String sql1 = "Select wine.avgrating from public.wine, public.Rating where" +
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
    public String getLinkForProduct(Integer idProduct)
    {
        String link="/assets/images/wines/"+idProduct+".png";
        return link;
    }
    public String isAddingToBasket(Integer idProduct,String login)
    {
        String parametrs = null;

        String sql ="SELECT login FROM Basket where id_product=" + idProduct+" and login='"+login+"' and favorite =false";
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
    public String isAddingToFavorite(Integer idProduct,String login)
    {
        String parametrs = null;
        String sql ="SELECT login FROM Basket where id_product=" + idProduct+" and login='"+login+"' and favorite = true";
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

}

package models;

import io.ebean.Ebean;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "provider", schema = "public")
public class provider  {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator = "public.provider_id_provider_seq")
    private Integer id_provider;

    @Constraints.Required
    private String name;

    @Constraints.Required
    private Long phone;

    @Constraints.Required
    private String address;



    public ArrayList<String> getNameColomn(){
        ArrayList<String > nameColomn = new ArrayList<>();
        nameColomn.add("Название");
        nameColomn.add("Телефон");
        nameColomn.add("Адрес");

        return nameColomn;
    }

    public static Finder<Integer, provider> find = new Finder<>(provider.class);
    public provider(){}

    public provider(@Constraints.Required String name,
                    @Constraints.Required Long phone, @Constraints.Required String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
    }

    public Integer getId_provider() {
        return id_provider;
    }

    public void setId_provider(Integer id_provider) {
        this.id_provider = id_provider;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}

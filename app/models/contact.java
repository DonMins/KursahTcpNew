package models;

import io.ebean.Ebean;
import io.ebean.Finder;
import play.data.validation.Constraints;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;

@Constraints.Validate
@Entity
@Table(name = "contact", schema = "public")
public class Contact implements Constraints.Validatable<String>  {
    @Id
    @Column(name = "id_contact")
    private Integer idContact;
    @Column(name = "phone")
    private String phone;
    @Column(name = "adress")
    private String adress;
    @Column(name = "email")
    private String email;
    @Column(name = "workHours")
    private String workHours;

    public Contact(Integer idContact, String phone, String adress, String email, String workHours)
    {
        this.idContact=idContact;
        this.phone=phone;
        this.adress=adress;
        this.email=email;
        this.workHours=workHours;
    }
    public Contact(){}
    public static Finder<Integer, Contact> find = new Finder<>(Contact.class);

    public Integer getIdContact(){return idContact;}

    public void setIdContact(Integer idContact) {
        this.idContact = idContact;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWorkHours(String workHours) {
        this.workHours = workHours;
    }

    public String getPhone() {
        return phone;
    }

    public String getAdress() {
        return adress;
    }

    public String getEmail() {
        return email;
    }

    public String getWorkHours() {
        return workHours;
    }
    @Override
    public String validate() {
        List<Contact> sale = Ebean.find(Contact.class).where().eq("id_contact", idContact).findList();
        if (sale.isEmpty()) {
            return null;
        }
        return "акция с таким id уже существует";
    }
}


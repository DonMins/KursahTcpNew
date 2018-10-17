package models;

public class contact {
    private String contact_number;
    private String adress;
    private String email;
    private String workHours;

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
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

    public String getContact_number() {
        return contact_number;
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
}

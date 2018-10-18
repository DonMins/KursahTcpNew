package models;

public class contact {
    private String contactNumber;
    private String adress;
    private String email;
    private String workHours;

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
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
        return contactNumber;
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

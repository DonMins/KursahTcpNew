package models;

import play.data.validation.Constraints;

/**
 * Class for updating user data (password, region, administrator rights)
 */
public class UpdateForm {
    @Constraints.Required
    private String password;

    @Override
    public String toString() {
        return "UpdateForm{" +
                "password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                '}';
    }
    
    private boolean isAdmin;

    public UpdateForm(@Constraints.Required String password, @Constraints.Required Boolean isAdmin) {
        this.password = password;

        this.isAdmin = isAdmin;
    }

    public String getPassword() {

        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public UpdateForm(){}
}

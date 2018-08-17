package models;

import play.data.validation.Constraints;

public class UpdateProvid {

    @Constraints.Required
    private String name;

    @Constraints.Required
    private Long phone;

    @Constraints.Required
    private String address;

    public UpdateProvid(){}

    public UpdateProvid(@Constraints.Required String name,
                        @Constraints.Required Long phone, @Constraints.Required String address) {
        this.name = name;
        this.phone = phone;
        this.address = address;
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

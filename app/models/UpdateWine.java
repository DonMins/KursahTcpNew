package models;

import play.data.validation.Constraints;

import javax.persistence.Column;

public class UpdateWine {

    @Constraints.Required
    private String name;
    @Constraints.Required
    private String colour;
    private String country;
    private String brand;
    @Column(name = "shelf_life")
    private String shelfLife;
    private String sugar;
    @Column(name = "grape_sort")
    private String grapeSort;
  //  @Constraints.Required
    private Double price;
    //@Constraints.Required
    private Double value;
   // @Constraints.Required
    private Double degree;

    public UpdateWine() {
    }

    public UpdateWine(@Constraints.Required String name, @Constraints.Required String colour,
                      String country, String brand, String shelfLife, String sugar,
                      String grapeSort, Double price,
                       Double value, Double degree) {
        this.name = name;
        this.colour = colour;
        this.country = country;
        this.brand = brand;
        this.shelfLife = shelfLife;
        this.sugar = sugar;
        this.grapeSort = grapeSort;
        this.price = price;
        this.value = value;
        this.degree = degree;
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

    public void setGrapeSort(String grapeSort) {
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
}



package models;

import play.data.validation.Constraints;

public class UpdateWine {

    @Constraints.Required
    private String name;
    @Constraints.Required
    private String colour;
    private String country;
    private String brand;
    private String shelf_life;
    private String sugar;
    private String grape_sort;
  //  @Constraints.Required
    private Double price;
    //@Constraints.Required
    private Double value;
   // @Constraints.Required
    private Double degree;

    public UpdateWine() {
    }

    public UpdateWine(@Constraints.Required String name, @Constraints.Required String colour,
                      String country, String brand, String shelf_life, String sugar,
                      String grape_sort, Double price,
                       Double value, Double degree) {
        this.name = name;
        this.colour = colour;
        this.country = country;
        this.brand = brand;
        this.shelf_life = shelf_life;
        this.sugar = sugar;
        this.grape_sort = grape_sort;
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
}



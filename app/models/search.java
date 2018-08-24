package models;

public class search {
    private  Double minprice;
    private  Double maxprice;


    public search(){}

    public Double getMinprice() {
        return minprice;
    }
    public Double getMaxprice() {
        return maxprice;
    }

    public void setMinprice(Double minprice) {
        this.minprice = minprice;
    }
    public void setMaxprice(Double maxprice) {
        this.maxprice = maxprice;
    }
}

package models;

public class search {
    private  Double minprice;
    private  Double maxprice;
    private  boolean redColour = false;
    private  boolean whiteColour = false;
    private  boolean pinkColour = false;



    public search(){}

    public boolean isRedColour() {
        return redColour;
    }

    public void setRedColour(boolean redColour) {
        this.redColour = redColour;
    }

    public boolean isWhiteColour() {
        return whiteColour;
    }

    public void setWhiteColour(boolean whiteColour) {
        this.whiteColour = whiteColour;
    }

    public boolean isPinkColour() {
        return pinkColour;
    }

    public void setPinkColour(boolean pinkColour) {
        this.pinkColour = pinkColour;
    }

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

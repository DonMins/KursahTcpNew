package models;

public class search {
    private  Double minprice;
    private  Double maxprice;
    private  boolean redColour = false;
    private  boolean whiteColour = false;
    private  boolean pinkColour = false;
    private  boolean anyColour = false;

    public boolean isAnyColour() {
        return anyColour;
    }

    public void setAnyColour(boolean anyColour) {
        this.anyColour = anyColour;
    }

    public boolean isAnySugar() {
        return anySugar;
    }

    public void setAnySugar(boolean anySugar) {
        this.anySugar = anySugar;
    }

    private  boolean anySugar = false;

    private boolean semisweet = false; // полусладкое
    private boolean sweet = false;// сладкое
    private boolean semidry = false; // полусухое
    private boolean dry = false; // cухое

    public boolean isSemisweet() {
        return semisweet;
    }

    public void setSemisweet(boolean semisweet) {
        this.semisweet = semisweet;
    }

    public boolean isSweet() {
        return sweet;
    }

    public void setSweet(boolean sweet) {
        this.sweet = sweet;
    }

    public boolean isSemidry() {
        return semidry;
    }

    public void setSemidry(boolean semidry) {
        this.semidry = semidry;
    }

    public boolean isDry() {
        return dry;
    }

    public void setDry(boolean dry) {
        this.dry = dry;
    }

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

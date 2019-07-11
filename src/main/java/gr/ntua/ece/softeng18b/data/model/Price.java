package gr.ntua.ece.softeng18b.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Price{

    private final double price;
    private final String date;   // EEEE-MM-HH
    private  String productName;
    private  long productId;
    private  List<String> productTags = new ArrayList<>();
    private  long shopId;
    private  String shopName;
    private  List<String> shopTags = new ArrayList<>();
    private  String shopAddress;
    private  int shopDist;   // dist of shop from point of interest

    public Price(double price, String date){
        this.price = price;
        this.date  = date;
    }

    public double getPrice(){
        return price;
    }

    public String getDate(){
        return date;
    }

    public String getProductName(){
        return  productName;
    }

    public long getProductId(){
        return productId;
    }

    public List<String> getTags(){
        return productTags;
    }

    public long getShopId(){
        return shopId;
    }

    public String getShopName(){
        return shopName;
    }

    public List<String> getShopTags(){
        return shopTags;
    }

    public String getShopAddress(){
        return shopAddress;
    }

    public int getShopDist(){
        return shopDist;
    }

    //---------------------------------------------------------------

    public void setProductTags(List<String> tags){
        this.productTags = tags;
    }

    public void setShopTags(List<String> tags){
        this.shopTags = tags;
    }

    public void setProductName(String name){
        this.productName = name;
    }

    public void setProductId(long id){
        this.productId = id;
    }

    public void setShopId(long id){
        this.shopId = id ;
    }

    public void setShopName(String name){
        this.shopName = name;
    }

    public void setShopAddress(String addr){
        this.shopAddress = addr;
    }

    public void setShopDist(int dist){
        this.shopDist = dist;
    }

    //-------------------------------------------------------------------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price priceObj = (Price) o;
        return (date == priceObj.date && shopId == priceObj.shopId && productId == priceObj.productId );
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }

}

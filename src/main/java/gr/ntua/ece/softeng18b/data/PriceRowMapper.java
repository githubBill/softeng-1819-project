package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Price;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class PriceRowMapper implements RowMapper {

    @Override
    public Price mapRow(ResultSet rs, int rowNum) throws SQLException {

        double price       = rs.getDouble("price");
        String productName = rs.getString("productName");
        String shopName    = rs.getString("shopName");
        String date        = rs.getString("date");
        List<String> shopTags = new ArrayList<String>(Arrays.asList(rs.getString("shTags").split(" ")));
        List<String> productTags = new ArrayList<String>(Arrays.asList(rs.getString("prTags").split(" ")));
        long productId      = rs.getLong("productId");
        long shopId         = rs.getLong("shopId");
        String shopAddress  = rs.getString("address");
        int dist = 0;
        try{
          dist = rs.getInt("dist") ;
        }
        catch(Exception e){
          dist = 0;
        }
        Price tempPrice = new Price(price,date);
        tempPrice.setProductTags(productTags);
        tempPrice.setShopTags(shopTags);
        tempPrice.setProductName(productName);
        tempPrice.setProductId(productId);
        tempPrice.setShopId(shopId);
        tempPrice.setShopName(shopName);
        tempPrice.setShopAddress(shopAddress);
        tempPrice.setShopDist(dist);

        return  tempPrice;

    }

}

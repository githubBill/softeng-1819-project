package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.AbstractInterruptibleBatchPreparedStatementSetter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

//--------------------------------------------------------------
import java.lang.Math.*;
import java.util.Iterator ;
import org.restlet.resource.ResourceException;
import org.restlet.data.Status;
//-------------------------------------------------------------
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit ;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
//--------------------------------------------------------

public class DataAccess {

    private static final Object[] EMPTY_ARGS = new Object[0];

    private static final int MAX_TOTAL_CONNECTIONS = 16;
    private static final int MAX_IDLE_CONNECTIONS = 8;

    private JdbcTemplate jdbcTemplate;
    private DataSourceTransactionManager tm;

    public void setup(String driverClass, String url, String user, String pass) throws SQLException {

        //initialize the data source
        BasicDataSource bds = new BasicDataSource();
        bds.setDriverClassName(driverClass);
        bds.setUrl(url);
        bds.setMaxTotal(MAX_TOTAL_CONNECTIONS);
        bds.setMaxIdle(MAX_IDLE_CONNECTIONS);
        bds.setUsername(user);
        bds.setPassword(pass);
        bds.setValidationQuery("SELECT 1");
        bds.setTestOnBorrow(true);
        bds.setDefaultAutoCommit(true);

        //check that everything works OK
        bds.getConnection().close();

        //initialize the jdbc template utilitiy
        jdbcTemplate = new JdbcTemplate(bds);

        tm = new DataSourceTransactionManager(bds);
    }

    //-------------------------------- start of Product access management ------------------------------------

    public List<Product> getProducts(Limits limits) {
        //TODO: Support limits  ----------------------

        long tempStart = limits.getStart();
        int tempCount = limits.getCount();
        long argTotal = Math.abs(tempCount - tempStart);
        limits.setTotal(argTotal);

        //--------------------------------------------
        List<Product> products = jdbcTemplate.query("select * from Product order by productId", EMPTY_ARGS, new ProductRowMapper());
        for (Product p: products) {
            fetchTagsOfProduct(p);
        }

        /*Iterator<Product> i = products.iterator();
        int w = 1;
        while (i.hasNext()) {
            Object o = i.next();
            if(w > argTotal){
              i.remove();
            }
            w++;
      }*/
        return products;
  }

    public Product addProduct(String name, String description, String category, boolean withdrawn, String[] tags ) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(tm);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        long id = transactionTemplate.execute((TransactionStatus status) -> {

            //Create the new product record using a prepared statement
            GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
            int rowCount = jdbcTemplate.update((Connection con) -> {
                PreparedStatement ps = con.prepareStatement(
                    "insert into Product(productName, description, category, withdrawn) values(?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, name);
                ps.setString(2, description);
                ps.setString(3, category);
                ps.setBoolean(4, withdrawn);
                return ps;
            }, keyHolder);

            if (rowCount != 1) {
                throw new RuntimeException("New product not inserted");
            }

            long newId = keyHolder.getKey().longValue();

            if (tags != null && tags.length > 0) {
                jdbcTemplate.batchUpdate("insert into productTags values(?, ?)", new AbstractInterruptibleBatchPreparedStatementSetter(){
                    @Override
                    protected boolean setValuesIfAvailable(PreparedStatement ps, int i) throws SQLException {
                        if (i < tags.length) {
                            ps.setLong(1, newId);
                            ps.setString(2, tags[i]);
                            return true;
                        }
                        else {
                            return false;
                        }
                    }

                });
            }

            return newId;
        });

        //New row has been added
        Product product = new Product(
            id,
            name,
            description,
            category,
            withdrawn
        );
        if (tags != null && tags.length > 0) {
            product.setTags(Arrays.asList(tags));
        }

        return product;
    }

    public Optional<Product> getProduct(long id) {
        Long[] params = new Long[]{id};
        List<Product> products = jdbcTemplate.query("select * from Product where productId = ?", params, new ProductRowMapper());
        if (products.size() == 1)  {
            Product p = products.get(0);
            fetchTagsOfProduct(p);
            return Optional.of(p);
        }
        else {
            return Optional.empty();
        }
    }

    protected void fetchTagsOfProduct(Product p) {
        Long[] params = new Long[]{p.getId()};
        List<String> tags = jdbcTemplate.query("select tag from productTags where pid = ?", params, new RowMapper<String>() {
            @Override
            public String mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getString("tag");
            }
        });
        p.setTags(tags);
    }


    public Optional<Product> updateProduct(long pid, String name, String description, String category, boolean withdrawn, String[] tags ) {

          int rowCount = jdbcTemplate.update((Connection con) -> {
              PreparedStatement ps = con.prepareStatement(
                  "update product set productName = ? , description =  ? , category = ? , withdrawn = ? where productId = ?");
              ps.setString(1, name);
              ps.setString(2, description);
              ps.setString(3, category);
              ps.setBoolean(4, withdrawn);
              ps.setLong(5, pid);
              return ps;
          });

          if (rowCount != 1) {
              throw new RuntimeException("Product not updated");
          }

          if (tags != null && tags.length > 0) {

              int rowCount2 = jdbcTemplate.update((Connection con) -> {
                  PreparedStatement ps = con.prepareStatement(
                      "delete from productTags where pid=?");
                  ps.setLong(1, pid);
                  return ps;
              });

              jdbcTemplate.batchUpdate("insert into productTags values(?, ?)", new AbstractInterruptibleBatchPreparedStatementSetter(){
                  @Override
                  protected boolean setValuesIfAvailable(PreparedStatement ps, int i) throws SQLException {
                      if (i < tags.length) {
                          ps.setLong(1, pid);
                          ps.setString(2, tags[i]);
                          return true;
                      }
                      else {
                          return false;
                      }
                  }

              });
          }

      //New row has been updated
      Product product = new Product(
          pid,
          name,
          description,
          category,
          withdrawn
      );
      if (tags != null && tags.length > 0) {
          product.setTags(Arrays.asList(tags));
      }

      return Optional.of(product);

    }

    public void deleteProduct(long pid, boolean isAdmin){

      // delete product with productId=pid and also the tags with pid=pid
      // admin always has pid = 1
      // also prices are affected

      if( isAdmin ){

        int rowCount3 = jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                "delete from product where productId=?");
            ps.setLong(1, pid);
            return ps;
        });


        int rowCount2 = jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                "delete from productTags where pid=?");
            ps.setLong(1, pid);
            return ps;
        });

      }
      else{

        int rowCount = jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement(
                "update product set withdrawn = ? where productId = ?");
            ps.setBoolean(1, true);
            ps.setLong(2, pid);
            return ps;
        });

        if (rowCount != 1) {
            throw new RuntimeException("Product not updated");
        }

      }


    }


    //------------------- end of Product access management -----------------------------------------------------------------------------

    // ----------------- start or Shop access management ---------------------------------------------------------------------------------

    public List<Shop> getShops(Limits limits) {
       //TODO: Support limits  ----------------------

       long tempStart = limits.getStart();
       int tempCount = limits.getCount();
       long argTotal = Math.abs(tempCount - tempStart);
       limits.setTotal(argTotal);

       //--------------------------------------------


       List<Shop> shops = jdbcTemplate.query("select * from Shop order by shopId", EMPTY_ARGS, new ShopRowMapper());
       for (Shop p: shops) {
           fetchTagsOfShop(p);
       }

       /*Iterator<Shop> i = shops.iterator();
       int w = 1;
       while (i.hasNext()) {
           Object o = i.next();
           if(w > argTotal){
             i.remove();
           }
           w++;
     }*/
       return shops;
   }

   public Optional<Shop> getShop(long id) {
       Long[] params = new Long[]{id};
       List<Shop> shops = jdbcTemplate.query("select * from Shop where shopId = ?", params, new ShopRowMapper());
       if (shops.size() == 1)  {
           Shop p = shops.get(0);
           fetchTagsOfShop(p);
           return Optional.of(p);
       }
       else {
           return Optional.empty();
       }
   }

   protected void fetchTagsOfShop(Shop p) {
       Long[] params = new Long[]{p.getId()};
       List<String> tags = jdbcTemplate.query("select tag from shopTags where sid = ?", params, new RowMapper<String>() {
           @Override
           public String mapRow(ResultSet rs, int rowNum) throws SQLException {
               return rs.getString("tag");
           }
       });
       p.setTags(tags);
   }


   public Shop addShop(String name, String address, double lng, double lat, boolean withdrawn, String[] tags ) {

       TransactionTemplate transactionTemplate = new TransactionTemplate(tm);
       transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

       long id = transactionTemplate.execute((TransactionStatus status) -> {

           //Create the new shop record using a prepared statement
           GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
           int rowCount = jdbcTemplate.update((Connection con) -> {
               PreparedStatement ps = con.prepareStatement(
                   "insert into Shop(shopName, address, lng, lat, withdrawn) values(?, ?, ?, ?, ?)",
                   Statement.RETURN_GENERATED_KEYS
               );
               ps.setString(1, name);
               ps.setString(2, address);
               ps.setDouble(3, lng);
               ps.setDouble(4, lat);
               ps.setBoolean(5, withdrawn);
               return ps;
           }, keyHolder);

           if (rowCount != 1) {
               throw new RuntimeException("New shop not inserted");
           }

           long newId = keyHolder.getKey().longValue();

           if (tags != null && tags.length > 0) {
               jdbcTemplate.batchUpdate("insert into shopTags values(?, ?)", new AbstractInterruptibleBatchPreparedStatementSetter(){
                   @Override
                   protected boolean setValuesIfAvailable(PreparedStatement ps, int i) throws SQLException {
                       if (i < tags.length) {
                           ps.setLong(1, newId);
                           ps.setString(2, tags[i]);
                           return true;
                       }
                       else {
                           return false;
                       }
                   }

               });
           }

           return newId;
       });

       //New row has been added
       Shop shop = new Shop(
           id,
           name,
           address,
           lng,
           lat,
           withdrawn
       );
       if (tags != null && tags.length > 0) {
           shop.setTags(Arrays.asList(tags));
       }

       return shop;
   }


   public Optional<Shop> updateShop(long sid, String name, String address, double lng, double lat, boolean withdrawn, String[] tags ) {

         int rowCount = jdbcTemplate.update((Connection con) -> {
             PreparedStatement ps = con.prepareStatement(
                 "update shop set shopName = ? ,  address =  ? , lng =  ?, lat =  ?, withdrawn = ? where shopId = ?");
             ps.setString(1, name);
             ps.setString(2, address);
             ps.setDouble(3, lng);
             ps.setDouble(4, lat);
             ps.setBoolean(5, withdrawn);
             ps.setLong(6, sid);
             return ps;
         });

         if (rowCount != 1) {
             throw new RuntimeException("Shop not updated");
         }

         if (tags != null && tags.length > 0) {

             int rowCount2 = jdbcTemplate.update((Connection con) -> {
                 PreparedStatement ps = con.prepareStatement(
                     "delete from shopTags where sid=?");
                 ps.setLong(1, sid);
                 return ps;
             });

             jdbcTemplate.batchUpdate("insert into shopTags values(?, ?)", new AbstractInterruptibleBatchPreparedStatementSetter(){
                 @Override
                 protected boolean setValuesIfAvailable(PreparedStatement ps, int i) throws SQLException {
                     if (i < tags.length) {
                         ps.setLong(1, sid);
                         ps.setString(2, tags[i]);
                         return true;
                     }
                     else {
                         return false;
                     }
                 }

             });
         }


     //New row has been updated
     Shop shop = new Shop(
       sid,
       name,
       address,
       lng,
       lat,
       withdrawn
     );
     if (tags != null && tags.length > 0) {
         shop.setTags(Arrays.asList(tags));
     }

     return Optional.of(shop);

   }


   public void deleteShop(long pid, boolean isAdmin){

       // delete shop with shoptId=pid and also the tags with pid=pid
       // admin always has pid = 1
       // also prices are affected

       if( isAdmin ){

         int rowCount3 = jdbcTemplate.update((Connection con) -> {
             PreparedStatement ps = con.prepareStatement(
                 "delete from shop where shopId=?");
             ps.setLong(1, pid);
             return ps;
         });


         int rowCount2 = jdbcTemplate.update((Connection con) -> {
             PreparedStatement ps = con.prepareStatement(
                 "delete from shopTags where sid=?");
             ps.setLong(1, pid);
             return ps;
         });

       }
       else{

         int rowCount = jdbcTemplate.update((Connection con) -> {
             PreparedStatement ps = con.prepareStatement(
                 "update shop set withdrawn = ? where shopId = ?");
             ps.setBoolean(1, true);
             ps.setLong(2, pid);
             return ps;
         });

         if (rowCount != 1) {
             throw new RuntimeException("Shop not updated");
         }

       }


   }


    //-------------------- end of Shop access management ----------------------------------------------------------------------------------


    //----------------------  start of Price access management ----------------------------------------------------------------------------

    public List<Price> getPricesNoDist(Limits limits, String dateFrom, String dateTo, String[] sort,
  String[] shops, String[] products, String[] tags ) {
    //TODO: Support limits  ----------------------

    long tempStart = limits.getStart();
    int tempCount = limits.getCount();
    long argTotal = Math.abs(tempCount - tempStart);
    limits.setTotal(argTotal);

    //--------------------------------------------
    int howManySorts = sort.length ;
    List<Price> prices = null ;
    String sort1="";
    String sort2="";
    String sort3="";

    String strShops = String.join(",", shops);
    String strProducts = String.join(",", products);
    String strTags = String.join(",", tags);
    String mysqlQuery = "";
    List<Shop> sids=null;
    List<Product> pids=null;
    int size =0;

    //-------------------------------------------
    if( strShops.equals("") ) {
        mysqlQuery = "select * from shop";
        sids = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new ShopRowMapper());
        size = sids.size();
        strShops="1";
        for(int i=2; i<=size; i++){
            strShops = strShops+","+i;
        }

    }
    if( strProducts.equals("")) {
        mysqlQuery = "select * from product";
        pids = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new ProductRowMapper());
        size = pids.size();
        strProducts="1";
        for(int i=2; i<=size; i++){
            strProducts = strProducts+","+i;
        }
    }
    //--------------------------------------------
    if( strShops.length() == 1){ strShops = strShops+","+strShops ;}
    if( strProducts.length() == 1){ strProducts = strProducts+","+strProducts ;}
    //--------------------------------------------

    if(howManySorts == 3){

        sort1 = sort[0].replace("|", " ");
        sort2 = sort[1].replace("|", " ");
        sort3 = sort[2].replace("|", " ");
        mysqlQuery = "select price.price, product.productId, product.productName,  "
        + " ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) as prTags, "
        + " shop.shopID , shop.shopName, "
        + " ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) as shTags, "
        + " shop.address , price.date  from product , shop , price "
        + " where price.date >= '"+dateFrom+"' and price.date <= '"+dateTo
        + "'  and shop.shopId in ("+strShops+") and product.productId in ("+strProducts+") "
        + "   and price.shopId in ("+strShops+") and price.productId in ("+strProducts+") "
        + "  and shop.shopId=price.shopId and product.productId=price.productId "
        + " and ( ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) like '%"+strTags+"%' or ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) like '%"+strTags+"%' ) "
        + "   order by "+sort1+" ,"+sort2+" ,"+sort3 ;

    }
    else if( howManySorts == 2 ){

      sort1 = sort[0].replace("|", " ");
      sort2 = sort[1].replace("|", " ");
      mysqlQuery = "select price.price, product.productId, product.productName,  "
      + " ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) as prTags, "
      + " shop.shopID , shop.shopName, "
      + " ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) as shTags, "
      + " shop.address , price.date  from product , shop , price "
      + " where price.date >= '"+dateFrom+"' and price.date <= '"+dateTo
      + "'  and shop.shopId in ("+strShops+") and product.productId in ("+strProducts+") "
      + "   and price.shopId in ("+strShops+") and price.productId in ("+strProducts+") "
      + "  and shop.shopId=price.shopId and product.productId=price.productId "
      + " and ( ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) like '%"+strTags+"%' or ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) like '%"+strTags+"%' ) "
      + "   order by "+sort1+" ,"+sort2 ;

    }
    else{

      sort1 = sort[0].replace("|", " ");
      mysqlQuery = "select price.price, product.productId, product.productName,  "
      + " ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) as prTags, "
      + " shop.shopID , shop.shopName, "
      + " ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) as shTags, "
      + " shop.address , price.date  from product , shop , price "
      + " where price.date >= '"+dateFrom+"' and price.date <= '"+dateTo
      + "'  and shop.shopId in ("+strShops+") and product.productId in ("+strProducts+") "
      + "   and price.shopId in ("+strShops+") and price.productId in ("+strProducts+") "
      + "  and shop.shopId=price.shopId and product.productId=price.productId "
      + " and ( ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) like '%"+strTags+"%' or ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) like '%"+strTags+"%' ) "
      + "   order by "+sort1 ;

    }

    prices = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new PriceRowMapper());
    return prices;

}


    public List<Price> getPrices(Limits limits, int geoDist, double geoLat, double geoLng, String dateFrom, String dateTo, String[] sort,
      String[] shops, String[] products, String[] tags ) {
        //TODO: Support limits  ----------------------

        long tempStart = limits.getStart();
        int tempCount = limits.getCount();
        long argTotal = Math.abs(tempCount - tempStart);
        limits.setTotal(argTotal);

        //--------------------------------------------
        int howManySorts = sort.length ;
        List<Price> prices = null ;
        String sort1="";
        String sort2="";
        String sort3="";

        String strShops = String.join(",", shops);
        String strProducts = String.join(",", products);
        String strTags = String.join(",", tags);
        String mysqlQuery = "";
        List<Shop> sids=null;
        List<Product> pids=null;
        int size =0;

        //-------------------------------------------
        if( strShops.equals("") ) {
           mysqlQuery = "select * from shop";
           sids = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new ShopRowMapper());
           size = sids.size();
           strShops="1";
           for(int i=2; i<=size; i++){
               strShops = strShops+","+i;
           }

       }
       if( strProducts.equals("")) {
           mysqlQuery = "select * from product";
           sids = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new ProductRowMapper());
           size = pids.size();
           strProducts="1";
           for(int i=2; i<=size; i++){
               strProducts = strProducts+","+i;
           }
       }

        //--------------------------------------------
        if( strShops.length() == 1){ strShops = strShops+","+strShops ;}
        if( strProducts.length() == 1){ strProducts = strProducts+","+strProducts ;}
        //--------------------------------------------

        String geoLatQ="select lat from shop where shopid=shop.shopid";
        String geoLngQ="select lng from shop where shopid=shop.shopid";

        if(howManySorts == 3){

            sort1 = sort[0].replace("|", " ");
            sort2 = sort[1].replace("|", " ");
            sort3 = sort[2].replace("|", " ");
            mysqlQuery = "select price.price, product.productId, product.productName,  "
            + " ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) as prTags, "
            + " shop.shopID , shop.shopName, "
            + " ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) as shTags, "
            + " shop.address , price.date, "
            + "  ( select DISTANCEGPS( shop.lat, shop.lng, "+String.valueOf(geoLat)+", "+String.valueOf(geoLng)+", 'KM' ) div 1 ) as dist"
            + "  from product , shop , price "
            + "   where ( DISTANCEGPS( shop.lat, shop.lng, "+String.valueOf(geoLat)+", "+String.valueOf(geoLng)+", 'KM' ) div 1 )  < "+String.valueOf(geoDist)
            + "   and price.date >= '"+dateFrom+"' and price.date <= '"+dateTo
            + "'  and shop.shopId in ("+strShops+") and product.productId in ("+strProducts+") "
            + "   and price.shopId in ("+strShops+") and price.productId in ("+strProducts+") "
            + "  and shop.shopId=price.shopId and product.productId=price.productId "
            + " and ( ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) like '%"+strTags+"%' or ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) like '%"+strTags+"%' ) "
            + "   order by "+sort1+" ,"+sort2+" ,"+sort3 ;

        }
        else if( howManySorts == 2 ){

          sort1 = sort[0].replace("|", " ");
          sort2 = sort[1].replace("|", " ");
          mysqlQuery = "select price.price, product.productId, product.productName,  "
          + " ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) as prTags, "
          + " shop.shopID , shop.shopName, "
          + " ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) as shTags, "
          + " shop.address , price.date, "
          + "  ( select DISTANCEGPS( shop.lat, shop.lng, "+String.valueOf(geoLat)+", "+String.valueOf(geoLng)+", 'KM' ) div 1 ) as dist"
          + "  from product , shop , price "
          + "   where ( DISTANCEGPS( shop.lat, shop.lng, "+String.valueOf(geoLat)+", "+String.valueOf(geoLng)+", 'KM' ) div 1 )  < "+String.valueOf(geoDist)
          + "   and price.date >= '"+dateFrom+"' and price.date <= '"+dateTo
          + "'  and shop.shopId in ("+strShops+") and product.productId in ("+strProducts+") "
          + "   and price.shopId in ("+strShops+") and price.productId in ("+strProducts+") "
          + "  and shop.shopId=price.shopId and product.productId=price.productId "
          + " and ( ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) like '%"+strTags+"%' or ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) like '%"+strTags+"%' ) "
          + "   order by "+sort1+" ,"+sort2 ;

        }
        else{

          sort1 = sort[0].replace("|", " ");
          mysqlQuery = "select price.price, product.productId, product.productName,  "
          + " ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) as prTags, "
          + " shop.shopID , shop.shopName, "
          + " ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) as shTags, "
          + " shop.address , price.date, "
          + " ( select DISTANCEGPS( shop.lat, shop.lng, "+String.valueOf(geoLat)+", "+String.valueOf(geoLng)+", 'KM' ) div 1 ) as dist"
          + "  from product , shop , price "
          + "   where DISTANCEGPS( shop.lat, shop.lng, "+String.valueOf(geoLat)+", "+String.valueOf(geoLng)+", 'KM' ) div 1   < "+String.valueOf(geoDist)
          + "   and price.date >= '"+dateFrom+"' and price.date <= '"+dateTo
          + "'  and shop.shopId in ("+strShops+") and product.productId in ("+strProducts+") "
          + "   and price.shopId in ("+strShops+") and price.productId in ("+strProducts+") "
          + "  and shop.shopId=price.shopId and product.productId=price.productId "
          + " and ( ( select group_concat(concat( tag ) separator ' ' ) from producttags where pid = product.productId ) like '%"+strTags+"%' or ( select group_concat(concat( tag ) separator ' ' ) from shoptags where sid = shop.shopId ) like '%"+strTags+"%' ) "
          + "   order by "+sort1 ;


        }

        prices = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new PriceRowMapper());
        return prices;

    }


    //--------------------------------------------------------------------------------------------------------

    public List<Price> addPrice(double price, LocalDate dateFrom, LocalDate dateTo, long productId, long shopId ) {

        TransactionTemplate transactionTemplate = new TransactionTemplate(tm);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        final long productId2 = productId;
        final long shopId2 = shopId;
        //String productName = "";
        //String shopName = "";
        //String shopAddress = "";
        String sql;

       // try {
            sql = "SELECT productName FROM product WHERE productId ="+productId;
            final String productName= jdbcTemplate.queryForObject(sql, String.class);

            sql = "SELECT shopName FROM shop WHERE shopId ="+shopId;
            final String shopName= jdbcTemplate.queryForObject(sql, String.class);

            sql = "SELECT address FROM shop WHERE shopId ="+shopId;
            final String shopAddress= jdbcTemplate.queryForObject(sql, String.class);
      // }
       //catch (Exception e) {
          //// TODO:
       //}

       //Long params = new Long(shopId);
       List<String> shopTags = jdbcTemplate.query("select tag from shopTags where sid ="+shopId, EMPTY_ARGS, new RowMapper<String>() {
           @Override
           public String mapRow(ResultSet rs, int rowNum) throws SQLException {
               return rs.getString("tag");
           }
       });

       //Long params = new Long(productId);
       List<String> productTags = jdbcTemplate.query("select tag from productTags where pid ="+productId, EMPTY_ARGS, new RowMapper<String>() {
           @Override
           public String mapRow(ResultSet rs, int rowNum) throws SQLException {
               return rs.getString("tag");
           }
       });



        Period diff = Period.between(dateFrom, dateTo);
        final int days = diff.getDays();
        final LocalDate dateFrom1 = dateFrom;
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        //String curDateStr = "";
       // LocalDate curDate = null;
        //sql = "insert into price (price, date, productId, shopId) "
        //+ "VALUES (?, ?, ?, ?);";


        List<Price> prices = new ArrayList<Price>();
        //Connection connection = getConnection();

        int rowCount = jdbcTemplate.update((Connection con) -> {
            PreparedStatement ps = con.prepareStatement("insert ignore into price (price, date, productId, shopId) "
            + "VALUES (?, ?, ?, ?);");
            for(int i = 0; i <= days; i++){
                LocalDate curDate1;
                String curDateStr;
                curDate1 = dateFrom1.plusDays(i);
                curDateStr = curDate1.format(formatter);
                ps.setDouble(1, price);
                ps.setString(2, curDateStr);
                ps.setLong(3, productId);
                ps.setLong(4, shopId);
                ps.addBatch();

                Price priceObj = new Price(
                price,
                curDateStr
                );

                priceObj.setShopTags(shopTags);
                priceObj.setProductTags(productTags);
                priceObj.setShopAddress(shopAddress);
                priceObj.setShopDist(0);
                priceObj.setShopName(shopName);
                priceObj.setProductName(productName);
                priceObj.setProductId(productId2);
                priceObj.setShopId(shopId2);


                prices.add(priceObj);
            }
            ps.executeBatch();
            return ps;

        });

        /*if (rowCount != days) {
            throw new RuntimeException("Price not updated");
        }*/

        //New row has been added


        return prices;
    }



    //----------------------  end of  Price access management --------------------------------------------------------------------------------



    //-------------------- Customer (credentials and checks ) management -----------------------------------------------------------------------

    //credentials check and signup user

    public boolean validateUser(String username, String password){
        String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
        String params[] = {username, sha256hex};
        List<Customer> clist = jdbcTemplate.query("select * from Customer where username=? and hashPassword=?",params, new CustomerRowMapper());
        if(clist.size() ==  0){
            // user not found or wrong credentials
            return false;
        }
        else{
            return true;  // user found
        }

    }


    public boolean signUp(String username, String password, String email, String firstName, String lastName){

      String sha256hex = org.apache.commons.codec.digest.DigestUtils.sha256Hex(password);
      String params[] = {username};
      List<Customer> clist = jdbcTemplate.query("select * from Customer where username=?",params, new CustomerRowMapper());
      if(clist.size() ==  1){
          // user not found or wrong credentials
          return false;
      }
      else{

          // check if valid format
          Integer testInt = 0;
          boolean isInt=true;
          try {
              testInt  = Integer.parseInt(username);
          }
          catch(Exception e) {
              isInt = false;
              // could not be parsed to Integer so its a valid string
          }
          if(isInt){
            // isInt is true  => bad input parameters
            return false;
          }

          //String mysqlQuery  = "insert into customer(firstName, lastName, email, username, hashPassword)"
          //+ " values ("+firstName+", "+lastName+", "+email+", "+username+", "+sha256hex+")" ;
          //List<Customer> customers = null ;
          //customers = jdbcTemplate.query(mysqlQuery, EMPTY_ARGS, new CustomerRowMapper());

          int rowCount = jdbcTemplate.update((Connection con) -> {
              PreparedStatement ps = con.prepareStatement(
                  "insert into customer(firstName, lastName, email, username, hashPassword) values(?, ?, ?, ?, ?)");
              ps.setString(1, firstName);
              ps.setString(2, lastName);
              ps.setString(3, email);
              ps.setString(4, username);
              ps.setString(5, sha256hex);
              return ps;
          });

          if (rowCount != 1) {
              throw new RuntimeException("Customer not updated");
          }

      }

      return true;

    }

    //--------------------- end of Customer management ------------------------------------------------------------------------------------------


}

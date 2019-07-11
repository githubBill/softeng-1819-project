package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.*;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Iterator ;
//------------------------------------------------------------
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//-------------------------------------------------------------
import org.restlet.data.Parameter;
import org.restlet.data.Form;
import org.restlet.util.Series;
import org.restlet.data.Header;

//---------------------------------------------------------------

public class PricesResource extends ServerResource {


    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();


    @Override
    public Representation get() throws ResourceException {

        Map<String, Object> map = new HashMap<>();
        Form f1 = getRequest().getResourceRef().getQueryAsForm();

        // support the following parameters in query url: start, count , geoDist, geoLng, geoLat
        // dateFrom , dateTo ( in form of EEEE-MM-HH)
        // shops (a list with the ids of shops)
        // products ( a list with the ids of products)
        // tags (  a list of tags? not for process though)

        boolean startFlag,countFlag,sortFlag,formatFlag,geoDistFlag;
        boolean geoLatFlag,geoLngFlag,dateFromFlag,dateToFlag,shopsFlag,productsFlag,tagsFlag;
        startFlag = false ; countFlag = false; formatFlag=false; geoDistFlag=false; geoLatFlag=false;
        geoLngFlag = false; sortFlag = false; dateToFlag = false; dateFromFlag = false; shopsFlag = false;
        productsFlag = false; tagsFlag = false;
        int localStart =0;
        int localCount =0;
        int localgeoDist = 0; // max dist in kilometers
        double localgeoLng = 0.0 ;
        double localgeoLat = 0.0;
        String localdateFrom = "";
        String localdateTo = "";
        List<String> localSort = new ArrayList<>();
        List<String> localShops = new ArrayList<>();
        List<String> localProducts = new ArrayList<>();
        List<String> localTags = new ArrayList<>();
        int paramDistCounter = 0;
        boolean queryWithDist = true;
        int paramDateCounter = 0;
        boolean queryWith2Dates = true;

        for(Parameter parameter : f1){

          if(parameter.getName().equals("format")){
              // only supprorted value is json
              formatFlag = true;
              if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
              }
              continue;
          }

          if( parameter.getName().equals("start")) {
            if(parameter.getValue() == null){
              //pass
            } else{
                startFlag = true ;
                //check if value is int ( meaning a string that is parsable to an int value)
                Integer startValue = null;
                try {
                    startValue  = Integer.parseInt(parameter.getValue());
                }
                catch(Exception e) {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter start ");
                }
                map.put(parameter.getName(), parameter.getValue());
                localStart = startValue;
             }
              continue;

          }

          if( parameter.getName().equals("count")){
            if(parameter.getValue() == null){
              //pass
            } else {
                countFlag =  true ;
                //check if value is int ( meaning a string that is parsable to an int value)
                Integer countValue = null;
                try {
                    countValue  = Integer.parseInt(parameter.getValue());
                }
                catch(Exception e) {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter count ");
                }
                map.put(parameter.getName(), parameter.getValue());
                localCount=countValue;
            }
              continue;
          }

          // filter parameters given in the url --------------------------------------------------
          if( parameter.getName().equals("sort")){
            if(parameter.getValue() == null){
              //pass
            }
            else{
              sortFlag = true ;
              String sortString = parameter.getValue();
                // accepted values of sort are : id|ASC, id|DESC, name|ASC, name|DESC, default id|DESC
                if( !sortString.equals("price|ASC") && !sortString.equals("price|DESC")
                    && sortString.equals("date|DESC") && sortString.equals("date|ASC")
                    && !sortString.equals("geo.dist|ASC") && !sortString.equals("geo.dist|DESC")) {
                      throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter sort ");
                    }

              localSort.add(sortString);
            }
            continue;
          }

          if( parameter.getName().equals("geoDist")){

              if( parameter.getValue() == null){
                // pass
              }
              else{
                geoDistFlag =  true ;
                //check if value is int ( meaning a string that is parsable to an int value)
                Integer geoDistValue = null;
                try {
                    geoDistValue  = Integer.parseInt(parameter.getValue());
                }
                catch(Exception e) {
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter geoDist");
                }
                localgeoDist = geoDistValue;
              }
              continue;
          }

          if( parameter.getName().equals("geoLng")){
              if(parameter.getValue() == null){
                //pass
              }
              else{
                geoLngFlag = true;
                // check if value is double
                Double geoLngvalue = null ;
                try{
                    geoLngvalue = Double.parseDouble(parameter.getValue());
                }
                catch(Exception e){
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter geoLng");
                }
                localgeoLng = geoLngvalue ;
              }
              continue;
          }

          if( parameter.getName().equals("geoLat")){
            if(parameter.getName() == null){
              //pass
            }else {
                geoLatFlag = true;
                // check if value is double
                Double geoLatvalue = null ;
                try{
                    geoLatvalue = Double.parseDouble(parameter.getValue());
                }
                catch(Exception e){
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter geoLat");
                }
                localgeoLat = geoLatvalue ;
            }
                continue;
          }

          if( parameter.getName().equals("dateFrom")){
              if(parameter.getValue() == null){
                //pass
              } else {
                dateFromFlag = true;
                //
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                dateFormat.setLenient(false);
                try {
                  dateFormat.parse(parameter.getValue().trim());
                } catch (ParseException pe) {
                    //return false;
                    throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter dateFrom");
                }
                    //return true;
                localdateFrom = parameter.getValue();
              }
              continue;
          }

          if( parameter.getName().equals("dateTo")){
            if(parameter.getValue() == null){
              //pass
            } else {
              dateToFlag = true;
              //
              SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
              dateFormat.setLenient(false);
              try {
                dateFormat.parse(parameter.getValue().trim());
              } catch (ParseException pe) {
                  //return false;
                  throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter dateTo");
              }
                  //return true;
              localdateTo = parameter.getValue();
            }
            continue;
          }

          if( parameter.getName().equals("products")){
              if(parameter.getValue()==null){
                //pass
              }
              else{
              productsFlag = true;
              localProducts.add(parameter.getValue());
            }
          }

          if( parameter.getName().equals("shops")){
            if(parameter.getValue() == null){
              //pass
            } else {
              shopsFlag = true;
              localShops.add(parameter.getValue());
            }
          }

          if( parameter.getName().equals("tags")){
            if(parameter.getValue() == null){
              //pass
            } else{
              tagsFlag = true;
              localTags.add(parameter.getValue());
            }
          }

        }

        //------------------------------------------------------------------
        if(! startFlag) { localStart = 0 ; map.put("start", "0"); }
        if(! countFlag) { localCount = 20; map.put("count", "20"); }

        // check if geoDist,geoLng,geoLat ( success: 0 or all 3 of them)
        if( geoDistFlag) { paramDistCounter ++ ;}
        if( geoLngFlag) { paramDistCounter++; }
        if( geoLatFlag) { paramDistCounter++; }
        if( paramDistCounter!=0 && paramDistCounter!=3){
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "geoLng,geoLat,geoDist 0 or all 3 of them!");
        }
        if( paramDistCounter == 0){
            queryWithDist = false; // sql query without factoring distances
        }

        // check if both dates are given (success: 0 or all of them 2 )
        if( dateToFlag) { paramDateCounter++ ;}
        if( dateFromFlag) { paramDateCounter++ ;}
        if( paramDateCounter!=0 && paramDateCounter!=2){
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "dateFrom,dateTo 0 or both of them!");
        }
        if( paramDateCounter == 0){
            queryWith2Dates = false;  //
        }

        //---------------------------------------------------------------------
        if(!sortFlag){
           localSort.add("price|ASC");
        }
        //---------------------------------------------------------------------
        if( !queryWith2Dates){
            // set in both the current date
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            localdateTo = timeStamp ;
            localdateFrom = timeStamp ;
        }

        // 2 methods 2 cases ( factoring distances and not )
        List<Price> priceObjs =  null;
        if( queryWithDist ){
            priceObjs = dataAccess.getPrices(new Limits(localStart, localCount), localgeoDist, localgeoLat, localgeoLng,
            localdateFrom, localdateTo, localSort.toArray(new String[0]), localShops.toArray(new String[0]),
            localProducts.toArray(new String[0]), localTags.toArray(new String [0]) );
        }
        else{
            priceObjs = dataAccess.getPricesNoDist(new Limits(localStart, localCount), localdateFrom, localdateTo,
            localSort.toArray(new String[0]), localShops.toArray(new String[0]), localProducts.toArray(new String[0]),
            localTags.toArray(new String [0]) );
        }

        Iterator<Price> i = priceObjs.iterator();
        //int w = 1;
        int str = 0;

          map.put("total", String.valueOf(priceObjs.size()));

        while (i.hasNext()) {
            Object o = i.next();
            if (str<localStart || str>localCount+localStart-1){
              i.remove();

            }
            str++;
        }


        map.put("prices", priceObjs);

        return new JsonMapRepresentation(map);

    }


    @Override
    protected Representation post(Representation entity) throws ResourceException {

        //----------------------------------------------
        Map<String, Object> map = new HashMap<>();

        Form f1 = getRequest().getResourceRef().getQueryAsForm();
        for(Parameter parameter : f1){
          if(parameter.getName().equals("format")){
            // only supprorted value is json
            if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
            }
          }
        }
        //------------------------------------------------

        //--------- check if user is logged in to perform this task --------------
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");

        //---------------------------------------
        if(user_token == null){
          //
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "did not provide authentication token");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            // user is ok
        }
        else if(user_token.equals("321CBB")){
          //user ok but not admin
        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, " did not provide authentication or wron creds ");
        }

        //-----------------------------------------------------------------------

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String price2 = form.getFirstValue("price");
        String dateFrom0 = form.getFirstValue("dateFrom");
        String dateTo0 = form.getFirstValue("dateTo");
        String productId2 = form.getFirstValue("productId");
        String shopId2 = form.getFirstValue("shopId");
        long productId = 0;
        long testLong;
        boolean isLong = true;
        try{
          testLong = Long.valueOf(productId2).longValue();
        } catch (NumberFormatException e){
         isLong = false;
        }
        if (isLong){
          productId = Long.valueOf(productId2).longValue();
        }
        else{
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, " Wrong Value ");
        }

          long shopId = Long.valueOf(shopId2).longValue();

        double price = Double.parseDouble(price2);
        DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTo = LocalDate.parse(dateTo0, myFormat);
        LocalDate dateFrom = LocalDate.parse(dateFrom0, myFormat);


        /*try {

          LocalDate dateFrom = LocalDate.parse(dateFrom0, myFormat);

        } catch (ParseException e) {
          e.printStackTrace();
        }

        try {

          LocalDate dateTo = LocalDate.parse(dateTo0, myFormat);

        } catch (ParseException e) {
          e.printStackTrace();
        }*/



        /*if(price == null || dateFrom == null || dateTo == null || shopId == null || productId == null){
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "some parameter names were not correct or omitted ");
        }*/


        //validate the values (in the general case)-----------------------
        //TODO:
        /*Integer testInt = 0;
        boolean isInt=true;

        isInt = true;
        try {
            testInt = Integer.parseInt(description);
        }
        catch(Exception e){
          isInt = false;
          // could not be parsed to Integer so its a valid string
        }
        if(isInt){
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter description");
        }

        // category has fixed values , such as: fps,sport-game,action,RPG,mouse,monitor,soundsystem


        if( form.getFirstValue("withdrawn")==null){
            withdrawn = false;
        }
        else{
            try{
              boolean tempBoolean = Boolean.valueOf(form.getFirstValue("withdrawn"));
              withdrawn = tempBoolean ;
            }
            catch(Exception e){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter withdrawn");
            }
        }

        int q = 0;
        for(String elem : tags){
          isInt = true;
          try {
              testInt = Integer.parseInt(elem);
          }
          catch(Exception e){
            isInt = false;
            // could not be parsed to Integer so its a valid string
          }
          if(isInt){
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter tag[" + q + "]");
          }
          q++ ;

        }*/
        //---------------------------------------------------------------

        List<Price> prices = dataAccess.addPrice(price, dateFrom, dateTo, productId, shopId);
        map.put("start",0);
        map.put("count",20);
        map.put("prices", prices);
        map.put("total", String.valueOf(prices.size()));
        return new JsonMapRepresentation(map);
    }

}

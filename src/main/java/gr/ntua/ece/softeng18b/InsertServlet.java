package gr.ntua.ece.softeng18b;

import gr.ntua.ece.softeng18b.data.model.*;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

//--------------------------------------
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.*;
//----------------------------------------------
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.TimeUnit ;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

//-----------------------------------------------

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.RequestDispatcher;
//-----------------------
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import java.util.List;
//----------------------
import com.google.maps.errors.ApiException;
import com.google.maps.internal.ApiResponse;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

//-------------------------
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;

//------------------------------------
public class InsertServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

          // code to process the form...
          // example of handling

          //*********************************
          final DataAccess dataAccess = Configuration.getInstance().getDataAccess();
          //*********************************
          boolean alarm = false ;

          String brand = request.getParameter("brand");
          String model = request.getParameter("model");
          String productName = brand + "_" + model;
          String category = request.getParameter("category");
          String description = request.getParameter("description");
          String productTags = request.getParameter("productTag") ; // comma separated string
          String price = request.getParameter("price") ;
          String shopName = request.getParameter("shopName");
          String city = request.getParameter("city");
          String town = request.getParameter("town");
          String streetName = request.getParameter("streetName");
          String streetNumber = request.getParameter("streetNumber");
          String shopTags = request.getParameter("shopTag");
          String dateFrom0 = request.getParameter("dateFrom");   //    dd/mm/yyyy
          String dateTo0 = request.getParameter("dateTo");       //    dd/mm/yyyy
          String address="";
          address = address + streetName + " " + streetNumber + " ," + town + " " + city;
          Map<String, Double> coords;
          //System.out.println("latitude :" + coords.get("lat"));
          //System.out.println("longitude:" + coords.get("lon"));
          //---------------
          //String urlrequest = " https://maps.googleapis.com/maps/api/geocode/json?address="+address+",+CA&key=AIzaSyBhWrpN3XbzhNN7H9DDIgs3RumIIwkl3Y4";
          //URL url = new URL(urlrequest);
          //HttpURLConnection con = (HttpURLConnection) url.openConnection();
          //con.setRequestMethod("GET");
          //int responseCode = con.getResponseCode();
          //----------------------------------------------------------
          /*BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
        		String inputLine;
        		StringBuffer responseBuff = new StringBuffer();

        		while ((inputLine = in.readLine()) != null) {
        			responseBuff.append(inputLine);
        		}
        		in.close(); */
          //---------------------------------------------------------
          /*String strlat = "";
          String strlon = "";
          JSONParser parser = new JSONParser();
          JSONObject jsonObj = new  JSONObject();
          jsonObj = (JSONObject) parser.parse(responseBuff.toString());
          //String argString = responseBuff.toString();
          //JSONObject jsonObj = new JSONObject(argString);
          JSONArray jArray = new JSONArray();
          jArray = (JSONArray) jsonObj.getJSONArray("location");
          for (int i = 0; i < jArray.length ; i++)
          {
              strlat = jArray.getJSONObject(i).getString("lat");
              strlon = jArray.getJSONObject(i).getString("lon");
          }
          double lng = Double.parseDouble(strlon);
          double lat = Double.parseDouble(strlat);
          */
          //---------------------------------------------------------------
          double lng = 27.9889;
          double lat = 38.85855;
          String[] tags1= productTags.split(",");
          String[] tags2 =  shopTags.split(",");
          Product inputProduct = dataAccess.addProduct(productName, description, category, false, tags1 );
          Shop inputShop = dataAccess.addShop(shopName, address, lng, lat, false, tags2 );
          // find pid ,sid
          DateTimeFormatter myFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          LocalDate dateTo = LocalDate.parse(dateTo0, myFormat);
          LocalDate dateFrom = LocalDate.parse(dateFrom0, myFormat);
          long productId = 0;
          long shopId = 0 ;
          boolean shopFlag = false;
          boolean productFlag = false;
          List<Product> pBuffer = dataAccess.getProducts( new Limits(0, 100));
          List<Shop> shBuffer = dataAccess.getShops( new Limits(0, 100));
          for( Product elem : pBuffer){

            if(elem.getName().equals(productName) && elem.getDescription().equals(description)
             && elem.getCategory().equals(category) && elem.isWithdrawn() == false &&
               Arrays.equals(tags1 , elem.getTags().toArray(new String[0]) ) ){
                    productFlag = true ;
                    productId = elem.getId();
               }

          }

          for( Shop elem2 : shBuffer){

            if( elem2.getLat() == lat && elem2.getLng() == lng){
                    shopFlag = true ;
                    shopId = elem2.getId();
               }

          }

          if( productFlag && shopFlag){
            List<Price> priceObjs = dataAccess.addPrice(Double.parseDouble(price), dateFrom, dateTo, productId, shopId );


          }

          RequestDispatcher reqDispatcher = getServletConfig().getServletContext().getRequestDispatcher("/index_user.jsp");
          reqDispatcher.forward(request,response);



    }

}


class OpenStreetMapUtils {

    public final static Logger log = Logger.getLogger("OpenStreeMapUtils");

    private static OpenStreetMapUtils instance = null;
    private JSONParser jsonParser;

    public OpenStreetMapUtils() {
        jsonParser = new JSONParser();
    }

    public static OpenStreetMapUtils getInstance() {
        if (instance == null) {
            instance = new OpenStreetMapUtils();
        }
        return instance;
    }

    private String getRequest(String url) throws Exception {

        final URL obj = new URL(url);
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200) {
            return null;
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public Map<String, Double> getCoordinates(String address) {
        Map<String, Double> res;
        StringBuffer query;
        String[] split = address.split(",");
        String queryResult = null;

        query = new StringBuffer();
        res = new HashMap<String, Double>();

        query.append("http://nominatim.openstreetmap.org/search?q=");

        if (split.length == 0) {
            return null;
        }

        for (int i = 0; i < split.length; i++) {
            query.append(split[i]);
            if (i < (split.length - 1)) {
                query.append("+");
            }
        }
        query.append("&format=json&addressdetails=1");

        log.debug("Query:" + query);

        try {
            queryResult = getRequest(query.toString());
        } catch (Exception e) {
            log.error("Error when trying to get data with the following query " + query);
        }

        if (queryResult == null) {
            return null;
        }

        Object obj = JSONValue.parse(queryResult);
        log.debug("obj=" + obj);

        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            if (array.size() > 0) {
                JSONObject jsonObject = (JSONObject) array.get(0);

                String lon = (String) jsonObject.get("lon");
                String lat = (String) jsonObject.get("lat");
                log.debug("lon=" + lon);
                log.debug("lat=" + lat);
                res.put("lon", Double.parseDouble(lon));
                res.put("lat", Double.parseDouble(lat));

            }
        }

        return res;
    }
}

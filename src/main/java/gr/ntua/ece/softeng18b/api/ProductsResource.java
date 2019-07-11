package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
import gr.ntua.ece.softeng18b.data.model.Product;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

//--------------------
import org.restlet.data.Status;
import org.restlet.data.Parameter;
import java.lang.Math.*;
import java.util.Iterator ;
import java.util.Collections;
import java.util.Comparator;
import org.restlet.util.Series;
import org.restlet.data.Header;
//------------------

public class ProductsResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        Map<String, Object> map = new HashMap<>();
        Form f1 = getRequest().getResourceRef().getQueryAsForm();

        //----------------------------------------
        // support the follow parameters in url query: start , count , sort and status  (default values if user does not provide any)
        // support error handling for the values of  those parameters ; if not as expected return bad request  (400 error code)
        // get request (method in Class Resource then from request get info of URI)

        boolean startFlag,countFlag,sortFlag,statusFlag,formatFlag;
        int localStart =0;
        int localCount =0;
        startFlag=false;
        countFlag=false;
        sortFlag=false;
        statusFlag=false;
        formatFlag=false;
        String strStatus="";
        String strSort="" ;
        String strFormat="";

        for(Parameter parameter : f1){

          if( parameter.getName().equals("format")){
              formatFlag = true;
              // only supprorted value is json
              if(! parameter.getValue().equals("json")){
                  throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
              }
              strFormat = "json";
              continue;
          }

          if( parameter.getName().equals("start")) {
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
              continue;

          }

          if( parameter.getName().equals("count")){
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
              continue;
          }

          if( parameter.getName().equals("status")){
              statusFlag = true ;
              // accepted values of status are : ALL , WITHDRAWN , ACTIVE  (DEFAULT IS ACTIVE)
              if( !parameter.getValue().equals("ALL") && !parameter.getValue().equals("ACTIVE") && !parameter.getValue().equals("WITHDRAWN") ){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter status ");
              }
              strStatus=parameter.getValue();
              continue;
          }

          if( parameter.getName().equals("sort")){
            sortFlag = true ;
            // accepted values of sort are : id|ASC, id|DESC, name|ASC, name|DESC, default id|DESC
            if( !parameter.getValue().equals("id|ASC") && !parameter.getValue().equals("id|DESC")
                && !parameter.getValue().equals("name|DESC") && !parameter.getValue().equals("name|ASC") ){
                  throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter sort ");
                }
            strSort = parameter.getValue();
            continue;

          }

        }

        if( !formatFlag){
           strFormat = "json";
        }

        if( !startFlag){
          // start parameter was not found --> set to default value
          Integer defaultInt  = 0 ;
          localStart = 0;
          map.put("start", defaultInt);
        }

        if( !countFlag){
          // count parameter was not found --> set to default values
          localCount = 20;
          Integer defaultCount = 20;
          map.put("count", defaultCount);
        }

        if( !statusFlag ) {strStatus = "ACTIVE" ; }
        if( !sortFlag ) { strSort = "id|DESC" ; }

        //List<Product> products = dataAccess.getProducts(new Limits(0, 10));
        List<Product> products = dataAccess.getProducts(new Limits(localStart, localCount));

        //filter with status--------------------------------------------------------

        if(strStatus.equals("ACTIVE")){

          // we keep in the list only products that have withdrawn value = 0
          Iterator<Product> i = products.iterator();
          while(i.hasNext()){
              Product o = i.next();
              if(o.isWithdrawn()){
                // not active ==> remove it from List
                i.remove();
              }
          }
        }
        else if(strStatus.equals("ALL")){
            // no filter here
            //pass
        }
        else if(strStatus.equals("WITHDRAWN")){
          // we keep items only with withdrawn value = 1
          Iterator<Product> i = products.iterator();
          while(i.hasNext()){
            Product o = i.next();
            if(! o.isWithdrawn()){
              // is active ==> remove it from List
              i.remove();
            }
          }
        }

        // end status filter ------------------------------------------------------

        // sort filter   ----------------------------------------------------------
        if(strSort.equals("id|DESC")){
          Collections.sort(products, new Comparator<Product>() {
            @Override
            public int compare(Product p1, Product p2){
                return Long.valueOf(p2.getId()).compareTo(Long.valueOf(p1.getId())); // descending order
            }
          });

        }
        else if(strSort.equals("id|ASC")){
          Collections.sort(products, new Comparator<Product>(){
            @Override
            public int compare(Product p1, Product p2){
              return Long.valueOf(p1.getId()).compareTo(Long.valueOf(p2.getId()));  // ascending order
            }
          });

        }
        else if(strSort.equals("name|ASC")){
          Collections.sort(products, new Comparator<Product>(){
            @Override
            public int compare(Product p1, Product p2){
              return p1.getName().compareTo(p2.getName());  // ascending order
            }
          });

        }
        else if(strSort.equals("name|DESC")){
          Collections.sort(products, new Comparator<Product>(){
            @Override
            public int compare(Product p1, Product p2){
              return p2.getName().compareTo(p1.getName());  // descending order
            }
          });
        }


        // end of sort filter ------------------------------------------------------------



        Iterator<Product> i = products.iterator();
        //int w = 1;
        int str = 0;


        while (i.hasNext()) {
            Object o = i.next();
            if (str<localStart || str>localCount+localStart-1){
              i.remove();

            }
            str++;
        }

        //---------------------------------------------------------------------
        map.put("total", String.valueOf(products.size()));
        map.put("products", products);

        return new JsonMapRepresentation(map);
    }

    @Override
    protected Representation post(Representation entity) throws ResourceException {

        //----------------------------------------------
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
        String name = form.getFirstValue("name");
        String description = form.getFirstValue("description");
        String category = form.getFirstValue("category");
        boolean withdrawn = false;
        String[] tags = form.getValuesArray("tags");

        if(name == null || description == null || category == null || tags == null  ){
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "some parameter names were not correct or omitted ");
        }


        //validate the values (in the general case)-----------------------
        //TODO:
        Integer testInt = 0;
        boolean isInt=true;
        try {
            testInt  = Integer.parseInt(name);
        }
        catch(Exception e) {
            isInt = false;
            // could not be parsed to Integer so its a valid string
        }
        if(isInt){
          // isInt is true  => bad input parameters
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter name");
        }

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

        }
        //---------------------------------------------------------------

        Product product = dataAccess.addProduct(name, description, category, withdrawn, tags);

        return new JsonProductRepresentation(product);
    }


}

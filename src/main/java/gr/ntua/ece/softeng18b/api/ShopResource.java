package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Shop;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import java.util.*;
import java.util.Optional;

//------------------------------------------------------
import org.restlet.data.Parameter;
import org.restlet.data.Form;
import org.restlet.util.Series;
import org.restlet.data.Header;
//-----------------------------------------------------

public class ShopResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation get() throws ResourceException {

        Form f1 = getRequest().getResourceRef().getQueryAsForm();
        for(Parameter parameter : f1){
          if(parameter.getName().equals("format")){
            // only supprorted value is json
            if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
            }
          }
        }

        String idAttr = getAttribute("id");

        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
        }

        Optional<Shop> optional = dataAccess.getShop(id);
        Shop shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));

        return new JsonShopRepresentation(shop);
    }
    // -------------------PUT Shop--------------------------------
    // -----------------------------------------------------------

    @Override
    protected Representation put(Representation entity) throws ResourceException{

        Form f1 = getRequest().getResourceRef().getQueryAsForm();
        for(Parameter parameter : f1){
          if(parameter.getName().equals("format")){
            // only supprorted value is json
            if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
            }
          }
        }

        //--------- check user authentication ----------------------------------------------------------------------
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");

        //---------------------------------------
        if(user_token == null){
          //
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "must be logged in to be able to put shop");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            // user is ok
        }
        else if(user_token.equals("321CBB")){
          // user but not admin
        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant logout if you are not logged in  ");
        }
        //---------------------------------------------------------------------------------------------------------

        String idAttr = getAttribute("id");
        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
        }

        //-------- get params from body -------------------------------------------------------------------
        //------- and check for valid input ---------------------------------------------------------------

        //Create a new restlet form
        Form form = new Form(entity);
        //Read the parameters
        String name = form.getFirstValue("name");
        String address = form.getFirstValue("address");
        String lng = form.getFirstValue("lng");
        String lat = form.getFirstValue("lat");
        boolean withdrawn = false;
        String[] tags = form.getValuesArray("tags");

        if(name == null || address == null || lng == null || lat == null || tags == null){
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
            testInt = Integer.parseInt(address);
        }
        catch(Exception e){
          isInt = false;
          // could not be parsed to Integer so its a valid string
        }
        if(isInt){
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter description");
        }

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

        //------------- -----------------------------------------------------------------------------------
        double lng2 = Double.parseDouble(lng);
        double lat2 = Double.parseDouble(lat);


        Optional<Shop> optional = dataAccess.updateShop(id, name, address, lng2, lat2, withdrawn, tags);
        Shop shop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found - id: " + idAttr));

        return new JsonShopRepresentation(shop);


    }


    @Override
    protected Representation patch(Representation entity) throws ResourceException{

        Form f1 = getRequest().getResourceRef().getQueryAsForm();
        for(Parameter parameter : f1){
          if(parameter.getName().equals("format")){
            // only supprorted value is json
            if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
            }
          }
        }

        //--------- check user authentication ----------------------------------------------------------------------
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");

        //---------------------------------------
        if(user_token == null){
          //
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "must be logged in to be able to patch shop");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            // user is ok
        }
        else if(user_token.equals("321CBB")){
          // user ok but not admin
        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant logout if you are not logged in  ");
        }
        //---------------------------------------------------------------------------------------------------------

        String idAttr = getAttribute("id");
        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
        }

        //-------- get 1 paramfrom body if more bad request -------------------------------------------------------------------
        //------- and check for valid input ---------------------------------------------------------------
        Shop shop = null ;
        Optional<Shop> op1 =null;
        op1 = dataAccess.getShop(id);
        shop = op1.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found "));
        List<String> tempList = new ArrayList<>();
        tempList = shop.getTags();
        String[] tempTags = tempList.toArray(new String[0]);
        String tempName = shop.getName();
        String tempAddress = shop.getAddress();
        double tempLng = shop.getLng();
        double tempLat = shop.getLat();
        boolean tempWithdrawn = shop.isWithdrawn();


        Optional<Shop> optional = null ;
        Shop finalShop = null;

        //Create a new restlet form
        Form form = new Form(entity);
        //----------------------------
        boolean nameFlag,addressFlag,lngFlag,latFlag,withdrawnFlag,tagsFlag;
        nameFlag = false;
        addressFlag = false;
        lngFlag  = false;
        latFlag  = false;
        withdrawnFlag = false;
        tagsFlag = false;
        int paramCounter = 0;

        String name = form.getFirstValue("name");
        String address = form.getFirstValue("address");
        String lng = form.getFirstValue("lng");
        String lat = form.getFirstValue("lat");
        boolean withdrawn = false;
        String[] tags = form.getValuesArray("tags");

        if( form.getFirstValue("name") != null){
          nameFlag = true;
          paramCounter++;
        }
        if( form.getFirstValue("address") == null){  } else { addressFlag=true; paramCounter++;}
        if( form.getFirstValue("lng") == null) { } else { lngFlag=true; paramCounter++; }
        if( form.getFirstValue("lat") == null) { } else { latFlag=true; paramCounter++; }
        if( form.getFirstValue("withdrawn") == null ) { } else{ withdrawnFlag=true; paramCounter++; }
        if( form.getValuesArray("tags") == null) { } else { tagsFlag=true; paramCounter++; }

        if(paramCounter > 2 ){  // 1 and plus 1 in query ?
             throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, " total of " + paramCounter + " params use put method instead");
        }

        long testInt = 0;
        boolean isInt=true;
        if( nameFlag){
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

            tempName = name;
        }


        isInt=true;
        if(addressFlag){
            try {
                testInt  = Integer.parseInt(address);
            }
            catch(Exception e) {
                isInt = false;
                // could not be parsed to Integer so its a valid string
            }
            if(isInt){
              // isInt is true  => bad input parameters
              throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter description");
            }

            tempAddress = address ;

        }


        isInt=true;
        if(lngFlag){
            try {
                testInt  = Long.parseLong(lng);
            }
            catch(Exception e) {
                isInt = false;
                // could not be parsed to Long so its a valid string
            }
            if(isInt){
              // isInt is true  => bad input parameters
              throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter name");
            }


            double lng2 = Double.parseDouble(lng);

            tempLng = lng2;


        }

        isInt=true;
        if(latFlag){
            try {
                testInt  = Long.parseLong(lat);
            }
            catch(Exception e) {
                isInt = false;
                // could not be parsed to long so its a valid string
            }
            if(isInt){
              // isInt is true  => bad input parameters
              throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter name");
            }

            double lat2 = Double.parseDouble(lat);

            tempLat = lat2;


        }

        if(withdrawnFlag){
            try{
              boolean tempBoolean = Boolean.valueOf(form.getFirstValue("withdrawn"));
              withdrawn = tempBoolean ;

            }
            catch(Exception e){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter withdrawn");
            }

            tempWithdrawn = withdrawn ;

        }

        if(tagsFlag){
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

            //String tagsCommaSeperated = String.join("," , tags);
            tempTags = tags ;

        }





        optional = dataAccess.updateShop(id, tempName, tempAddress, tempLng, tempLat, tempWithdrawn, tempTags);
        finalShop = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found "));
        op1 = dataAccess.getShop(id);
        shop = op1.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop not found "));
        finalShop.setTags(shop.getTags());
        return new JsonShopRepresentation(finalShop);

    }


    @Override
    protected Representation delete() throws ResourceException {

        Form f1 = getRequest().getResourceRef().getQueryAsForm();
        for(Parameter parameter : f1){
          if(parameter.getName().equals("format")){
            // only supprorted value is json
            if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
            }
          }
        }

        //--------- check user authentication ----------------------------------------------------------------------
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");
        boolean isAdmin = false ;

        //---------------------------------------
        if(user_token == null){
          //
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "must be logged in to be able to delete shop");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            // user is ok
            isAdmin = true;
        }
        else if( user_token.equals("321CBB")){
            //ok but not admin
            isAdmin = false;
        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant delete wrong token bro ");
        }
        //---------------------------------------------------------------------------------------------------------

        String idAttr = getAttribute("id");
        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing shop id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid shop id: " + idAttr);
        }

        //------------------------------------------------------------------------------------------------------------

        Map<String, Object> map = new HashMap<>();
        Shop shop = null ;
        Optional<Shop> op1 =null;
        op1 = dataAccess.getShop(id);
        shop = op1.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Shop with id  " + idAttr  + " was not found "));

        dataAccess.deleteShop(id, isAdmin);
        String isOk = "OK";
        map.put("message", isOk);
        return new JsonMapRepresentation(map);

    }

}

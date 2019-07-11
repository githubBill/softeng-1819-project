package gr.ntua.ece.softeng18b.api;

import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.model.Product;
import org.restlet.data.Status;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//------------------------------------------------------
import org.restlet.data.Parameter;
import org.restlet.data.Form;
import org.restlet.util.Series;
import org.restlet.data.Header;
//-----------------------------------------------------

public class ProductResource extends ServerResource {

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
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }

        Optional<Product> optional = dataAccess.getProduct(id);
        Product product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));

        return new JsonProductRepresentation(product);
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
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "must be logged in to be able to delete product");
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
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }

        //------------------------------------------------------------------------------------------------------------

        Map<String, Object> map = new HashMap<>();
        Product product = null ;
        Optional<Product> op1 =null;
        op1 = dataAccess.getProduct(id);
        product = op1.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product with id  " + idAttr  + " was not found "));

        dataAccess.deleteProduct(id, isAdmin);
        String isOk = "OK";
        map.put("message", isOk);
        return new JsonMapRepresentation(map);

    }


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
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "must be logged in to be able to put product");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            // user is ok
        }
        else if( user_token.equals("321CBB")){
            //ok but not admin

        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant logout if you are not logged in  ");
        }
        //---------------------------------------------------------------------------------------------------------

        String idAttr = getAttribute("id");
        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }

        //-------- get params from body -------------------------------------------------------------------
        //------- and check for valid input ---------------------------------------------------------------

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

        //------------- -----------------------------------------------------------------------------------

        Optional<Product> optional = dataAccess.updateProduct(id,name,description,category,withdrawn,tags);
        Product product = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found - id: " + idAttr));

        return new JsonProductRepresentation(product);
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
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "must be logged in to be able to put product");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            // user is ok
        }
        else if( user_token.equals("321CBB")){
            //ok but not admin

        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant logout if you are not logged in  ");
        }
        //---------------------------------------------------------------------------------------------------------

        String idAttr = getAttribute("id");
        if (idAttr == null) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Missing product id");
        }

        Long id = null;
        try {
            id = Long.parseLong(idAttr);
        }
        catch(Exception e) {
            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid product id: " + idAttr);
        }

        //-------- get 1 paramfrom body if more bad request -------------------------------------------------------------------
        //------- and check for valid input ---------------------------------------------------------------
        Product product = null ;
        Optional<Product> op1 =null;
        op1 = dataAccess.getProduct(id);
        product = op1.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found "));
        List<String> tempList = new ArrayList<>();
        tempList = product.getTags();
        String[] tempTags = tempList.toArray(new String[0]);
        String tempName = product.getName();
        String tempCategory = product.getCategory();
        String tempDescription = product.getDescription();
        boolean tempWithdrawn = product.isWithdrawn();


        Optional<Product> optional = null ;
        Product finalProduct = null;

        //Create a new restlet form
        Form form = new Form(entity);
        //----------------------------
        boolean nameFlag,descFlag,catFlag,withdrawnFlag,tagsFlag;
        nameFlag = false;
        descFlag = false;
        catFlag  = false;
        withdrawnFlag = false;
        tagsFlag = false;
        int paramCounter = 0;

        String name = form.getFirstValue("name");
        String description = form.getFirstValue("description");
        String category = form.getFirstValue("category");
        boolean withdrawn = false;
        String[] tags = form.getValuesArray("tags");

        if( form.getFirstValue("name") != null){
          nameFlag = true;
          paramCounter++;
        }
        if( form.getFirstValue("description") == null){  } else { descFlag=true; paramCounter++;}
        if( form.getFirstValue("category") == null) { } else { catFlag=true; paramCounter++; }
        if( form.getFirstValue("withdrawn") == null ) { } else{ withdrawnFlag=true; paramCounter++; }
        if( form.getValuesArray("tags") == null) { } else { tagsFlag=true; paramCounter++; }

        if(paramCounter > 2 ){  // 1 and plus 1 in query ?
             throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, " total of " + paramCounter + " params use put method instead");
        }

        Integer testInt = 0;
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
        if(descFlag){
            try {
                testInt  = Integer.parseInt(description);
            }
            catch(Exception e) {
                isInt = false;
                // could not be parsed to Integer so its a valid string
            }
            if(isInt){
              // isInt is true  => bad input parameters
              throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter description");
            }

            tempDescription = description ;

        }
        isInt=true;
        if(catFlag){

            if(!category.equals("sports-game") && !category.equals("RPG") && !category.equals("action") &&
               !category.equals("mouse") && !category.equals("monitor") && !category.equals("soundsystem") && !category.equals("fps")){
                   throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter category");
            } else {

              tempCategory = category;

            }
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

        optional = dataAccess.updateProduct(id,tempName, tempDescription, tempCategory,tempWithdrawn, tempTags);
        finalProduct = optional.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found "));
        op1 = dataAccess.getProduct(id);
        product = op1.orElseThrow(() -> new ResourceException(Status.CLIENT_ERROR_NOT_FOUND, "Product not found "));
        finalProduct.setTags(product.getTags());
        return new JsonProductRepresentation(finalProduct);

      }

}

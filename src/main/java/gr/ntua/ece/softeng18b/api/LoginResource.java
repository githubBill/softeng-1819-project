package gr.ntua.ece.softeng18b.api;

import java.util.HashMap;
import java.util.Map;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

//--------------
import org.restlet.representation.StringRepresentation;
import org.restlet.data.MediaType;
import org.restlet.data.Form;
import org.restlet.data.Status;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
//-----------------

import org.restlet.data.Parameter;
//-----------------


public class LoginResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {
        //All login attempts succeed with the same toke
        Form f1 = getRequest().getResourceRef().getQueryAsForm();
        for(Parameter parameter : f1){
          if(parameter.getName().equals("format")){
            // only supprorted value is json
            if(! parameter.getValue().equals("json")){
                throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "Invalid value of parameter format ");
            }
          }
        }

        //--------------------------------------
        final Form form = new Form(entity);
        String username = form.getFirstValue("username");
        String password = form.getFirstValue("password");
        //---------------------------------------
        Map<String, Object> map = new HashMap<>();

        //------------------------------------------------
        if(username == null || password == null){
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "some parameters were not provided ");
        }

        boolean isValiduser = false;
        isValiduser =  dataAccess.validateUser(username, password);

        if(isValiduser){

            if(password.equals("fakepassword")) {map.put("token", "321CBA");}  // for admin
            else { map.put("token", "321CBB") ; }

        }
        else{
          throw new ResourceException(Status.CLIENT_ERROR_UNAUTHORIZED, "wrong credentials bro");
        }

        //------------------------------------

        return new JsonMapRepresentation(map);
    }


}

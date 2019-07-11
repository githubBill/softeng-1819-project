package gr.ntua.ece.softeng18b.api;

import org.restlet.resource.ServerResource;

//--------------
import org.restlet.representation.StringRepresentation;
import org.restlet.data.MediaType;
import org.restlet.data.Form;
import org.restlet.data.Status;
import gr.ntua.ece.softeng18b.conf.Configuration;
import gr.ntua.ece.softeng18b.data.DataAccess;
import gr.ntua.ece.softeng18b.data.Limits;
//-------------
import java.util.HashMap;
import java.util.Map;
import org.restlet.representation.Representation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

//--------------
import org.restlet.util.Series;
import org.restlet.data.Header;
import org.restlet.data.Parameter;
//--------


public class LogoutResource extends ServerResource {

    private final DataAccess dataAccess = Configuration.getInstance().getDataAccess();

    @Override
    protected Representation post(Representation entity) throws ResourceException {
        Map<String, Object> map = new HashMap<>();

        //---------------------------------------
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
        //final Form form = (Form) getRequest().getAttributes().get("org.restlet.http.headers");
        //String user_token = form.getFirstValue("X-OBSERVATORY-AUTH");
        Series<Header> headers = (Series<Header>) getRequestAttributes().get("org.restlet.http.headers");
        String user_token = headers.getFirstValue("X-OBSERVATORY-AUTH");

        //---------------------------------------
        if(user_token == null){
          //
          throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant logout if you are not logged in");
        }

        //----------------------------------------

        if(user_token.equals("321CBA")){
            String isok = "OK";
            String mess = "message";
            map.put(mess,isok);
        }
        else{

            throw new ResourceException(Status.CLIENT_ERROR_BAD_REQUEST, "cant logout if you are not logged in  ");
        }

        //------------------------------------

        return new JsonMapRepresentation(map);
    }


}

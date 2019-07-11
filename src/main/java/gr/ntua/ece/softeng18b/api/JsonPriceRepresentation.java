package gr.ntua.ece.softeng18b.api;

import com.google.gson.Gson;
import gr.ntua.ece.softeng18b.data.model.Price;
import org.restlet.data.MediaType;
import org.restlet.representation.WriterRepresentation;

import java.io.IOException;
import java.io.Writer;

public class JsonPriceRepresentation extends WriterRepresentation {

    private final Price priceObj;

    public JsonPriceRepresentation(Price priceObj) {
        super(MediaType.APPLICATION_JSON);
        this.priceObj = priceObj;
    }

    @Override
    public void write(Writer writer) throws IOException {
        Gson gson = new Gson();
        writer.write(gson.toJson(priceObj));
    }
}

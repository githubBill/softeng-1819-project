package gr.ntua.ece.softeng18b.api;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestfulApp extends Application {

    @Override
    public synchronized Restlet createInboundRoot() {

        // the above method (that we Override ) is a factory method
        // called by the framework when the Application starts. Its' in charge
        // of creating the inbound root Restlet.

        Router router = new Router(getContext());

        //GET, POST
        router.attach("/products", ProductsResource.class);

        //GET, DELETE  TODO: PUT,PATCH
        router.attach("/products/{id}", ProductResource.class);

        router.attach("/login", LoginResource.class);

        // api for logout (method: POST)
        router.attach("/logout", LogoutResource.class);

        // GET, POST
        router.attach("/shops", ShopsResource.class);

        // GET, DELETE , PUT, PATCH
        router.attach("/shops/{id}", ShopResource.class);

        // GET , POST
        router.attach("/prices", PricesResource.class);

        return router;
    }

}

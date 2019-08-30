package fkealy;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class App
{
    public static void main( String[] args )
    {
        Vertx vertx = Vertx.vertx();

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        Route getHelloHandler = router.get("/hello/:name")
                .handler(routingContext -> {
                    String name = routingContext.request().getParam("name");
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.putHeader("content-type", "text/plain");
                    response.end("Hello " + name + " \n");
                });

        Route postHelloHandler = router
                .post("/hello")
                .consumes("application/json")
                .handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    response.setChunked(true);
                    response.putHeader("content-type", "text/plain");
                    response.write("you posted fkealy? \n");
                    routingContext
                            .vertx()
                            .setTimer(1000,tid -> routingContext.next());
                });


        Route howAreYou = router.route("/hello")
                .handler(routingContext -> {
                    HttpServerResponse response = routingContext.response();
                    response.putHeader("content-type", "text/plain");
                        response.write("How are  you? \n");
                        response.end("bye");
                });


        httpServer
                .requestHandler(router::accept)
                .listen(8091);
    }
}

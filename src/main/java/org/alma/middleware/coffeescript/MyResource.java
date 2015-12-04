package org.alma.middleware.coffeescript;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.message.BasicNameValuePair;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("coffeebackend")
public class MyResource {

    final static String authServer = "http://localhost:8085";
    final static String apiKey = "bb9dd8fb-98fb-40d7-914a-7ff2cc06cff9";

    @GET
    @Path("bonjour")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBonjour() {
        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity("{authServer: \""+authServer+"\", apiKey: \""+apiKey+"\"}")
                .build();
    }

    @POST
    @Path("startSession")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postStartSession(TokenRequest tokenRequest) throws IOException {
        System.out.println(tokenRequest.getToken());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost request = new HttpPost(authServer+"/token");
        request.setHeader("Content-type","application/json");
        request.setHeader("Accept", "application/json");
        request.setEntity(new StringEntity("{\"token\": \""+tokenRequest.getToken()+"\"}", "UTF-8"));
        CloseableHttpResponse response = httpClient.execute(request);
        JsonObject jobj = new Gson().fromJson(getContextAsString(response), JsonObject.class);
        System.out.println(response);
        String id = jobj.get("firstname").getAsString()+" "+jobj.get("lastname").getAsString();
        String mytoken = UUID.randomUUID().toString();
        Storage storage = new Storage();
        storage.putToken(mytoken, id);
        if(storage.getCoffee(id) == null)
            storage.putCoffee(id, 0);
        storage.close();
        TokenResponse t = new TokenResponse();
        t.setToken(mytoken);
        return Response
                .status(200)
                .type(MediaType.APPLICATION_JSON)
                .entity(t)
                .build();
    }

    @POST
    @Path("buyCoffee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buyCoffee(BuyCoffeeRequest buyCoffeeRequest) {

        Storage storage = new Storage();
        String name = storage.getNameFromToken(buyCoffeeRequest.getToken());
        if(name != null) {
            Integer nbCoffees = storage.getCoffee(name);
            if(nbCoffees > 0) {
                storage.putCoffee(name, nbCoffees - 1);
                storage.close();
                return Response
                        .status(200)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(StatusResponse.getOK())
                        .build();
            } else {
                storage.close();
                return Response
                        .status(403)
                        .type(MediaType.APPLICATION_JSON)
                        .entity(StatusResponse.getKO())
                        .build();
            }
        }
        storage.close();
        return Response
                .status(401)
                .type(MediaType.APPLICATION_JSON)
                .entity(StatusResponse.getKO())
                .build();
    }

    @POST
    @Path("chargeCoffee")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response chargeCoffee(ChargeCoffeeRequest chargeCoffeeRequest) {

        Storage storage = new Storage();
        String name = storage.getNameFromToken(chargeCoffeeRequest.getToken());
        if(name != null) {
            storage.putCoffee(name, storage.getCoffee(name)+chargeCoffeeRequest.getNbCoffee());
            storage.close();
            return Response
                    .status(200)
                    .type(MediaType.APPLICATION_JSON)
                    .entity(StatusResponse.getOK())
                    .build();
        }
        storage.close();
        return Response
                .status(401)
                .type(MediaType.APPLICATION_JSON)
                .entity(StatusResponse.getKO())
                .build();

    }

    protected static String getContextAsString(HttpResponse response) throws IOException {

        StringWriter writer = new StringWriter();
        InputStream inputStream = response.getEntity().getContent();
        try {
            IOUtils.copy(inputStream, writer, "UTF-8");
        } finally {
            inputStream.close();
        }
        return writer.toString();
    }

}

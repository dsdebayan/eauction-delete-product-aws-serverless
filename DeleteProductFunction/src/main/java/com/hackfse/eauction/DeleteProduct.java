package com.hackfse.eauction;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DeleteProduct {

   // AmazonSNS sns = AmazonSNSClientBuilder .defaultClient();

    public APIGatewayProxyResponseEvent checkHealth(APIGatewayProxyRequestEvent request){
        return  new APIGatewayProxyResponseEvent().withStatusCode(200).withBody("Deleted");
    }

    public APIGatewayProxyResponseEvent deleteProduct(APIGatewayProxyRequestEvent request)  {

        Logger logger = LoggerFactory.getLogger(DeleteProduct.class);
        HttpClient client = HttpClient.newHttpClient();
        logger.info("APIGatewayProxyRequestEvent request : " + request);
        String URL = System.getenv("WAREHOUSE_URL") + request.getPathParameters().get("productId");
        HttpRequest httpRequest = HttpRequest.newBuilder(
                URI.create(URL))
                .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQ=")
                .DELETE()

                .build();
        logger.info("http request : " + httpRequest);

        HttpResponse response = null;

// use the client to send the request
        try {
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e){
            logger.error("Exception is : " + e);
        }

        logger.info("http response : " + response);
        String message = response.body().toString().isEmpty() ?
                ("Product Id " + request.getPathParameters().get("productId") + " is deleted.")
                : response.body().toString();

        logger.info("message : " + message);

   //     sns.publish(System.getenv("PRODUCT_DELETED_TOPIC"), message);

// the response:

        return  new APIGatewayProxyResponseEvent().withStatusCode(response.statusCode()).withBody(message);
    }
}

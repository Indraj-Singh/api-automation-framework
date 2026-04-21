package factory;

import config.ConfigReader;
import filters.CustomFilter;
import filters.RetryFilter;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

public class RequestSpecFactory {


    public static RequestSpecification getNoAuthRequestSpec(){
        RequestSpecification request = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getInstance().getBaseUrl())
                .addHeader("Content-Type","application/json")
                .addFilter(new RetryFilter())
                .addFilter(new CustomFilter())
                .build();
        return request;
    }

    public static RequestSpecification getRequestSpec(){
        RequestSpecification request = new RequestSpecBuilder()
                .setBaseUri(ConfigReader.getInstance().getBaseUrl())
                .addFilter(new RetryFilter())
                .addFilter(new CustomFilter())
                .build();
        return request;
    }




}

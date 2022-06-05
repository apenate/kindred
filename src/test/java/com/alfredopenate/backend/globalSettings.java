package com.alfredopenate.backend;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.testng.annotations.BeforeClass;

import static io.restassured.specification.ProxySpecification.host;

public class globalSettings {
    @BeforeClass
    public void setup() {
        // Setting BaseURI
        RestAssured.baseURI = "https://www.unibet.co.uk";
        //Logging All Requests
        RestAssured.filters(new RequestLoggingFilter());
        //Logging All Responses
        RestAssured.filters(new ResponseLoggingFilter());
        //Proxy
        //RestAssured.proxy = host("127.0.0.1").withPort(8888);
    }
}

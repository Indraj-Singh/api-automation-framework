package tests;

import baseTest.BaseTest;
import config.ConfigReader;
import io.restassured.response.Response;
import models.Login;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import services.AuthService;

import java.util.HashMap;
import java.util.Map;

public class AuthTest extends BaseTest {


    @Test(description = "Verify that a valid user can successfully generate an authentication token using correct credentials")
    public void testValidLogin(){

        SoftAssert softAssert= new SoftAssert();
        String username =ConfigReader.getInstance().getUsername();
        String password = ConfigReader.getInstance().getPassword();
        Login login = new Login.Builder().setUsername(username).setPassword(password).build();
        Response response  = new AuthService().loginAuth(login);

        softAssert.assertEquals(response.statusCode(), 201, "Status code mismatch");
        softAssert.assertNotNull(response.path("token"), "Token is null");
        softAssert.assertFalse(response.path("token").toString().isEmpty(), "Token is empty");

        softAssert.assertAll();
    }

    @Test(description = "Verify that the API returns an error message when an incorrect password is provided for a valid username")
    public void testInvalidPasswordLogin(){
        SoftAssert softAssert= new SoftAssert();
        String username =ConfigReader.getInstance().getUsername();
        Login login = new Login.Builder().setUsername(username).setPassword("invalid").build();
        Response response  = new AuthService().loginAuth(login);

        softAssert.assertEquals(response.statusCode(), 401, "Status code mismatch");
        softAssert.assertEquals(response.asString(), "username or password is incorrect");

        softAssert.assertAll();
    }

    @Test(description = "Verify that the API returns an error response when the password field is missing in the request payload")
    public void testMissingPasswordLogin(){
        SoftAssert softAssert= new SoftAssert();
        String username =ConfigReader.getInstance().getUsername();

        Login login = new Login.Builder().setUsername(username).build();
        Response response  = new AuthService().loginAuth(login);

        softAssert.assertEquals(response.statusCode(), 400, "Status code mismatch");
        softAssert.assertEquals(response.asString(), "username and password are not provided in JSON format","Error message mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify that the API returns an error response when an empty request body is sent")
    public void testEmptyLoginRequest(){
        SoftAssert softAssert= new SoftAssert();
        Response response  = new AuthService().loginAuth("");

        softAssert.assertEquals(response.statusCode(), 400, "Status code mismatch");
        softAssert.assertEquals(response.asString(), "username and password are not provided in JSON format","Error message mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify that the API returns an error when invalid data types are provided in the authentication request payload")
    public void testInvalidPayloadLogin(){
        SoftAssert softAssert= new SoftAssert();
        Map<String,Object> userMap = new HashMap<>();
        userMap.put("username",ConfigReader.getInstance().getUsername());
        userMap.put("password",12345);

        Response response  = new AuthService().loginAuth(userMap);

        softAssert.assertEquals(response.statusCode(), 401, "Status code mismatch");
        softAssert.assertEquals(response.asString(), "username or password is incorrect","Error message mismatch");

        softAssert.assertAll();
    }
}

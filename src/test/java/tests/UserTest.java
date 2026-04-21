package tests;

import baseTest.BaseTest;
import dataproviders.TestDataProvider;
import io.restassured.response.Response;
import models.User;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import services.UserService;

import java.util.List;
import java.util.Map;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;


public class UserTest extends BaseTest {

    @Test(description = "Verify that all users are retrieved successfully and each user object contains required fields with valid response headers")
    public void testGetAllUsers(){

        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().getAllUsers();

        softAssert.assertEquals(response.statusCode(),200,"Mismatch in status code");
        softAssert.assertTrue(response.header("Content-Type").contains("application/json"),"Header Content-Type mismatch");
        List<Map<String, Object>> list = response.jsonPath().getList("");
        softAssert.assertFalse(list.isEmpty(),"User list is empty");

        for (Map<String, Object> user : list) {
            softAssert.assertTrue(user.containsKey("id"), "Missing id");
            softAssert.assertTrue(user.containsKey("email"), "Missing email");
            softAssert.assertTrue(user.containsKey("username"), "Missing username");
            softAssert.assertTrue(user.containsKey("password"), "Missing password");
            softAssert.assertTrue(user.containsKey("phone"), "Missing phone number");
            softAssert.assertTrue(user.containsKey("name"), "Missing name");
            softAssert.assertTrue(user.containsKey("address"), "Missing address");
        }
        softAssert.assertAll();
    }

    @Test(description = "Verify that a user can be retrieved using a valid user ID and the response matches the expected schema and nested field values")
    public void testGetUserByValidId(){

        int id =1;
        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().getUserById(id);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/userSchema.json"));

        var json = response.jsonPath();
        softAssert.assertEquals(response.statusCode(),200,"Mismatch in status code");

        softAssert.assertEquals(json.getInt("id"),id,"Id mismatch");

        softAssert.assertFalse(json.getString("address.geolocation.lat").isEmpty(),"Address geolocation lat empty");
        softAssert.assertFalse(json.getString("address.geolocation.long").isEmpty(),"Address geolocation long empty");
        softAssert.assertFalse(json.getString("address.city").isEmpty(),"Address city empty");
        softAssert.assertFalse(json.getString("address.street").isEmpty(),"Address street empty");
        softAssert.assertTrue(json.getInt("address.number")>=0,"Address number empty");
        softAssert.assertFalse(json.getString("address.zipcode").isEmpty(),"Address zipcode empty");

        softAssert.assertFalse(json.getString("email").isEmpty(),"Email empty");
        softAssert.assertFalse(json.getString("username").isEmpty(),"Username empty");
        softAssert.assertFalse(json.getString("password").isEmpty(),"Password empty");
        softAssert.assertFalse(json.getString("name.firstname").isEmpty(),"FirstName empty");
        softAssert.assertFalse(json.getString("name.lastname").isEmpty(),"LastName empty");
        softAssert.assertFalse(json.getString("phone").isEmpty(),"Phone number empty");
        softAssert.assertTrue(json.getInt("__v") >=0,"__v invalid");
        softAssert.assertAll();
    }

    @Test(description = "Verify API behavior when an invalid user ID is provided and ensure it returns an empty or null response without errors")
    public void testGetUserByInvalidId(){

        int id =99999;
        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().getUserById(id);
        softAssert.assertEquals(response.statusCode(),200,"Mismatch in status code");
        softAssert.assertTrue(
                response.asString().equals("{}") ||
                        response.asString().equals("null") ||
                        response.asString().isEmpty(),
                "Expected empty response for invalid user ID"
        );
        softAssert.assertAll();
    }

    @Test(description = "Verify that a user is created successfully with valid input data and the response contains a generated user ID",
            dataProvider = "CreateUserDetails",dataProviderClass = TestDataProvider.class)
    public void testCreateUserWithValidData(User user){

        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().createUser(user);

        softAssert.assertEquals(response.statusCode(),201,"Status code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "User ID is null");

        softAssert.assertAll();
    }

    @Test(description = "Verify that the API handles user creation with missing fields gracefully and returns a valid response without failure")
    public void testCreateUserWithMissingFields(){

        User user = new User.Builder()
                .setId(1)
                .setUserName("Test222")
                .setPassword("12345")
                .build();

        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().createUser(user);
        softAssert.assertEquals(response.statusCode(),201,"Status code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "User ID is null");

        softAssert.assertAll();

    }

    @Test(description = "Verify API behavior when an empty request body is sent during user creation and ensure no unexpected failure occurs")
    public void testCreateUserWithEmptyBody(){

        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().createUser("");
        softAssert.assertEquals(response.statusCode(),201,"Status code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "User ID is null");

        softAssert.assertAll();
    }

    @Test(description = "Verify that an existing user is updated successfully with valid data and updated fields are reflected in the response")
    public void testUpdateUserWithValidData(){
        int id =1;

        User user = new User.Builder()
                .setId(id)
                .setUserName("user1")
                .setPassword("pass1")
                .setEmail("user1@gmail.com")
                .build();
        SoftAssert softAssert= new SoftAssert();
        Response response = new UserService().updateUser(id,user);

        var json = response.jsonPath();
        softAssert.assertEquals(response.statusCode(),200,"Status code mismatch");
        softAssert.assertNotNull(json.get("id"), "User ID is null");

        softAssert.assertEquals(json.getString("username"),user.getUsername(),"Username mismatch");
        softAssert.assertEquals(json.getString("email"), user.getEmail(),"Email mismatch");
        softAssert.assertEquals(json.getString("password"),user.getPassword(),"Password mismatch");

        softAssert.assertAll();

    }

    @Test(description = "Verify that a user can be deleted successfully using a valid user ID")
    public void testDeleteUserByValidId(){

        SoftAssert softAssert= new SoftAssert();
        int id=1;
        Response response = new UserService().deleteUser(id);
        softAssert.assertEquals(response.statusCode(),200,"Status code mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify API behavior when attempting to delete a user with an invalid ID and ensure the request is handled safely")
    public void testDeleteUserByInvalidId(){
        SoftAssert softAssert= new SoftAssert();
        int id=99999;
        Response response = new UserService().deleteUser(id);
        softAssert.assertEquals(response.statusCode(),200,"Status code mismatch");

        softAssert.assertAll();

    }

}

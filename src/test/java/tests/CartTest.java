package tests;

import baseTest.BaseTest;
import dataproviders.TestDataProvider;
import io.restassured.response.Response;
import models.Cart;
import models.Product;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import services.CartService;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import java.util.List;
import java.util.Map;


public class CartTest extends BaseTest {

    @Test(description = "Verify that all carts are retrieved successfully and each cart contains required fields with valid response headers")
    public void testGetAllCarts(){

        SoftAssert softAssert = new SoftAssert();

        Response response = new CartService().getAllCart();

        softAssert.assertEquals(response.statusCode(),200,"Status Code mismatch");
        softAssert.assertTrue(response.header("Content-Type").contains("application/json"),"Header Content-Type mismatch");

        List<Map<String, Object>> list = response.jsonPath().getList("");
        softAssert.assertFalse(list.isEmpty(),"Cart list is empty");

        for (Map<String, Object> product : list) {
            softAssert.assertTrue(product.containsKey("id"), "Missing id");
            softAssert.assertTrue(product.containsKey("userId"), "Missing User id");
            softAssert.assertTrue(product.containsKey("date"), "Missing date");
            softAssert.assertTrue(product.containsKey("products"), "Missing products");
            softAssert.assertTrue(product.containsKey("__v"), "Missing __v");

        }
        softAssert.assertAll();
    }

    @Test(description = "Verify that a cart can be retrieved using a valid cart ID and the response matches the expected schema and field values")
    public void testGetCartByValidId(){

        SoftAssert softAssert = new SoftAssert();
        int id =1;
        Response response = new CartService().getSingleCart(id);

        response.then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/cartSchema.json"));

        var json = response.jsonPath();

        softAssert.assertEquals(response.statusCode(),200,"Status code mismatch");
        softAssert.assertEquals(json.getInt("id"),id,"Id mismatch");
        softAssert.assertTrue(json.getInt("userId") >=0,"User id invalid");
        softAssert.assertFalse(json.getString("date").isEmpty(),"Date missing");
        softAssert.assertTrue(json.getInt("__v")>=0,"Invalid __v");
        softAssert.assertFalse(json.getList("products").isEmpty(),"Products missing");

        softAssert.assertAll();
    }

    @Test(description = "Verify API behavior when an invalid cart ID is provided and ensure it returns an empty or null response without errors")
    public void testGetCartByInvalidId(){

        SoftAssert softAssert = new SoftAssert();
        int id =99999;
        Response response = new CartService().getSingleCart(id);
        softAssert.assertEquals(response.statusCode(),200,"Status code mismatch");

        softAssert.assertTrue(
                response.asString().equals("{}") ||
                        response.asString().equals("null") ||
                        response.asString().isEmpty(),
                "Expected empty response for invalid Cart ID"
        );

        softAssert.assertAll();

    }


    @Test(description = "Verify that a cart is created successfully with valid input data and the response contains product details as per API contract",
            dataProvider = "CreateCartDetails",dataProviderClass = TestDataProvider.class)
    public void testCreateCartWithValidData(Cart cart){

        SoftAssert softAssert = new SoftAssert();

        Response response = new CartService().createCart(cart);
        softAssert.assertEquals(response.statusCode(),201,"Status Code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "Cart ID is null");
        var json = response.jsonPath();

        softAssert.assertFalse(json.getList("products").isEmpty(),"Products missing ");

        List<Map<String, Object>> products = response.jsonPath().getList("products");
        softAssert.assertEquals(products.size(),1,"Expected only one product");

        Map<String,Object> map = products.get(0);
        softAssert.assertEquals(map.get("id"),cart.getProducts().get(0).getId(),"Product is mismatch");
        softAssert.assertEquals(map.get("title"),cart.getProducts().get(0).getTitle(),"Product title mismatch");
        softAssert.assertEquals(Double.parseDouble(map.get("price").toString()),cart.getProducts().get(0).getPrice(),"Product price mismatch");
        softAssert.assertEquals(map.get("description"),cart.getProducts().get(0).getDescription(),"Product description mismatch");
        softAssert.assertEquals(map.get("category"),cart.getProducts().get(0).getCategory(),"Product category mismatch");
        softAssert.assertEquals(map.get("image"),cart.getProducts().get(0).getImage(),"Product image mismatch");

        softAssert.assertAll();

    }

    @Test(description = "Verify that the API handles cart creation with missing fields gracefully and returns a valid response without failure")
    public void testCreateCartWithMissingFields(){

        SoftAssert softAssert = new SoftAssert();
        Cart cart = new Cart.Builder()
                .setId(11)
                .setUserId(3)
                .build();

        Response response = new CartService().createCart(cart);
        softAssert.assertEquals(response.statusCode(),201,"Status Code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "Cart ID is null");
        softAssert.assertEquals(response.jsonPath().getInt("userId"),cart.getUserId(),"User Id mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify API behavior when an empty request body is sent during cart creation and ensure no unexpected failure occurs")
    public void testCreateCartWithEmptyBody(){

        SoftAssert softAssert = new SoftAssert();
        Response response = new CartService().createCart("");

        softAssert.assertEquals(response.statusCode(),201,"Status Code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "Cart ID is null");
        softAssert.assertAll();
    }

    @Test(description = "Verify that an existing cart is updated successfully with valid data and updated product details are reflected in the response")
    public void testUpdateCartWithValidData(){

        int id =1;
        Product product1= new Product.Builder()
                .setId(1)
                .setTitle("T-Shirt")
                .setPrice(1000.0)
                .setCategory("Cotton ")
                .setImage("http://test.com")
                .build();

        Cart cart = new Cart.Builder()
                .setId(id)
                .setUserId(3)
                .setProducts(List.of(product1))
                .build();

        SoftAssert softAssert = new SoftAssert();

        Response response = new CartService().updateCart(id,cart);

        softAssert.assertEquals(response.statusCode(),200, "Status code mismatch");
        var json = response.jsonPath();

        softAssert.assertEquals(json.getInt("id"),cart.getId(),"Id mismatch");
        softAssert.assertEquals(json.getInt("userId"),cart.getUserId(),"User Id mismatch");
        softAssert.assertFalse(json.getList("products").isEmpty(),"Products missing ");

        List<Map<String, Object>> products = response.jsonPath().getList("products");
        softAssert.assertEquals(products.size(),1,"Expected only one product");

        Map<String,Object> map = products.get(0);
        softAssert.assertEquals(map.get("id"),product1.getId(),"Product is mismatch");
        softAssert.assertEquals(map.get("title"),product1.getTitle(),"Product title mismatch");
        softAssert.assertEquals(Double.parseDouble(map.get("price").toString()),product1.getPrice(),"Product price mismatch");
        softAssert.assertEquals(map.get("description"),product1.getDescription(),"Product description mismatch");
        softAssert.assertEquals(map.get("category"),product1.getCategory(),"Product category mismatch");
        softAssert.assertEquals(map.get("image"),product1.getImage(),"Product image mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify that a cart can be deleted successfully using a valid cart ID")
    public void testDeleteCartByValidId(){

        int id = 1;

        SoftAssert softAssert = new SoftAssert();
        Response response = new CartService().deleteCart(id);
        softAssert.assertEquals(response.statusCode(),200,"Status Code mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify API behavior when attempting to delete a cart with an invalid ID and ensure the request is handled safely")
    public void testDeleteCartByInvalidId(){
        int id = 99999;

        SoftAssert softAssert = new SoftAssert();
        Response response = new CartService().deleteCart(id);
        softAssert.assertEquals(response.statusCode(),200,"Status Code mismatch");

        softAssert.assertAll();
    }
}

package tests;

import baseTest.BaseTest;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.testng.Assert.assertEquals;

import dataproviders.TestDataProvider;
import io.restassured.response.Response;
import models.Product;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import services.ProductService;

import java.util.List;
import java.util.Map;

public class ProductTest extends BaseTest {

    @Test(description = "Verify that all products are retrieved successfully and each product contains required fields with valid response headers")
    public void testGetAllProducts(){

        SoftAssert softAssert = new SoftAssert();
        ProductService productService= new ProductService();
        Response response = productService.getAllProducts();

        softAssert.assertEquals(response.statusCode(),200,"Mismatch in status code");
        softAssert.assertTrue(response.header("Content-Type").contains("application/json"),"Header Content-Type mismatch");
        List<Map<String, Object>> list = response.jsonPath().getList("");
        softAssert.assertFalse(list.isEmpty(),"Product list is empty");

        for (Map<String, Object> product : list) {
            softAssert.assertTrue(product.containsKey("id"), "Missing id");
            softAssert.assertTrue(product.containsKey("title"), "Missing title");
            softAssert.assertTrue(product.containsKey("price"), "Missing price");
            softAssert.assertTrue(product.containsKey("description"), "Missing description");
            softAssert.assertTrue(product.containsKey("category"), "Missing category");
            softAssert.assertTrue(product.containsKey("image"), "Missing image");
            softAssert.assertTrue(product.containsKey("rating"), "Missing rating");
        }
        softAssert.assertAll();
    }

    @Test(description = "Verify that a product can be retrieved using a valid product ID and the response matches the expected schema and field values")
    public void testGetProductByValidId(){
        SoftAssert softAssert = new SoftAssert();
        int id =1;
        ProductService productService= new ProductService();
        Response response = productService.getProductById(id);

        response
                .then()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/productSchema.json"));


        softAssert.assertEquals(response.statusCode(),200,"Mismatch in status code");
        softAssert.assertEquals(response.jsonPath().getInt("id"),1,"Id mismatch");
        softAssert.assertFalse(response.jsonPath().getString("title").isEmpty());
        softAssert.assertTrue(response.jsonPath().getDouble("price")>=0,"Invalid price");
        softAssert.assertFalse(response.jsonPath().getString("description").isEmpty(),"Description empty");
        softAssert.assertFalse(response.jsonPath().getString("category").isEmpty(),"Category empty");
        softAssert.assertFalse(response.jsonPath().getString("image").isEmpty(),"Image empty");
        softAssert.assertTrue(response.jsonPath().getDouble("rating.rate")>=0,"Invalid rating");
        softAssert.assertTrue(response.jsonPath().getInt("rating.count") >= 0, "Invalid rating count");
        softAssert.assertAll();
    }

    @Test(description = "Verify API behavior when an invalid product ID is provided and ensure it returns an empty or null response without errors")
    public void testGetProductByInvalidId(){

        SoftAssert softAssert = new SoftAssert();
        int id =99999;
        ProductService productService= new ProductService();
        Response response = productService.getProductById(id);

        softAssert.assertEquals(response.statusCode(),200,"Mismatch in status code");
        softAssert.assertTrue(
                response.asString().equals("{}") ||
                        response.asString().equals("null") ||
                        response.asString().isEmpty(),
                "Expected empty response for invalid product ID"
        );
        softAssert.assertAll();
    }


    @Test(description = "Verify that a product is created successfully with valid input data and response fields match the request payload",dataProvider = "CreateProductDetails",dataProviderClass = TestDataProvider.class)
    public void testCreateProductWithValidData(Product product){

        SoftAssert softAssert = new SoftAssert();

        ProductService productService= new ProductService();
        Response response = productService.createProduct(product);

        softAssert.assertEquals(response.statusCode(),201,"Status code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "Product ID is null");
        softAssert.assertEquals(response.jsonPath().getString("title"),product.getTitle(),"Title mismatch");
        softAssert.assertEquals(response.jsonPath().getDouble("price"),product.getPrice(),"Price mismatch");
        softAssert.assertEquals(response.jsonPath().getString("description"),product.getDescription(),"Description mismatch");
        softAssert.assertEquals(response.jsonPath().getString("image"),product.getImage(),"Image url mismatch");
        softAssert.assertEquals(response.jsonPath().getString("category"),product.getCategory(),"Category mismatch");

        softAssert.assertAll();
    }

    @Test(description = "Verify that the API handles product creation with missing fields gracefully and returns a valid response without failure")
    public void testCreateProductWithMissingFields(){

        SoftAssert softAssert = new SoftAssert();

        Product product1= new Product.Builder()
                .setId(1)
                .setTitle("T-Shirt")
                .setPrice(1000.0)
                .setCategory("Cotton ")
                .setImage("http://test.com")
                .build();

        ProductService productService= new ProductService();
        Response response = productService.createProduct(product1);

        softAssert.assertEquals(response.statusCode(),201,"Status code mismatch");
        softAssert.assertNotNull(response.jsonPath().get("id"), "Product ID is null");
        softAssert.assertEquals(response.jsonPath().getString("title"),product1.getTitle(),"Title mismatch");
        softAssert.assertEquals(response.jsonPath().getDouble("price"),product1.getPrice(),"Price mismatch");
        softAssert.assertEquals(response.jsonPath().getString("image"),product1.getImage(),"Image url mismatch");
        softAssert.assertEquals(response.jsonPath().getString("category"),product1.getCategory(),"Category mismatch");
        softAssert.assertTrue(
                response.jsonPath().get("description") == null ||
                        response.jsonPath().getString("description").isEmpty(),
                "Description should be null or empty"
        );

        softAssert.assertAll();

    }

    @Test(description = "Verify API behavior when an empty request body is sent during product creation and ensure no unexpected failure occurs")
    public void testCreateProductWithEmptyBody(){
        SoftAssert softAssert = new SoftAssert();

        ProductService productService= new ProductService();
        Response response = productService.createProduct("");

        var json = response.jsonPath();
        softAssert.assertEquals(response.statusCode(),201,"Status code mismatch");
        softAssert.assertNotNull(json.get("id"), "Product ID is null");
        softAssert.assertTrue(json.get("title") == null || json.getString("title").isEmpty(),
                "Title should be null or empty"
        );
        softAssert.assertTrue(json.get("price") == null || json.getDouble("price")>=0,
                "Description should be null or empty"
        );
        softAssert.assertTrue(json.get("description") == null || json.getString("description").isEmpty(),
                "Description should be null or empty"
        );
        softAssert.assertTrue(json.get("category") == null || json.getString("category").isEmpty(),
                "Category should be null or empty"
        );
        softAssert.assertTrue(json.get("image") == null || json.getString("image").isEmpty(),
                "image should be null or empty"
        );

        softAssert.assertAll();
    }

    @Test(description = "Verify that an existing product is updated successfully with valid data and updated fields are reflected in the response")
    public void testUpdateCartWithValidData(){
        SoftAssert softAssert = new SoftAssert();
        int id =1;
        Product product1= new Product.Builder()
                .setTitle("TV")
                .setPrice(10000.0)
                .setCategory("Electronic")
                .setDescription("Android Tv")
                .setImage("http://test.com")
                .build();

        ProductService productService= new ProductService();
        Response response = productService.updateProduct(id,product1);

        var json = response.jsonPath();
        softAssert.assertEquals(response.statusCode(),200,"Status code mismatch");
        softAssert.assertEquals(json.getInt("id"),id, "Product ID not matching");
        softAssert.assertEquals(json.getString("title"),product1.getTitle(),"Title mismatch");
        softAssert.assertEquals(json.getDouble("price"),product1.getPrice(),"Price mismatch");
        softAssert.assertEquals(json.getString("description"),product1.getDescription(),"Description mismatch");
        softAssert.assertEquals(json.getString("image"),product1.getImage(),"Image url mismatch");
        softAssert.assertEquals(json.getString("category"),product1.getCategory(),"Category mismatch");

        softAssert.assertAll();

    }

    @Test(description = "Verify that a product can be deleted successfully using a valid product ID")
    public void testDeleteCartByValidId(){

        int id =1;

        ProductService productService= new ProductService();
        Response response = productService.deleteProduct(id);
        assertEquals(response.statusCode(),200,"Status code mismatch");

    }

    @Test(description = "Verify API behavior when attempting to delete a product with an invalid ID and ensure it handles the request safely")
    public void testDeleteCartByInvalidId(){

        int id =99999;

        ProductService productService= new ProductService();
        Response response = productService.deleteProduct(id);
        assertEquals(response.statusCode(),200,"Status code mismatch");
    }
}

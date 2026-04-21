package dataproviders;


import models.Cart;
import models.Product;
import models.User;
import org.testng.annotations.DataProvider;

import java.util.Arrays;

public class TestDataProvider {

    @DataProvider(name="CreateProductDetails")
    public static Object[][] getProductData(){
        Product product1= new Product.Builder()
                .setId(1)
                .setTitle("Shoes")
                .setPrice(1000.550)
                .setDescription("White shoes")
                .setCategory("Sports")
                .setImage("http://example1.com")
                .build();

        Product product2= new Product.Builder()
                .setId(2)
                .setTitle("Shirt")
                .setPrice(32.5)
                .setDescription("Cotton shirt")
                .setCategory("Shirt")
                .setImage("http://example.com")
                .build();

        Product product3= new Product.Builder()
                .setId(4)
                .setTitle("watch")
                .setPrice(5648.0)
                .setDescription("Metal strip watch")
                .setCategory("smartwatch")
                .setImage("http://example2.com")
                .build();
        return new Object[][]{
                {product1},
                {product2},
                {product3}
        };
    }

    @DataProvider(name="CreateCartDetails")
    public static Object[][] getCartData(){
        Product product1= new Product.Builder()
                .setId(4)
                .setTitle("Laptop")
                .setPrice(55000.0)
                .setDescription("Gaming laptop")
                .setCategory("electronics")
                .setImage("http://image.com/laptop")
                .build();

        Product product2= new Product.Builder()
                .setId(5)
                .setTitle("Phone")
                .setPrice(20000.0)
                .setDescription("Smartphone")
                .setCategory("electronics")
                .setImage("http://image.com/phone")
                .build();

        Cart cart1= new Cart.Builder()
                .setId(1)
                .setUserId(101)
                .setProducts(Arrays.asList(product1)).build();
        Cart cart2= new Cart.Builder()
                .setId(2)
                .setUserId(102)
                .setProducts(Arrays.asList(product2)).build();

        return new Object[][] {
                { cart1 },
                { cart2 }
        };
    }

    @DataProvider(name="CreateUserDetails")
    public static Object[][] getUserData(){

        User user1= new User.Builder()
                .setId(1)
                .setUserName("Test1")
                .setEmail("Test1@gmail.com")
                .setPassword("Test1@123")
                .build();
        User user2= new User.Builder()
                .setId(2)
                .setUserName("Test 2")
                .setEmail("Test_@gmail.com")
                .setPassword("Test_2 3")
                .build();
        User user3= new User.Builder()
                .setId(3)
                .setUserName("test3")
                .setEmail("Test@3@gmail.com")
                .setPassword("test@3@123")
                .build();
        User user4= new User.Builder()
                .setId(4)
                .setUserName("Test4")
                .setEmail("Test4@gmail.com")
                .setPassword("Test4@123")
                .build();
        return new Object[][]{
                { user1 },
                { user2 },
                { user3 },
                { user4 }
        };
    }


}

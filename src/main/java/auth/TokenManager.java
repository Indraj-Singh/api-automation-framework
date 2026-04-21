package auth;

import config.ConfigReader;
import constants.EndPoints;
import io.restassured.response.Response;
import models.Login;

import static factory.RequestSpecFactory.getNoAuthRequestSpec;
import static io.restassured.RestAssured.given;


public class TokenManager {

    private static volatile TokenManager instance;
    private volatile String token;

    private TokenManager(){
        generateToken();
    }

    public static TokenManager getInstance(){
        if(instance == null){
            synchronized (TokenManager.class){
                if(instance == null){
                    instance = new TokenManager();
                }
            }

        }
        return instance;
    }

    public String getToken(){
        if(token==null || token.isEmpty()){
            synchronized (this){
                if(token==null || token.isEmpty()){
                    generateToken();
                }
            }
        }
        return token;
    }

    private void generateToken(){

        Login login= new Login.Builder().setUsername(ConfigReader.getInstance().getUsername())
                .setPassword(ConfigReader.getInstance().getPassword())
                .build();

        Response response = given()
                .spec(getNoAuthRequestSpec())
                .log().all()
                .body(login)
                .post(EndPoints.AUTH);

        if (response.statusCode() != 201) {
            throw new RuntimeException("Token API failed: " + response.asString());
        }

        String newToken = response.jsonPath().getString("token");

        if (newToken == null || newToken.isEmpty()) {
            throw new RuntimeException("Token is null or empty");
        }

        token = newToken;
    }

}

package baseService;

import factory.RequestSpecFactory;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BaseService {

    protected Response get(String endpoint) {
        return given()
                .spec(RequestSpecFactory.getRequestSpec())
                .when()
                .get(endpoint);
    }


    protected Response post(String endpoint, Object body) {
        return given()
                .spec(RequestSpecFactory.getNoAuthRequestSpec())
                .body(body)
                .when()
                .post(endpoint);
    }

    protected Response put(String endpoint, Object body) {
        return given()
                .spec(RequestSpecFactory.getNoAuthRequestSpec())
                .body(body)
                .when()
                .put(endpoint);
    }

    protected Response delete(String endpoint) {
        return given()
                .spec(RequestSpecFactory.getRequestSpec())
                .when()
                .delete(endpoint);
    }
}
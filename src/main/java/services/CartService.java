package services;

import baseService.BaseService;
import constants.EndPoints;
import io.restassured.response.Response;

public class CartService extends BaseService {

    public Response createCart(Object body) {
        return post(EndPoints.CART, body);
    }

    public Response getSingleCart(int id) {
        return get(EndPoints.CART+ "/"+id);
    }

    public Response getAllCart() {
        return get(EndPoints.CART);
    }

    public Response updateCart(int id ,Object body) {
        return put(EndPoints.CART+"/"+id, body);
    }

    public Response deleteCart(int id) {
        return delete(EndPoints.CART+"/"+id);
    }

}

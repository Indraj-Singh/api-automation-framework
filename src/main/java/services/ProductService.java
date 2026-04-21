package services;

import baseService.BaseService;
import constants.EndPoints;
import io.restassured.response.Response;
import models.Product;

public class ProductService extends BaseService {

    public Response getAllProducts(){
        return get(EndPoints.PRODUCT);
    }

    public Response getProductById(int id){
        return get(EndPoints.PRODUCT+"/"+id);
    }

    public Response createProduct(Object object){
        return post(EndPoints.PRODUCT,object);
    }

    public Response updateProduct(int productid ,Product product){
        return put(EndPoints.PRODUCT+"/"+productid,product);
    }

    public Response deleteProduct(int id){
        return delete(EndPoints.PRODUCT+"/"+id);
    }
}

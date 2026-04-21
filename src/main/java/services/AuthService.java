package services;

import baseService.BaseService;
import constants.EndPoints;
import io.restassured.response.Response;

public class AuthService extends BaseService {

    public Response loginAuth(Object object){
        return post(EndPoints.AUTH,object);
    }
}

package services;

import baseService.BaseService;
import constants.EndPoints;
import io.restassured.response.Response;
import models.User;

public class UserService extends BaseService {

    public Response getAllUsers(){
        return get(EndPoints.USERS);
    }

    public Response getUserById(int id){
        return get(EndPoints.USERS+"/"+id);
    }

    public Response createUser(Object object){
        return post(EndPoints.USERS,object);
    }

    public Response updateUser(int id ,User user){
        return put(EndPoints.USERS+"/"+id,user);
    }

    public Response deleteUser(int id){
        return delete(EndPoints.USERS+"/"+id);
    }
}

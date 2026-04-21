package models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

public class Cart {

    private int id;
    private int userId;
    private List<Product> products;

    private Cart(Builder builder){
        this.id=builder.id;
        this.userId= builder.userId;;
        this.products=builder.products;

    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public List<Product> getProducts() {
        return products;
    }

    public static class Builder{
        private int id;
        private int userId;
        private List<Product> products;

        public Builder setId(int id){
            this.id=id;
            return this;
        }

        public Builder setUserId(int userId){
            this.userId=userId;
            return this;
        }

        public Builder setProducts(List<Product> products){
            this.products=products;
            return this;
        }

        public Cart build(){
            return new Cart(this);
        }
    }
}

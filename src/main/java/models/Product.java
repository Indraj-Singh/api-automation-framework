package models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Product {
    private int id;
    private String title;
    private double price;
    private String description;
    private String category;
    private String image;

    private Product(Builder builder){
        this.id=builder.id;
        this.title=builder.title;
        this.price = builder.price;
        this.description=builder.description;
        this.category=builder.category;
        this.image= builder.image;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getDescription() {
        return description;
    }

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public static class Builder{
        private int id;
        private String title;
        private double price;
        private String description;
        private String category;
        private String image;

        public Builder setId(int id){
            this.id=id;
            return this;
        }


        public Builder setTitle(String title){
            this.title=title;
            return this;
        }

        public Builder setPrice(double price){
            this.price=price;
            return this;
        }

        public Builder setDescription(String description){
            this.description=description;
            return this;
        }

        public Builder setCategory(String category){
            this.category=category;
            return this;
        }

        public Builder setImage(String image){
            this.image=image;
            return this;
        }

        public Product build(){
            return new Product(this);
        }
    }
}

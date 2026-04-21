package models;


import com.fasterxml.jackson.annotation.JsonInclude;


public class User {

    private int id;
    private String username;
    private String email;
    private String password;

    private User(Builder builder) {
        this.id=builder.id;
        this.username=builder.username;
        this.email=builder.email;
        this.password=builder.password;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public static class Builder {
        private int id;
        private String username;
        private String email;
        private String password;

        public Builder setId(int id){
            this.id=id;
            return this;
        }

        public Builder setUserName(String username){
            this.username=username;
            return this;
        }

        public Builder setEmail(String email){
            this.email=email;
            return this;
        }

        public Builder setPassword(String password){
            this.password=password;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}

package models;

import com.fasterxml.jackson.annotation.JsonInclude;

public class Login {

    private String username;
    private String password;

    private Login(Builder builder){
        this.username=builder.username;
        this.password=builder.password;
    }

    public String getUsername() {
        return username;
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String getPassword() {
        return password;
    }

    public static class Builder{
        private String username;
        private String password;

        public Builder setUsername(String username){
            this.username=username;
            return this;
        }
        public Builder setPassword(String password){
            this.password=password;
            return this;
        }

        public Login build(){
            return new Login(this);
        }
    }
}

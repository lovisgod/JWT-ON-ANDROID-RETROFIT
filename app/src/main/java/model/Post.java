package model;

public class Post {
    private String name;
    private String password;
    private String about;
    private String token;

    public Post(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public Post(String name, String password, String about){
        this.name = name;
        this.password = password;
        this.about = about;
    }



    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getAbout() {
        return about;
    }

    public String getToken() {
        return token;
    }
}

package taembe.example.blackwine.taembe.model.stores.user;

/**
 * Created by Blackwine on 3/2/2017.
 */

public class User {
    private String name, id, facebook_user;
    private boolean login;

    public String getFacebook_user() {
        return facebook_user;
    }

    public void setFacebook_user(String facebook_user) {
        this.facebook_user = facebook_user;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

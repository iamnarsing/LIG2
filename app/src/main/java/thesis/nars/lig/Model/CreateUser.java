package thesis.nars.lig.Model;

/**
 * Created by nars on 12/16/2019.
 */

public class CreateUser {
    public CreateUser()
    {}

    public String username;
    public String password;

    public CreateUser(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

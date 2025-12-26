package school.hei;

public class DBConnection {
    public String URL;
    public String user;
    public String password;

    public DBConnection(String URL, String user, String password) {
        this.URL = URL;
        this.user = user;
        this.password = password;
    }

}

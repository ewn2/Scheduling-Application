package sample.Model;

public class User {
    private String username;

    public User(){
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String logFile = "login_activity.txt";

    public void loginAttempt(String username, String date, String time, boolean valid){
        //Write to a file if it exists, otherwise create it first. Lookup Java file I/O in the morning.
    }
}

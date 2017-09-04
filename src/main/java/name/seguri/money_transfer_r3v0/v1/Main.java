package name.seguri.money_transfer_r3v0.v1;

import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        port(getHerokuAssignedPort());
        get("/hello", (req, res) -> "Hello World");
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        // Return default port if heroku-port isn't set (i.e. on localhost)
        return 4567;
    }
}
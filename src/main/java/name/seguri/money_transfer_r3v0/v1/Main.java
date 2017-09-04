package name.seguri.money_transfer_r3v0.v1;

import com.google.gson.Gson;
import name.seguri.money_transfer_r3v0.v1.dao.AccountDao;
import name.seguri.money_transfer_r3v0.v1.dao.ErrorDao;
import name.seguri.money_transfer_r3v0.v1.dao.TransferDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;

public class Main {

    private static final Logger L = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        // Set the port we will listen to
        port(getHerokuAssignedPort());
        // Set Content-Type for each response
        before((req, res) -> res.type("application/json"));
        // Define endpoints
        setEndpoints();
        // Finally, log the request and the response
        afterAfter("/*", (req, res) -> L.info("{} \"{} {}\" {} {}", req.ip(), req.requestMethod(), req.uri(), req.protocol(), res.status()));
    }

    private static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        // Return default port if heroku-port isn't set (i.e. on localhost)
        return 4567;
    }

    private static void setEndpoints() {
        Gson gson = new Gson();
        // I tried to build modular routes with `path("/api/v1", () -> { path("/accounts", () -> {}) })`
        // but those routes return 404, so in this demo I'll be verbose and declare all routes.
        // Moreover the framework doesn't manage trailing slash.
        post("/api/v1/accounts"        , "application/json", AccountDao::addAccount   , gson::toJson);
        post("/api/v1/accounts/"       , "application/json", AccountDao::addAccount   , gson::toJson);
        get ("/api/v1/accounts"        , "application/json", AccountDao::getAccounts  , gson::toJson);
        get ("/api/v1/accounts/"       , "application/json", AccountDao::getAccounts  , gson::toJson);
        get ("/api/v1/accounts/:uuid"  , "application/json", AccountDao::getAccount   , gson::toJson);
        get ("/api/v1/accounts/:uuid/" , "application/json", AccountDao::getAccount   , gson::toJson);
        post("/api/v1/transfers"       , "application/json", TransferDao::addTransfer , gson::toJson);
        post("/api/v1/transfers/"      , "application/json", TransferDao::addTransfer , gson::toJson);
        get ("/api/v1/transfers"       , "application/json", TransferDao::getTransfers, gson::toJson);
        get ("/api/v1/transfers/"      , "application/json", TransferDao::getTransfers, gson::toJson);
        get ("/api/v1/transfers/:uuid" , "application/json", TransferDao::getTransfer , gson::toJson);
        get ("/api/v1/transfers/:uuid/", "application/json", TransferDao::getTransfer , gson::toJson);
        // Common errors management
        notFound(ErrorDao::notFound);
        internalServerError(ErrorDao::internalServerError);
    }
}
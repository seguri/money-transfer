package name.seguri.money_transfer_r3v0.v1.dao;

import com.google.gson.Gson;
import name.seguri.money_transfer_r3v0.v1.models.Account;
import name.seguri.money_transfer_r3v0.v1.models.Error;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    private static final List<Account> DATA = new ArrayList<>();

    public static Object addAccount(final Request request, final Response response) {
        // Get a partial Account model from payload
        final Account data = new Gson().fromJson(request.body(), Account.class);
        if (data == null) {
            return Error.from(400);
        }
        // Return a model completed with uuid
        final Account account = Account.from(data);
        // Add to memory db
        DATA.add(account);
        return account;
    }

    public static List<Account> getAccounts(final Request request, final Response response) {
        return DATA;
    }

    public static Object getAccount(final Request request, final Response response) {
        // Get parameter from user request
        final String uuid = request.params("uuid");
        // Return specific account
        final Account account =  getAccount(uuid);
        if (account == null) {
            return Error.from(404);
        }
        return account;
    }

    public static Account getAccount(final String uuid) {
        for (Account account: DATA) {
            if (account.getUuid().equalsIgnoreCase(uuid)) {
                return account;
            }
        }
        return null;
    }

    public static boolean update(final Account account) {
        for (Account persistedAccount: DATA) {
            if (persistedAccount.getUuid().equalsIgnoreCase(account.getUuid())) {
                // Update balance
                persistedAccount.setBalance(account.getBalance());
                // Operation succeeded
                return true;
            }
        }
        return false;
    }
}

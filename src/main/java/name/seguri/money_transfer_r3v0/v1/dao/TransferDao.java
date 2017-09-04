package name.seguri.money_transfer_r3v0.v1.dao;

import com.google.gson.Gson;
import name.seguri.money_transfer_r3v0.v1.models.Account;
import name.seguri.money_transfer_r3v0.v1.models.Error;
import name.seguri.money_transfer_r3v0.v1.models.Transfer;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

public class TransferDao {

    private static final List<Transfer> DATA = new ArrayList<>();

    public static Object addTransfer(final Request request, final Response response) {
        // Get a partial Transfer model from payload
        final Transfer data = new Gson().fromJson(request.body(), Transfer.class);
        if (data == null) {
            return Error.from(400);
        }
        // Start "transaction"
        // Retrieve the accounts to be updated
        final Account from = AccountDao.getAccount(data.getFrom());
        final Account to = AccountDao.getAccount(data.getTo());
        if (from == null || to == null) {
            return Error.from(404);
        }
        // Save original balance
        final int fromBalance = from.getBalance();
        final int toBalance = to.getBalance();
        // Update balance
        from.setBalance(fromBalance - data.getAmount());
        to.setBalance(toBalance + data.getAmount());
        // Update accounts
        if (AccountDao.update(from) && AccountDao.update(to)) {
            // Create a model complete with uuid
            final Transfer transfer = Transfer.from(data);
            // Add to memory db
            DATA.add(transfer);
            return transfer;
        } else {
            // Restore original balances
            from.setBalance(fromBalance);
            to.setBalance(toBalance);
            // Rollback
            AccountDao.update(from);
            AccountDao.update(to);
            return Error.from(500);
        }
    }

    public static List<Transfer> getTransfers(final Request request, final Response response) {
        return DATA;
    }

    public static Object getTransfer(final Request request, final Response response) {
        // Get parameter from user request
        final String uuid = request.params("uuid");
        // Return specifc transfer
        final Transfer transfer = getTransfer(uuid);
        if (transfer == null) {
            return Error.from(404);
        }
        return transfer;
    }

    public static Transfer getTransfer(final String uuid) {
        for (Transfer transfer: DATA) {
            if (transfer.getUuid().equalsIgnoreCase(uuid)) {
                return transfer;
            }
        }
        return null;
    }
}

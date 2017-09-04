package name.seguri.money_transfer_r3v0.v1.models;

import java.util.UUID;

public class Account {

    private final String uuid;
    private String email;
    private int balance;

    public Account(final String email, final int balance) {
        this.uuid = UUID.randomUUID().toString();
        this.email = email;
        this.balance = balance;
    }

    public String getUuid() {
        return uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(final int balance) {
        this.balance = balance;
    }

    public static Account from(final Account data) {
        return new Account(data.getEmail(), data.getBalance());
    }

    @Override
    public String toString() {
        return "Account{" +
                "uuid=" + uuid +
                ", email='" + email + '\'' +
                ", balance=" + balance +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Account that = (Account) obj;
        if (!uuid.equalsIgnoreCase(that.uuid)) {
            return false;
        }
        if (!email.equalsIgnoreCase(that.email)) {
            return false;
        }
        if (balance != that.balance) {
            return false;
        }
        return true;
    }
}

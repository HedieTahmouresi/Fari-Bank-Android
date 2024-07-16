package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import ir.ac.kntu.util.Calendar;

import java.text.DecimalFormat;
import java.time.DateTimeException;
import java.time.Instant;
import java.util.*;

public class Account {
    private CreditCard creditCard;
    private String accountId;
    private double balance;
    private SimpleUser owner;
    private List<Transaction> transactions;
    private List<Recent> recentList;

    public List<Recent> getRecentList() {
        return recentList;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public SimpleUser getOwner() {
        return owner;
    }

    public void setOwner(SimpleUser owner) {
        this.owner = owner;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Account(SimpleUser owner, NeoBank neoBank) {
        setOwner(owner);
        setBalance(0);
        transactions = new ArrayList<>();
        Random random = new Random();
        String mask1 = "0000000";
        DecimalFormat df1 = new DecimalFormat(mask1);
        String mask2 = "000000";
        DecimalFormat df2 = new DecimalFormat(mask2);
        int accountId1;
        int accountId2;
        String accountIdString;
        do {
            accountId1 = random.nextInt(10000000);
            accountId2 = random.nextInt(1000000);
            accountIdString = df1.format(accountId1).concat(df2.format(accountId2));
        } while (neoBank.getBankData().existsAccountID(accountIdString));
        setAccountId(accountIdString);
        setCreditCard(new CreditCard(neoBank));
        recentList = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(0, transaction);
    }

    public void addRecent(TransferTransaction transaction, String phoneNumber, NeoBank neoBank) {
        Recent newRecent = new Recent(neoBank.getBankData().getUserByPhone(phoneNumber), transaction.isByContact());
        for (Recent recent : recentList) {
            if (recent.equals(newRecent)) {
                return;
            }
        }
        recentList.add(newRecent);
    }

    public void addRecentCentral(TransferTransaction transaction, String phoneNumber, CentralBank centralBank) {
        Recent newRecent = new Recent(centralBank.getUserBySim(phoneNumber), transaction.isByContact());
        for (Recent recent : recentList) {
            if (recent.equals(newRecent)) {
                return;
            }
        }
        recentList.add(newRecent);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void chargeAccount(NeoBank neoBank, String answer, Context context) {
        this.setBalance(this.getBalance() + Double.parseDouble(answer));
        this.addTransaction(new ChargeTransaction(Double.parseDouble(answer), neoBank.getTracingNumber(), this.getBalance()));
        neoBank.setTracingNumber(neoBank.getTracingNumber() + 1);
        Toast.makeText(context, "Account successfully charged!", Toast.LENGTH_SHORT).show();
    }

    public void transfer(String value, SimpleUser receiver, List<Boolean> facts, Context context) {
        double remains = this.getOwner().isHasRemainsFund() ? this.getOwner().getRemainsFund().calculateRemains(value) : 0;
        if (Double.parseDouble(value) + MainActivity.getFariBank().getManagerData().getFariWage() + remains > this.getBalance()) {
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.setBalance(this.getBalance() - Double.parseDouble(value) - MainActivity.getFariBank().getManagerData().getFariWage() - remains);
        receiver.getAccount().setBalance(receiver.getAccount().getBalance() + Double.parseDouble(value));
        String info = receiver.getAccount().getAccountId();
        if (facts.get(1)) {
            info = receiver.getSimCard().getPhoneNumber();
        }
        TransferTransaction newTransaction = new TransferTransaction(Double.parseDouble(value) + MainActivity.getFariBank().getManagerData().getFariWage(), MainActivity.getFariBank().getTracingNumber(), receiver, facts.get(1), info, "-", this.getOwner(), false, this.getBalance());
        this.addTransaction(newTransaction);
        receiver.getAccount().addTransaction(new TransferTransaction(Double.parseDouble(value), MainActivity.getFariBank().getTracingNumber() + 1, receiver, facts.get(1), info, "+", this.getOwner(), true, receiver.getAccount().getBalance()));
        MainActivity.getFariBank().setTracingNumber(MainActivity.getFariBank().getTracingNumber() + 2);
        if (this.getOwner().isHasRemainsFund()) {
            this.getOwner().getRemainsFund().saveRemains(remains, MainActivity.getFariBank());
        }
        if (!facts.get(1)) {
            this.addRecent(newTransaction, receiver.getSimCard().getPhoneNumber(), MainActivity.getFariBank());
        }
        Toast.makeText(context, "Transfer completed!", Toast.LENGTH_SHORT).show();
    }


    public List<ChartView.Entry> createDataPoints() {
        List<ChartView.Entry> dataPoints = new ArrayList<>();
        for (int index = this.transactions.size() - 1; index >= 0; index--) {
            Transaction transaction = this.transactions.get(index);
            Date date = Date.from(transaction.getDateAndTime());
            ChartView.Entry newEntry = new ChartView.Entry(date, (float) transaction.getAccountBalance());
            dataPoints.add(newEntry);
        }
        return dataPoints;
    }

    public double getPlusSideTransfers() {
        double amount = 0;
        Transaction prev = this.transactions.get(0);
        for (Transaction transaction : this.transactions) {
            if (transaction instanceof TransferTransaction transfer) {
                if (transfer.getAccountBalance() > prev.getAccountBalance()) {
                    amount = amount + transfer.getValue();
                }
            }
            prev = transaction;
        }
        return amount;
    }

    public double getMinusSideTransfers() {
        double amount = 0;
        Transaction prev = this.transactions.get(0);
        for (Transaction transaction : this.transactions) {
            if (transaction instanceof TransferTransaction transfer) {
                if (transfer.getAccountBalance() < prev.getAccountBalance()) {
                    amount = amount + transfer.getValue();
                }
            }
            prev = transaction;
        }
        return amount;
    }
/*

    public void chargeSimCard(NeoBank neoBank) {
        String phoneNumber = input.nextPhoneNumber(neoBank.getBankData(), "doesn't matter");
        if (phoneNumber == null) {
            return;
        }
        SimCard simCard = neoBank.getManagerData().getSimCard(phoneNumber);
        if (simCard == null) {
            simCard = new SimCard(phoneNumber, false);
        }
        boolean hasBeenCharged = simCard.chargeSimCard(neoBank, this.getOwner());
        if (!hasBeenCharged) {
            System.out.println(ColorConsole.RED + "Charge has failed!" + ColorConsole.RESET);
            return;
        }
        neoBank.getManagerData().addSimCard(simCard);
    }

     */
}

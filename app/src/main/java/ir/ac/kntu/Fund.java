package ir.ac.kntu;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Fund {
    private double balance;
    private SimpleUser owner;
    private String fundID;// a string like 9988-----
    private List<Transaction> transactions;

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(Transaction transaction) {
        this.transactions.add(transaction);
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public SimpleUser getOwner() {
        return owner;
    }

    public void setOwner(SimpleUser owner) {
        this.owner = owner;
    }

    public String getFundID() {
        return fundID;
    }

    public void setFundID(String fundID) {
        this.fundID = fundID;
    }

    public Fund(SimpleUser owner, NeoBank neoBank) {
        String mask = "00000";
        DecimalFormat decimalFormat = new DecimalFormat(mask);
        int fundID1 = neoBank.getBaseFundID();
        neoBank.setBaseFundID(neoBank.getBaseFundID() + 1);
        String fundID = "9" + decimalFormat.format(fundID1);
        setFundID(fundID);
        setBalance(0.0);
        transactions = new ArrayList<>();
        setOwner(owner);
    }


    public void transferToFund(String fundType, String value, Context context, TextView balance) {
        NeoBank neoBank = MainActivity.getFariBank();
        Fund currentFund = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure?\n Receiver : Your " + fundType + "\n Amount : " + value);
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double remains = currentFund.getOwner().isHasRemainsFund() ? currentFund.getOwner().getRemainsFund().calculateRemains(value) : 0;
                if (currentFund.getOwner().getAccount().getBalance() >= Double.parseDouble(value) + remains) {
                    currentFund.setBalance(currentFund.getBalance() + Double.parseDouble(value));
                    currentFund.getOwner().getAccount().setBalance(currentFund.getOwner().getAccount().getBalance() - Double.parseDouble(value) - remains);
                    currentFund.getOwner().getAccount().addTransaction(new TransferInsideTransaction(Double.parseDouble(value), neoBank.getTracingNumber(), "Account", fundType, currentFund.getFundID(), currentFund.getOwner().getAccount().getBalance()));
                    currentFund.addTransaction(new TransferInsideTransaction(Double.parseDouble(value), neoBank.getTracingNumber() + 1, "Account", fundType, currentFund.getFundID(), currentFund.getBalance()));
                    neoBank.setTracingNumber(neoBank.getTracingNumber() + 2);
                    if (currentFund.getOwner().isHasRemainsFund()) {
                        currentFund.getOwner().getRemainsFund().saveRemains(remains, neoBank);
                    }
                    Toast.makeText(context, "Transfer Completed", Toast.LENGTH_SHORT).show();
                    balance.setText(Double.toString(currentFund.getBalance()));
                } else {
                    Toast.makeText(context, "You don't have enough money!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Noo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Transfer");
        warning.show();
    }

    public void transferFromFund(String fundType, String value, Context context, TextView balance) {
        NeoBank neoBank = MainActivity.getFariBank();
        Fund currentFund = this;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure?\n Receiver : Your Account" + "\n Amount : " + value);
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double remains = currentFund.getOwner().isHasRemainsFund() ? currentFund.getOwner().getRemainsFund().calculateRemains(value) : 0;
                if (currentFund.getBalance() >= Double.parseDouble(value) + remains) {
                    currentFund.setBalance(currentFund.getBalance() - Double.parseDouble(value) - remains);
                    currentFund.getOwner().getAccount().setBalance(currentFund.getOwner().getAccount().getBalance() + Double.parseDouble(value));
                    currentFund.getOwner().getAccount().addTransaction(new TransferInsideTransaction(Double.parseDouble(value), neoBank.getTracingNumber(), fundType, "Account", currentFund.getFundID(), currentFund.getOwner().getAccount().getBalance()));
                    currentFund.addTransaction(new TransferInsideTransaction(Double.parseDouble(value), neoBank.getTracingNumber() + 1, fundType, "Account", currentFund.getFundID(), currentFund.getBalance()));
                    neoBank.setTracingNumber(neoBank.getTracingNumber() + 2);
                    if (currentFund.getOwner().isHasRemainsFund()) {
                        currentFund.getOwner().getRemainsFund().saveRemains(remains, neoBank);
                    }
                    balance.setText(Double.toString(currentFund.getBalance()));
                    Toast.makeText(context, "Transfer Completed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "You don't have enough money!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Noo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Transfer");
        warning.show();
    }


    public void dissolveFund(NeoBank neoBank, Context context) {
        this.getOwner().getAccount().setBalance(this.getOwner().getAccount().getBalance() + this.getBalance());
        this.getOwner().removeFund(this);
        Transaction newTransaction = new TransferInsideTransaction(this.getBalance(), neoBank.getTracingNumber(), "Fund", "Account", this.getFundID(), this.getOwner().getAccount().getBalance());
        this.getOwner().getAccount().addTransaction(newTransaction);
        neoBank.setTracingNumber(neoBank.getTracingNumber() + 1);
        double remains = this.getOwner().isHasRemainsFund() ? this.getOwner().getRemainsFund().calculateRemains(Double.toString(this.getBalance())) : 0;
        if (this.getOwner().isHasRemainsFund()) {
            this.getOwner().getRemainsFund().saveRemains(remains, neoBank);
        }
        Toast.makeText(context, "Fund successfully deleted", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, FundsPage.class);
        intent.putExtra("Phone Number", this.getOwner().getSimCard().getPhoneNumber());
        startActivity(context, intent, Bundle.EMPTY);

    }


}

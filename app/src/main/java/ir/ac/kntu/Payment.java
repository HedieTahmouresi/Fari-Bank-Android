package ir.ac.kntu;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.time.Instant;

public class Payment {
    private int id;
    private double amount;
    private boolean beenPayed;
    private Instant dueDate;
    private Loan loan;

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public boolean hasBeenPayed() {
        return beenPayed;
    }

    public void setBeenPayed(boolean beenPayed) {
        this.beenPayed = beenPayed;
    }

    public Instant getDueDate() {
        return dueDate;
    }

    public void setDueDate(Instant dueDate) {
        this.dueDate = dueDate;
    }

    public Payment(int id, double amount, Instant dueDate, Loan loan) {
        this.id = id;
        this.amount = amount;
        this.beenPayed = false;
        this.dueDate = dueDate;
        this.loan = loan;
    }

    public Payment pay(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure?\n The Payment Amount : " + this.getAmount());
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completePayment(context);
            }
        });
        builder.setNegativeButton("Noo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Loan Payment");
        warning.show();
        if (this.hasBeenPayed()){
            return this.getLoan().getPayments().get(this.getId() + 1);
        }
        return this;
    }

    public void completePayment(Context context){
        double remains = this.getLoan().getOwner().isHasRemainsFund() ? this.getLoan().getOwner().getRemainsFund().calculateRemains(Double.toString(this.getAmount())) : 0;
        if (this.getLoan().getOwner().getAccount().getBalance() < this.getAmount() + remains){
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.getLoan().getOwner().getAccount().setBalance(this.getLoan().getOwner().getAccount().getBalance()-this.getAmount()-remains);
        Transaction newTransaction = new PaymentTransfer(this.getLoan().getOwner().getAccount().getBalance()-this.getAmount()-remains, MainActivity.getFariBank().getTracingNumber(), "-");
        this.getLoan().getOwner().getAccount().addTransaction(newTransaction);
        if (this.getLoan().getOwner().isHasRemainsFund()) {
            this.getLoan().getOwner().getRemainsFund().saveRemains(remains, MainActivity.getFariBank());
        }
        this.setBeenPayed(true);
        return;
    }

}

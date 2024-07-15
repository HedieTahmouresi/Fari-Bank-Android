package ir.ac.kntu;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import org.w3c.dom.Text;

import java.time.Duration;
import java.time.Instant;

import ir.ac.kntu.util.Calendar;

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

    public void pay(Context context, TextView textView) {
        if (this.hasBeenPayed()) {
            Toast.makeText(context, "This has already been payed", Toast.LENGTH_SHORT).show();
            return;
        }
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
        if (this.getId() == this.getLoan().getPayments().size() && this.hasBeenPayed()) {
            LoanDetails.getFullPay().setText("This Loan has been fully paid");
            this.getLoan().setBeenFullyPaid(true);
        } else if (this.hasBeenPayed() && Calendar.now().isAfter(this.getDueDate())) {
            this.getLoan().setCurrentPayment(this.getLoan().getPayments().get(this.getId()));
        }
    }

    public void completePayment(Context context) {
        double remains = this.getLoan().getOwner().isHasRemainsFund() ? this.getLoan().getOwner().getRemainsFund().calculateRemains(Double.toString(this.getAmount())) : 0;
        if (this.getLoan().getOwner().getAccount().getBalance() < this.getAmount() + remains) {
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        this.getLoan().getOwner().getAccount().setBalance(this.getLoan().getOwner().getAccount().getBalance() - this.getAmount() - remains);
        Transaction newTransaction = new PaymentTransfer(this.getAmount() + remains, MainActivity.getFariBank().getTracingNumber(), "-");
        this.getLoan().getOwner().getAccount().addTransaction(newTransaction);
        if (this.getLoan().getOwner().isHasRemainsFund()) {
            this.getLoan().getOwner().getRemainsFund().saveRemains(remains, MainActivity.getFariBank());
        }
        this.setBeenPayed(true);
        Instant now = Calendar.now();
        Duration duration = Duration.between(this.getDueDate(), now);
        long numOfDays = duration.toDays();
        if (numOfDays >= 0 && this.getId() < this.getLoan().getPayments().size()) {
            this.getLoan().setCurrentPayment(this.getLoan().getPayments().get(this.getId()));
        } else if (numOfDays < 0) {
            this.getLoan().setCurrentPayment(this);
        } else {
            this.getLoan().setCurrentPayment(this);
            this.getLoan().setBeenFullyPaid(true);
        }
        final String newText = "Updated Text";
        Message msg = LoanDetails.getHandler().obtainMessage();
        msg.obj = newText;
        LoanDetails.getHandler().sendMessage(msg);
    }

}

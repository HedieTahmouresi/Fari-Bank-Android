package ir.ac.kntu;

import android.content.Context;

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

    }

}

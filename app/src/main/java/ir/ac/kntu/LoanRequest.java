package ir.ac.kntu;

import androidx.annotation.NonNull;

public class LoanRequest {
    private double amount;
    private int numOfMonths;
    private SimpleUser owner;
    private LoanStatus status;
    private String reason;

    @NonNull
    @Override
    public String toString() {
        return "  *Status : " + status + ", Num Of Months : " + numOfMonths + ", Amount : " + amount ;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getNumOfMonths() {
        return numOfMonths;
    }

    public void setNumOfMonths(int numOfMonths) {
        this.numOfMonths = numOfMonths;
    }

    public SimpleUser getOwner() {
        return owner;
    }

    public void setOwner(SimpleUser owner) {
        this.owner = owner;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public LoanRequest(double amount, int numOfMonths, SimpleUser owner) {
        this.amount = amount;
        this.numOfMonths = numOfMonths;
        this.owner = owner;
        this.reason = "Checking your background...";
        this.status = LoanStatus.PENDING;
        LoanRequestThread loanRequestThread = new LoanRequestThread(this);
        Thread thread = new Thread(loanRequestThread);
        thread.start();
    }

}

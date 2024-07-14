package ir.ac.kntu;

import java.text.DecimalFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ir.ac.kntu.util.Calendar;

public class Loan {
    private String id;
    private double amount;
    private int numOfMonths;
    private int delays;
    private SimpleUser owner;
    private Instant creation;
    private List<Payment> payments;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public int getDelays() {
        return delays;
    }

    public void setDelays(int delays) {
        this.delays = delays;
    }

    public SimpleUser getOwner() {
        return owner;
    }

    public void setOwner(SimpleUser owner) {
        this.owner = owner;
    }

    public Instant getCreation() {
        return creation;
    }

    public void setCreation(Instant creation) {
        this.creation = creation;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Loan(double amount, int numOfMonths, SimpleUser owner) {
        this.amount = amount;
        this.numOfMonths = numOfMonths;
        this.delays = 0;
        String mask = "000000";
        DecimalFormat decimalFormat = new DecimalFormat(mask);
        int loanID1 = MainActivity.getFariBank().getBaseLoanID();
        MainActivity.getFariBank().setBaseLoanID(MainActivity.getFariBank().getBaseFundID() + 1);
        String loanID = "8" + decimalFormat.format(loanID1);
        this.id = loanID;
        this.owner = owner;
        this.creation = Calendar.now();
        this.payments = new ArrayList<>();
    }

    public Loan createLoan(double amount, int numOfMonths, SimpleUser owner){
        Loan newLoan = new Loan(amount, numOfMonths, owner);
        newLoan.setPayments(createPayments(amount, numOfMonths, newLoan));
        return newLoan;
    }

    public List<Payment> createPayments(double loanAmount, int numOfMonths, Loan loan){
        double benefit = MainActivity.getFariBank().getManagerData().getLoanBenefits();
        double returnValue = (loanAmount * (100 + benefit))/100;
        double paymentAmount = returnValue/numOfMonths;
        ZonedDateTime firstDay = this.getCreation().atZone(ZoneId.systemDefault());
        ZonedDateTime due = firstDay;
        List<Payment> paymentsList = new ArrayList<>();
        for (int i = 1 ; i <= numOfMonths ; i++){
            due = due.plusMonths(1);
            Instant dueDate = due.toInstant();
            Payment newPayment = new Payment(i, paymentAmount, dueDate, loan);
            paymentsList.add(newPayment);
        }
        return paymentsList;
    }
}

package ir.ac.kntu;

public class LoanRequest {
    private double amount;
    private int numOfMonths;
    private SimpleUser owner;
    private LoanStatus status;

    @Override
    public String toString() {
        return "LoanRequest{" +
                "status= " + status +
                ", numOfMonths= " + numOfMonths +
                ", amount= " + amount +
                '}';
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
    }
}

package ir.ac.kntu;

public class LoanRequestThread implements Runnable{
    private LoanRequest loanRequest;

    public LoanRequest getLoanRequest() {
        return loanRequest;
    }

    public void setLoanRequest(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    public LoanRequestThread(LoanRequest loanRequest) {
        this.loanRequest = loanRequest;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
            if (this.getLoanRequest().getOwner().getNegativePoints()<5 && this.getLoanRequest().getOwner().getAccount().getBalance()>=this.getLoanRequest().getAmount()/2 && this.getLoanRequest().getOwner().getLoans().size()<=5){
                this.getLoanRequest().setStatus(LoanStatus.ACCEPTED);
                Loan newLoan = new Loan(this.getLoanRequest().getAmount(), getLoanRequest().getNumOfMonths(), this.getLoanRequest().getOwner());
                this.getLoanRequest().getOwner().addLoan(newLoan);
            }else {
                this.getLoanRequest().setStatus(LoanStatus.REJECTED);
                this.getLoanRequest().setReason("You have " + this.getLoanRequest().getOwner().getNegativePoints() + "negative points" + "Your account balance is " + this.getLoanRequest().getOwner().getAccount().getBalance() + " and you have " + this.getLoanRequest().getOwner().getLoans().size() + "loans in payment!");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

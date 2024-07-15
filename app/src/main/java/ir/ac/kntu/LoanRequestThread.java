package ir.ac.kntu;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

public class LoanRequestThread implements Runnable {
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
            if (this.getLoanRequest().getOwner().getNegativePoints() < 5 && this.getLoanRequest().getOwner().getAccount().getBalance() >= this.getLoanRequest().getAmount() / 2 && this.getLoanRequest().getOwner().getLoans().size() <= 5) {
                this.getLoanRequest().setStatus(LoanStatus.ACCEPTED);
                Loan newLoan = new Loan(this.getLoanRequest().getAmount(), getLoanRequest().getNumOfMonths(), this.getLoanRequest().getOwner());
                this.getLoanRequest().getOwner().addLoan(newLoan);
                this.getLoanRequest().setReason("Accepted");
            } else {
                this.getLoanRequest().setStatus(LoanStatus.REJECTED);
                this.getLoanRequest().setReason("You have " + this.getLoanRequest().getOwner().getNegativePoints() + " negative points. " + "\nYour account balance is " + this.getLoanRequest().getOwner().getAccount().getBalance() + " \nand you have " + this.getLoanRequest().getOwner().getLoans().size() + " loans in payment!");
            }
            final String newText = "Updated Text";
            Message msg = LoanRequestPage.getHandler().obtainMessage();
            msg.obj = newText;
            LoanRequestPage.getHandler().sendMessage(msg);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}

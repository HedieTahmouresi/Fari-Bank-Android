package ir.ac.kntu;


import android.os.Message;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import ir.ac.kntu.util.Calendar;

public class PaymentThread implements Runnable {
    private Payment payment;

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public PaymentThread(Payment payment) {
        this.payment = payment;
    }

    @Override
    public void run() {
        Instant now = Calendar.now();
        Duration duration = Duration.between(now, this.getPayment().getDueDate());
        Duration oneDay = Duration.ofDays(1);
        long time = duration.toMillis() / 6000;
        time = time + (oneDay.toMillis() / 6000);
        try {
            Thread.sleep(time);
            if (!this.getPayment().hasBeenPayed()) {
                this.getPayment().getLoan().setDelays(this.getPayment().getLoan().getDelays() + 1);
                this.getPayment().getLoan().getOwner().setNegativePoints(this.getPayment().getLoan().getOwner().getNegativePoints() + 1);
                this.getPayment().getLoan().setCurrentPayment(this.getPayment());
                final String newText = "Updated Text";
                Message msg = LoanDetails.getHandler().obtainMessage();
                msg.obj = newText;
                LoanDetails.getHandler().sendMessage(msg);
            } else if (this.getPayment().getId() != this.getPayment().getLoan().getPayments().size()) {
                this.getPayment().getLoan().setCurrentPayment(this.getPayment().getLoan().getPayments().get(this.getPayment().getId()));
                final String newText = "Updated Text";
                Message msg = LoanDetails.getHandler().obtainMessage();
                msg.obj = newText;
                LoanDetails.getHandler().sendMessage(msg);
            } else {
                LoanDetails.getFullPay().setText("This Loan has been fully paid!");
            }
        } catch (InterruptedException error) {
            System.out.println("Thread Error");
        }
    }
}

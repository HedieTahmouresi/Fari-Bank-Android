package ir.ac.kntu;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class PaymentTransfer extends Transaction {
    public PaymentTransfer(double value, int tracingNumber, String sign, double accountBalance) {
        super(value, tracingNumber, sign, accountBalance);
    }

    @Override
    public String showInfo(NeoBank neoBank) {
        String returnValue = "***";
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        returnValue = returnValue + "\nLoan Payment : ";
        returnValue = returnValue + "\nValue: " + this.getSign() + this.getValue();
        returnValue = returnValue + "\nDate: " + datePart;
        returnValue = returnValue + "\nTime: " + timePart;
        returnValue = returnValue + "\nTracing Number: " + this.getTracingNumber();
        returnValue = returnValue + "\n***";
        return returnValue;
    }
}

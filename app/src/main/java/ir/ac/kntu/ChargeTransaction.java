package ir.ac.kntu;

import android.os.Parcel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class ChargeTransaction extends Transaction {
    public ChargeTransaction(double value, int tracingNumber) {
        super(value, tracingNumber, "+");
    }


    @Override
    public String showInfo(NeoBank neoBank) {
        String returnValue = "***";
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        returnValue = returnValue + "\nCharge Transaction : ";
        returnValue = returnValue + "\nValue: " + this.getSign() + this.getValue();
        returnValue = returnValue + "\nDate: " + datePart;
        returnValue = returnValue + "\nTime: " + timePart;
        returnValue = returnValue + "\nTracing Number: " + this.getTracingNumber();
        returnValue = returnValue + "\n***";
        return returnValue;
    }


}

package ir.ac.kntu;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class SimChargeTransaction extends Transaction {
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public SimChargeTransaction(double value, int tracingNumber, String sign, String phoneNumber) {
        super(value, tracingNumber, sign);
        setPhoneNumber(phoneNumber);
    }


    @Override
    public String showInfo(NeoBank neoBank) {
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        return "Transaction : \n" + "Value: " + this.getSign() + this.getValue() + "\nDate: " + datePart + ", Time: "+ timePart + "\nTracing Number: " +this.getTracingNumber()+"\nReceiver Phone Number: " + this.getPhoneNumber();
    }


}

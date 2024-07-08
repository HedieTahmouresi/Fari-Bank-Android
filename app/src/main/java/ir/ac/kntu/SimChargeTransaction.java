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
    public void showInfo(NeoBank neoBank) {
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        System.out.println(ColorConsole.PINK + "Transaction : " + ColorConsole.PURPLE + "Charging Sim Card" + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Value: " + ColorConsole.PURPLE + this.getSign() + this.getValue() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Date: " + ColorConsole.PURPLE + datePart + ColorConsole.PINK + ", Time: " + ColorConsole.PURPLE + timePart + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Tracing Number: " + ColorConsole.PURPLE + this.getTracingNumber() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Receiver Phone Number: " + ColorConsole.PURPLE + this.getPhoneNumber() + ColorConsole.RESET);

    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Transaction Type :" + ColorConsole.CYAN + "Sim Card Charge " + super.toString();
    }
}

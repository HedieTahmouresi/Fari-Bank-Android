package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import ir.ac.kntu.util.Calendar;

import java.time.*;

public class Transaction {
    private double value;
    private int tracingNumber;
    private String sign;
    private Instant dateAndTime;


    public Instant getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(Instant dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getTracingNumber() {
        return tracingNumber;
    }

    public void setTracingNumber(int tracingNumber) {
        this.tracingNumber = tracingNumber;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Transaction(double value, int tracingNumber, String sign) {
        setValue(value);
        setTracingNumber(tracingNumber);
        setDateAndTime(Calendar.now());
        setSign(sign);
    }

    public void showInfo(NeoBank neoBank) {
        ZonedDateTime zonedDateTime = dateAndTime.atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        System.out.println(ColorConsole.PINK + "Transaction : " + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Value: " + ColorConsole.PURPLE + this.getSign() + this.getValue() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Date: " + ColorConsole.PURPLE + datePart + ColorConsole.PINK + ", Time: " + ColorConsole.PURPLE + timePart + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Tracing Number: " + ColorConsole.PURPLE + this.getTracingNumber() + ColorConsole.RESET);
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + ", Date and Time :" + ColorConsole.CYAN + this.getDateAndTime() +
                ColorConsole.PURPLE + ", Value : " + ColorConsole.CYAN + this.getValue() +
                ColorConsole.PURPLE + ", Tracing Number : " + ColorConsole.CYAN + this.getTracingNumber() + ColorConsole.RESET;
    }

    public boolean dateIsBetween(Instant start, Instant end) {
        return this.getDateAndTime().isAfter(start) && this.getDateAndTime().isBefore(end);
    }
}

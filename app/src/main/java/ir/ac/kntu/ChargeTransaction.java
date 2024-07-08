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


    /*
    @Override
    public void showInfo(NeoBank neoBank) {
        System.out.println(ColorConsole.PURPLE + "***" + ColorConsole.RESET);
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        System.out.println(ColorConsole.PURPLE + "Charge Transaction : ");
        System.out.println("Value: " + ColorConsole.GREEN + this.getSign() + this.getValue() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Date: " + ColorConsole.PINK + datePart + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Time: " + ColorConsole.PINK + timePart + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Tracing Number: " + ColorConsole.PINK + this.getTracingNumber() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "***" + ColorConsole.RESET);
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Transaction Type :" + ColorConsole.CYAN + "Charge" + super.toString();
    }

     */
}

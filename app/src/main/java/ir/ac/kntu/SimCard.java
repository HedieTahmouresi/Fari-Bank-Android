package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class SimCard {
    private String phoneNumber;
    private double charge;
    private boolean hasAccount;


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean hasAccount() {
        return hasAccount;
    }

    public void setHasAccount(boolean hasAccount) {
        this.hasAccount = hasAccount;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public SimCard(String phoneNumber, boolean hasAccount) {
        setPhoneNumber(phoneNumber);
        setCharge(0);
        setHasAccount(hasAccount);
    }

    public void showCharge() {
        System.out.println( "This is your charge : " + this.getCharge() );
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof SimCard simCard)) {
            return false;
        }
        return Objects.equals(getPhoneNumber(), simCard.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPhoneNumber());
    }

/*
    public boolean chargeSimCard(NeoBank neoBank, SimpleUser currentUser) {
        System.out.println(ColorConsole.BLUE + "How much do you want to charge this sim card?" + ColorConsole.PURPLE + "(" + this.getPhoneNumber() + ")" + ColorConsole.RESET);
        String answer = input.nextLine();
        if (!input.exitPoint(answer)) {
            return false;
        } else if (!answer.matches("[0-9]+\\.?[0-9]*")) {
            System.out.println(ColorConsole.RED + "Wrong format" + ColorConsole.RESET);
            return this.chargeSimCard(neoBank, currentUser);
        }
        double remains = currentUser.isHasRemainsFund() ? currentUser.getRemainsFund().calculateRemains(answer) : 0;
        if (Double.parseDouble(answer) + remains + neoBank.getManagerData().getChargeWage() > currentUser.getAccount().getBalance()) {
            System.out.println(ColorConsole.RED + "transfer failed! you don't have enough money!" + ColorConsole.RESET);
            return this.chargeSimCard(neoBank, currentUser);
        }
        currentUser.getAccount().addTransaction(new SimChargeTransaction(Double.parseDouble(answer) + neoBank.getManagerData().getChargeWage(), neoBank.getTracingNumber(), " ", this.getPhoneNumber()), "Sim Card Charge");
        this.setCharge(this.getCharge() + Double.parseDouble(answer));
        currentUser.getAccount().setBalance(currentUser.getAccount().getBalance() - remains - Double.parseDouble(answer) - neoBank.getManagerData().getChargeWage());
        if (currentUser.isHasRemainsFund()) {
            currentUser.getRemainsFund().saveRemains(remains, neoBank);
        }
        System.out.println(ColorConsole.GREEN + "Sim Card successfully charged" + ColorConsole.RESET);
        return true;
    }



 */
}

package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ManagerData {
    private Data data;
    private List<BonusFund> bonusFunds;
    private double fariWage;
    private List<SimCard> simCards;
    private double chargeWage;
    private int bonusPercentage;
    private double cardWage;
    private int bridgePercentage;
    private double wireWage;
    private double loanBenefits;
    private List<WireTransaction> wireTransactions;


    public void addTransaction(WireTransaction wireTransaction) {
        this.wireTransactions.add(wireTransaction);
    }

    public double getWireWage() {
        return wireWage;
    }

    public void setWireWage(double wireWage) {
        this.wireWage = wireWage;
    }

    public int getBridgePercentage() {
        return bridgePercentage;
    }

    public void setBridgePercentage(int bridgePercentage) {
        this.bridgePercentage = bridgePercentage;
    }

    public double getCardWage() {
        return cardWage;
    }

    public void setCardWage(double cardWage) {
        this.cardWage = cardWage;
    }

    public double getChargeWage() {
        return chargeWage;
    }

    public void setChargeWage(double chargeWage) {
        this.chargeWage = chargeWage;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public ManagerData(Data data) {
        this.data = data;
        this.bonusFunds = new ArrayList<>();
        //this.admins = new ArrayList<>();
        //this.managers = new ArrayList<>();
        this.simCards = new ArrayList<>();
        this.wireTransactions = new ArrayList<>();
        setFariWage(0.0);
        setCardWage(300.0);
        setChargeWage(0.0);
        setBridgePercentage(2);
        setWireWage(2000);
        setBonusPercentage(10);
        setLoanBenefits(10);
    }

    public double getLoanBenefits() {
        return loanBenefits;
    }

    public void setLoanBenefits(double loanBenefits) {
        this.loanBenefits = loanBenefits;
    }

    public double getFariWage() {
        return fariWage;
    }

    public void setFariWage(double fariWage) {
        this.fariWage = fariWage;
    }

    public int getBonusPercentage() {
        return bonusPercentage;
    }

    public void setBonusPercentage(int bonusPercentage) {
        this.bonusPercentage = bonusPercentage;
    }

    public void addBonusFund(BonusFund fund) {
        this.bonusFunds.add(fund);
    }

    public void addSimCard(SimCard simCard) {
        this.simCards.add(simCard);
    }

    public SimCard getSimCard(String phoneNumber) {
        for (SimCard simCard : simCards) {
            if (simCard.getPhoneNumber().equals(phoneNumber)) {
                return simCard;
            }
        }
        return null;
    }

}

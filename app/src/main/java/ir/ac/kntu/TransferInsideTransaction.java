package ir.ac.kntu;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class TransferInsideTransaction extends Transaction {

    private String receiver;
    private String sender;
    private String fundId;

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getFundId() {
        return fundId;
    }

    public void setFundId(String fundId) {
        this.fundId = fundId;
    }

    public TransferInsideTransaction(double value, int tracingNumber, String sender, String receiver, String fundId) {
        super(value, tracingNumber, " ");
        setSender(sender);
        setReceiver(receiver);
        setFundId(fundId);
    }

    @Override
    public void showInfo(NeoBank neoBank) {
        super.showInfo(neoBank);
        System.out.println(ColorConsole.PINK + "***\n" + "From your : " + ColorConsole.PURPLE + this.getSender() + ColorConsole.RESET);
        if ("Account".equals(this.getReceiver())) {
            System.out.println(ColorConsole.PINK + "Fund Id : " + ColorConsole.PURPLE + this.getFundId() + ColorConsole.RESET);
        }
        System.out.println(ColorConsole.PINK + "To your : " + ColorConsole.PURPLE + this.getReceiver() + ColorConsole.RESET);
        if ("Account".equals(this.getSender())) {
            System.out.println(ColorConsole.PINK + "Fund Id : " + ColorConsole.PURPLE + this.getFundId() + ColorConsole.RESET);
        }
        System.out.println(ColorConsole.PINK + "***" + ColorConsole.RESET);
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Transaction Type :" + ColorConsole.CYAN + "Inside Transfer" + super.toString();
    }
}

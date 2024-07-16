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

    public TransferInsideTransaction(double value, int tracingNumber, String sender, String receiver, String fundId, double accountBalance) {
        super(value, tracingNumber, " ", accountBalance);
        setSender(sender);
        setReceiver(receiver);
        setFundId(fundId);
    }

    @Override
    public String showInfo(NeoBank neoBank) {
        String returnValue = super.showInfo(neoBank);
        returnValue = returnValue + "\n***\n" + "From your : " + this.getSender();
        if ("Account".equals(this.getReceiver())) {
            returnValue = returnValue + "\nFund Id : " + this.getFundId();
        }
        returnValue = returnValue + "\nTo your : " + this.getReceiver();
        if ("Account".equals(this.getSender())) {
            returnValue = returnValue + "\nFund Id : " + this.getFundId();
        }
        returnValue = returnValue + "\n***";
        return returnValue;
    }

}

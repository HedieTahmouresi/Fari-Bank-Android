package ir.ac.kntu;

import android.os.Parcel;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TransferTransaction extends Transaction {
    private UserPerson receiver;
    private SimpleUser sender;
    private boolean byContact;
    private String receiverInfo;
    private boolean isReceiver;


    public boolean isReceiver() {
        return isReceiver;
    }

    public void setReceiver(boolean receiver) {
        isReceiver = receiver;
    }

    public SimpleUser getSender() {
        return sender;
    }

    public void setSender(SimpleUser sender) {
        this.sender = sender;
    }

    public String getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(String receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public boolean isByContact() {
        return byContact;
    }

    public void setByContact(boolean byContact) {
        this.byContact = byContact;
    }

    public void setReceiver(UserPerson receiver) {
        this.receiver = receiver;
    }

    public UserPerson getReceiver() {
        return receiver;
    }

    public TransferTransaction(double value, int tracingNumber, UserPerson receiver, boolean byContact, String receiverInfo, String sign, SimpleUser sender, Boolean isReceiver, double accountBalance) {
        super(value, tracingNumber, sign, accountBalance);
        setReceiver(receiver);
        setSender(sender);
        setByContact(byContact);
        setReceiverInfo(receiverInfo);
        setReceiver(isReceiver);
    }

    @Override
    public String showInfo(NeoBank neoBank) {
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        String returnValue = "***\nTransfer Transaction:";
        if (!this.isByContact()) {
            returnValue = returnValue + this.showNotContactVersion(neoBank);
        } else {
            returnValue = returnValue + this.showContactVersion(neoBank);
        }
        returnValue = returnValue + "\nValue: \n" + this.getSign().toString() + Double.toString(this.getValue());
        returnValue = returnValue + "\nDate: " + datePart.toString() + "Time: " + timePart.toString();
        returnValue = returnValue + "\nTracing Number: " + Integer.toString(this.getTracingNumber()) + "\n***";
        return returnValue;
    }


    public String showContactVersion(NeoBank neoBank) {
        String returnValue = "";
        if (!this.isReceiver()) {
            returnValue = returnValue + "\nFullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName();
            returnValue = returnValue + "\nPhone number sender : " + this.getSender().getSimCard().getPhoneNumber();
            Contact currContact = this.getSender().findContact(this.getReceiverInfo());
            returnValue = returnValue + "\nFullName receiver : " + currContact.getName() + " " + currContact.getLastName();
            returnValue = returnValue + "\nPhone Number receiver : " + this.getReceiverInfo();
        } else {
            if (neoBank.getBankData().getUserByPhone(this.getReceiverInfo()).contactExistence(new Contact(" ", " ", this.getSender().getSimCard()))) {
                Contact currContact = neoBank.getBankData().getUserByPhone(this.getReceiverInfo()).findContact(this.getSender().getSimCard().getPhoneNumber());
                returnValue = returnValue + "\nFullName sender : " + currContact.getName() + " " + currContact.getLastName();
                returnValue = returnValue + "\nPhone number sender : " + this.getSender().getSimCard().getPhoneNumber();
            } else {
                returnValue = returnValue + "\nFullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName();
                returnValue = returnValue + "\nPhone number sender : " + this.getSender().getSimCard().getPhoneNumber();
            }
            returnValue = returnValue + "\nFullName receiver : " + this.getReceiver().getName() + " " + this.getReceiver().getLastName();
            returnValue = returnValue + "\nPhone Number receiver : " + this.getReceiverInfo();
        }
        return returnValue;
    }

    public String showNotContactVersion(NeoBank neoBank) {
        String returnValue = "";
        if (!this.isReceiver()) {
            returnValue = returnValue + "\nFullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName();
            returnValue = returnValue + "\nPhone number sender : " + this.getSender().getSimCard().getPhoneNumber();
        } else {
            if (this.getSender().contactExistence(new Contact(" ", " ", this.getReceiver().getSimCard()))) {
                Contact currentContact = neoBank.getBankData().getUserByAccountID(this.getReceiverInfo()).findContact(this.getSender().getSimCard().getPhoneNumber());
                returnValue = returnValue + "\nFullName sender : " + currentContact.getName() + " " + currentContact.getLastName();
                returnValue = returnValue + "\nPhone number sender : " + this.getSender().getSimCard().getPhoneNumber();
            } else {
                returnValue = returnValue + "FullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName();
                returnValue = returnValue + "\nPhone number sender : " + this.getSender().getSimCard().getPhoneNumber();
            }
        }
        if (this.getSender().contactExistence(new Contact(" ", " ", this.getReceiver().getSimCard())) && !this.isReceiver() && neoBank.getBankData().getUserByAccountID(this.getReceiverInfo()).isContactOption()) {
            Contact currContact = this.getSender().findContact(neoBank.getBankData().getUserByAccountID(this.getReceiverInfo()).getSimCard().getPhoneNumber());
            returnValue = returnValue + "\nFullName receiver : " + currContact.getName() + " " + currContact.getLastName();
        } else {
            returnValue = returnValue + "\nFullName receiver : " + this.getReceiver().getName() + " " + this.getReceiver().getLastName();
        }
        returnValue = returnValue + "\nAccount ID receiver : " + this.getReceiverInfo();
        return returnValue;
    }

}

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

    public TransferTransaction(double value, int tracingNumber, UserPerson receiver, boolean byContact, String receiverInfo, String sign, SimpleUser sender, Boolean isReceiver) {
        super(value, tracingNumber, sign);
        setReceiver(receiver);
        setSender(sender);
        setByContact(byContact);
        setReceiverInfo(receiverInfo);
        setReceiver(isReceiver);
    }
/*
    @Override
    public void showInfo(NeoBank neoBank) {
        ZonedDateTime zonedDateTime = this.getDateAndTime().atZone(ZoneId.systemDefault());
        LocalDate datePart = zonedDateTime.toLocalDate();
        LocalTime timePart = zonedDateTime.toLocalTime();
        System.out.println(ColorConsole.CYAN + "***\nTransfer Transaction:");
        if (!this.isByContact()) {
            this.showNotContactVersion(neoBank);
        } else {
            this.showContactVersion(neoBank);
        }
        System.out.println("Value: " + this.getSign() + this.getValue() + ColorConsole.RESET);
        System.out.println(ColorConsole.CYAN + "Date: " + datePart + "Time: " + timePart);
        System.out.println("Tracing Number: " + this.getTracingNumber() + ColorConsole.RESET + "\n***");
    }

    public void showContactVersion(NeoBank neoBank) {
        if (!this.isReceiver()) {
            System.out.println("FullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName());
            System.out.println("Phone number sender : " + this.getSender().getSimCard().getPhoneNumber());
            Contact currContact = this.getSender().findContact(this.getReceiverInfo());
            System.out.println("FullName receiver : " + currContact.getName() + " " + currContact.getLastName());
            System.out.println("Phone Number receiver : " + this.getReceiverInfo());
        } else {
            if (neoBank.getBankData().getUserByPhone(this.getReceiverInfo()).contactExistence(new Contact(" ", " ", this.getSender().getSimCard()))) {
                Contact currContact = neoBank.getBankData().getUserByPhone(this.getReceiverInfo()).findContact(this.getSender().getSimCard().getPhoneNumber());
                System.out.println("FullName sender : " + currContact.getName() + " " + currContact.getLastName());
                System.out.println("Phone number sender : " + this.getSender().getSimCard().getPhoneNumber());
            } else {
                System.out.println("FullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName());
                System.out.println("Phone number sender : " + this.getSender().getSimCard().getPhoneNumber());
            }
            System.out.println("FullName receiver : " + this.getReceiver().getName() + " " + this.getReceiver().getLastName());
            System.out.println("Phone Number receiver : " + this.getReceiverInfo());
        }
    }

    public void showNotContactVersion(NeoBank neoBank) {
        if (!this.isReceiver()) {
            System.out.println("FullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName());
            System.out.println("Phone number sender : " + this.getSender().getSimCard().getPhoneNumber());
        } else {
            if (this.getSender().contactExistence(new Contact(" ", " ", this.getReceiver().getSimCard()))) {
                Contact currentContact = neoBank.getBankData().getUserByAccountID(this.getReceiverInfo()).findContact(this.getSender().getSimCard().getPhoneNumber());
                System.out.println("FullName sender : " + currentContact.getName() + " " + currentContact.getLastName());
                System.out.println("Phone number sender : " + this.getSender().getSimCard().getPhoneNumber());
            } else {
                System.out.println("FullName sender : " + this.getSender().getName() + " " + this.getSender().getLastName());
                System.out.println("Phone number sender : " + this.getSender().getSimCard().getPhoneNumber());
            }
        }
        if (this.getSender().contactExistence(new Contact(" ", " ", this.getReceiver().getSimCard())) && !this.isReceiver() && neoBank.getBankData().getUserByAccountID(this.getReceiverInfo()).isContactOption()) {
            Contact currContact = this.getSender().findContact(neoBank.getBankData().getUserByAccountID(this.getReceiverInfo()).getSimCard().getPhoneNumber());
            System.out.println("FullName receiver : " + currContact.getName() + " " + currContact.getLastName());
        } else {
            System.out.println("FullName receiver : " + this.getReceiver().getName() + " " + this.getReceiver().getLastName());
        }
        System.out.println("Account ID receiver : " + this.getReceiverInfo());
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Transaction Type :" + ColorConsole.CYAN + "Transfer" + super.toString();
    }

 */
}

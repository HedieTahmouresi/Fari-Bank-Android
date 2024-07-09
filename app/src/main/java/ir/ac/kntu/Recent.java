package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Recent {
    private SimpleUser person;
    private boolean byContact;
    private String receiverInfo;


    public SimpleUser getPerson() {
        return person;
    }

    public void setPerson(SimpleUser person) {
        this.person = person;
    }

    public boolean isByContact() {
        return byContact;
    }

    public void setByContact(boolean byContact) {
        this.byContact = byContact;
    }

    public String getReceiverInfo() {
        return receiverInfo;
    }

    public void setReceiverInfo(String receiverInfo) {
        this.receiverInfo = receiverInfo;
    }

    public Recent(SimpleUser person, boolean byContact) {
        setPerson(person);
        setByContact(byContact);
        if (byContact) {
            setReceiverInfo(person.getSimCard().getPhoneNumber());
        } else {
            setReceiverInfo(person.getAccount().getAccountId());
        }
    }

    @Override
    public String toString() {
        return this.getPerson().getName() + " " + this.getPerson().getLastName();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Recent recent)) {
            return false;
        }
        return Objects.equals(getPerson(), recent.getPerson());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPerson());
    }
}

package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class UserPerson {
    private String name;
    private String lastName;
    private SimCard simCard;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SimCard getSimCard() {
        return simCard;
    }

    public void setSimCard(SimCard simCard) {
        this.simCard = simCard;
    }

    public UserPerson(String name, String lastName, SimCard simCard) {
        setName(name);
        setLastName(lastName);
        simCard.setHasAccount(true);
        setSimCard(simCard);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UserPerson that)) {
            return false;
        }
        return Objects.equals(getSimCard(), that.getSimCard());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSimCard());
    }



}

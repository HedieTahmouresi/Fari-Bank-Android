package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Authentication extends Request {
    private boolean authenticated;


    protected Authentication(Parcel in) {
        super(in.readString(), RequestSection.AUTHENTICATIONS, in.readString());
        authenticated = in.readByte() != 0;
    }


    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public Authentication(String phoneNumber) {
        super("Please authenticate me!", RequestSection.AUTHENTICATIONS, phoneNumber);
        setAuthenticated(false);
    }

    public String showRejection() {
        return "you haven't been authenticated!\nThe reason : " + this.getAnswer();
    }

    public void authenticateUser(NeoBank neoBank, SimpleUser user) {
        this.setAuthenticated(true);
        this.setAnswer("Accepted!");
        user.setAccount(new Account(user, neoBank));
    }

}

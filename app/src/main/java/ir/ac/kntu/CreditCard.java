package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.DecimalFormat;
import java.util.Random;

public class CreditCard {
    private String creditCardId;
    private int password;
    private boolean setPassword;


    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public boolean hasSetPassword() {
        return setPassword;
    }

    public void setSetPassword(boolean setPassword) {
        this.setPassword = setPassword;
    }

    public CreditCard(NeoBank neoBank) {
        Random random = new Random();
        long creditCardPartOne;
        String creditCardString;
        String mask1 = "0000";
        DecimalFormat decimalFormat = new DecimalFormat(mask1);
        do {
            creditCardString = neoBank.getCreditCardStarter();
            creditCardPartOne = random.nextInt(10000);
            creditCardString = creditCardString.concat(decimalFormat.format(creditCardPartOne));
            creditCardPartOne = random.nextInt(10000);
            creditCardString = creditCardString.concat(decimalFormat.format(creditCardPartOne));
        } while (neoBank.getBankData().existsCreditCard(creditCardString));
        setCreditCardId(creditCardString);
        setSetPassword(false);
    }


}

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

/*
    public boolean enterPreviousPassCode() {
        System.out.println(ColorConsole.BLUE + "Please enter passcode!" + ColorConsole.RESET);
        String answer = input.nextLine();
        if (!input.exitPoint(answer)) {
            return false;
        } else if (!answer.matches("\\d+")) {
            System.out.println(ColorConsole.RED + "Wrong format! You should enter a number made of 4 digits!" + ColorConsole.RESET);
        } else if (answer.length() != 4) {
            System.out.println(ColorConsole.RED + "Wrong format! You should enter a number made of 4 digits!" + ColorConsole.RESET);
        } else if (Integer.parseInt(answer) == this.getPassword()) {
            return true;
        }else {
            System.out.println(ColorConsole.RED + "Wrong password!" + ColorConsole.RESET);
        }
        return this.enterPreviousPassCode();
    }

    public void changePassCode() {
        System.out.println(ColorConsole.PINK + "Since you already have set a password first you have to enter that passcode!" + ColorConsole.RESET);
        if (!enterPreviousPassCode()) {
            return;
        }
        String answer;
        do {
            System.out.println(ColorConsole.BLUE + "Please enter the new passcode!" + ColorConsole.RESET);
            answer = input.nextLine();
            if (!input.exitPoint(answer)) {
                return;
            } else if (!answer.matches("\\d{4}")) {
                System.out.println(ColorConsole.RED + "Wrong format! You should enter a number made of 4 digits!" + ColorConsole.RESET);
            }
        } while (!answer.matches("\\d{4}"));
        this.setPassword(Integer.parseInt(answer));
    }

    public void setPassCode() {
        System.out.println(ColorConsole.PINK + "You haven't set a password before!" + ColorConsole.RESET);
        String answer;
        do {
            System.out.println(ColorConsole.BLUE + "Please enter the new passcode!" + ColorConsole.RESET);
            answer = input.nextLine();
            if (!input.exitPoint(answer)) {
                return;
            } else if (!answer.matches("\\d{4}")) {
                System.out.println(ColorConsole.RED + "Wrong format! You should enter a number made of 4 digits!" + ColorConsole.RESET);
            }
        } while (!answer.matches("\\d{4}"));
        this.setPassword(Integer.parseInt(answer));
        this.setSetPassword(true);
    }

 */
}

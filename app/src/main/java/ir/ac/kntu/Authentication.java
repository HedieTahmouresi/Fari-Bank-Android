package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Authentication extends Request{
    private boolean authenticated;


    protected Authentication(Parcel in) {
        super(in.readString(),RequestSection.AUTHENTICATIONS, in.readString());
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
/*
    public void showRejection() {
        System.out.println(ColorConsole.YELLOW_BOLD + "you haven't been authenticated!");
        System.out.println("The reason :" + this.getAnswer() + ColorConsole.RESET);
    }

    @Override
    public void showInfo(Data data) {
        SimpleUser currentUser = data.getUserByPhone(this.getPhoneNumber());
        System.out.println(ColorConsole.PINK + "***" + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Name : " + ColorConsole.PURPLE + currentUser.getName() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Last Name : " + ColorConsole.PURPLE + currentUser.getLastName() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Social Security Number : " + ColorConsole.PURPLE + currentUser.getSecurityNumber() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Phone Number : " + ColorConsole.PURPLE + currentUser.getSimCard().getPhoneNumber() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Password : " + ColorConsole.PURPLE + currentUser.getPassword() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Request Status : " + ColorConsole.PURPLE + this.getStatus());
        System.out.println(ColorConsole.PINK + "Admins Answer : " + ColorConsole.PURPLE + this.getAnswer());
        System.out.println(ColorConsole.PINK + "***" + ColorConsole.RESET);
    }
    */

    public void authenticateUser(NeoBank neoBank, SimpleUser user) {
        this.setAuthenticated(true);
        this.setAnswer("Accepted!");
        user.setAccount(new Account(user, neoBank));
    }

    /*
    public void rejectUser() {
        System.out.println(ColorConsole.BLUE + "Why are you rejecting this user?" + ColorConsole.RESET);
        String answer = input.nextLine();
        if (!input.exitPoint(answer)) {
            return;
        }
        this.setAuthenticated(false);
        this.setAnswer(answer);
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Authentication Request {" +
                "phoneNumber='" + this.getPhoneNumber() + '\'' +
                ", authenticated=" + authenticated +
                ", Status=" + this.getStatus() +
                '}' + ColorConsole.RESET;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isAuthenticated(), input);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Authentication that)) {
            return false;
        }
        return this.getPhoneNumber().equals(((Authentication) obj).getPhoneNumber());
    }

    public void authenticateUser(NeoBank neoBank) {
        this.setStatus(RequestStatus.IN_PROCESS);
        SimpleUser currentUser = neoBank.getBankData().getUserByPhone(this.getPhoneNumber());
        currentUser.getAuthenticated().showInfo(neoBank.getBankData());
        System.out.println(ColorConsole.BLUE + "Would you like to authenticate this user?" + ColorConsole.PURPLE + "(1. yes, 2. no)" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "1", "yes":
                this.setStatus(RequestStatus.PROCESSED);
                neoBank.getBankData().removeAuthentication(currentUser);
                currentUser.getAuthenticated().authenticateUser(neoBank, currentUser);
                return;
            case "2", "no":
                this.setStatus(RequestStatus.PROCESSED);
                currentUser.getAuthenticated().rejectUser();
                return;
            default:
                if (!input.exitPoint(answer)) {
                    return;
                } else {
                    System.out.println(ColorConsole.RED + "Wrong Input" + ColorConsole.RESET);
                }
        }
        this.authenticateUser(neoBank);
    }


 */
}

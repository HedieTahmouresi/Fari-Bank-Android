package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Data {
    private List<Request> requests;
    private List<SimpleUser> users;


    public Data() {
        this.requests = new ArrayList<>();
        this.users = new ArrayList<>();
    }


    public void addRequest(SimpleUser currentUser, Request request) {
        this.requests.add(request);
        RequestThread requestThread = new RequestThread(currentUser, MainActivity.getFariBank(), request);
        Thread thread = new Thread(requestThread);
        thread.start();
    }

    public void addAuthentication(Authentication authentication) {
        this.requests.add(authentication);
    }

    public void removeAuthentication(SimpleUser user) {
        this.requests.remove(user.getAuthenticated());
    }

    public void addUser(SimpleUser user) {
        this.users.add(user);
    }

    public SimpleUser getUserByPhone(String phoneNumber) {
        for (SimpleUser user : this.users) {
            if (user.getSimCard().getPhoneNumber().equalsIgnoreCase(phoneNumber)) {
                return user;
            }
        }
        return null;
    }

    public Account getAccountByID(String accountID) {
        for (SimpleUser user : this.users) {
            if (user.getAccount().getAccountId().equalsIgnoreCase(accountID)) {
                return user.getAccount();
            }
        }
        return null;
    }

    public SimpleUser getUserByAccountID(String accountID) {
        for (SimpleUser user : this.users) {
            if (user.getAccount().getAccountId().equalsIgnoreCase(accountID)) {
                return user;
            }
        }
        return null;
    }

    public Account getAccountByCard(String creditID) {
        for (SimpleUser user : this.users) {
            if (user.getAccount().getCreditCard().getCreditCardId().equalsIgnoreCase(creditID)) {

                return user.getAccount();

            }
        }
        return null;
    }

    public SimpleUser getUserByCreditID(String creditID) {
        for (SimpleUser user : this.users) {
            if (user.getAccount().getCreditCard().getCreditCardId().equalsIgnoreCase(creditID)) {
                return user;
            }
        }
        return null;
    }

    public boolean existsCreditCard(String creditCardString) {
        for (SimpleUser user : this.users) {
            if (user.getAccount() != null) {
                if (user.getAccount().getCreditCard().getCreditCardId().equals(creditCardString)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean existsAccountID(String accountIDString) {
        for (SimpleUser user : this.users) {
            if (user.getAccount() != null) {
                if (user.getAccount().getAccountId().equals(accountIDString)) {
                    return true;
                }
            }
        }
        return false;
    }


    public int checkPhoneNumber(String phoneNumber, String need) {
        String phoneRegEx = "^09[0-9]{9}$";
        Pattern phonePattern = Pattern.compile(phoneRegEx);
        Matcher phoneMatcher = phonePattern.matcher(phoneNumber);
        if (!phoneMatcher.matches()) {
            return 1;
        } else if ("doesn't matter".equals(need)) {
            return 0;
        }
        for (SimpleUser user : this.users) {
            if (phoneNumber.equalsIgnoreCase(user.getSimCard().getPhoneNumber()) && "shouldn't exist".equals(need)) {
                return 2;
            } else if (phoneNumber.equalsIgnoreCase(user.getSimCard().getPhoneNumber())) {
                return 0;
            }
        }
        if ("should exist".equals(need)) {
            return 3;
        }
        return 0;
    }

    public int checkSecurityNumber(String securityNumber) {
        String ssnRegEx = "^[0-9]{10}$";
        Pattern ssnPattern = Pattern.compile(ssnRegEx);
        Matcher ssnMatcher = ssnPattern.matcher(securityNumber);
        if (!ssnMatcher.matches()) {
            return 1;
        }
        for (SimpleUser user : this.users) {
            if (securityNumber.equalsIgnoreCase(user.getSecurityNumber())) {
                return 2;
            }
        }
        return 0;
    }

    public void signUp(String[] names, String phoneNumber, String securityNumber, String password) {
        String name = names[0];
        String lastName = names[1];
        SimCard simCard = this.getSim(MainActivity.getFariBank(), phoneNumber);
        Authentication newAuthentication = new Authentication(phoneNumber);
        this.addUser(new SimpleUser(name, lastName, simCard, securityNumber, password, newAuthentication));
        this.addAuthentication(newAuthentication);
    }


    public SimCard getSim(NeoBank neoBank, String phoneNumber) {
        SimCard simCard = neoBank.getManagerData().getSimCard(phoneNumber);
        if (simCard == null) {
            simCard = new SimCard(phoneNumber, false);
            neoBank.getManagerData().addSimCard(simCard);
        }
        return simCard;
    }

}

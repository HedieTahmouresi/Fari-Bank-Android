package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Data{
    private List<Request> requests;
    private List<SimpleUser> users;


    public Data() {
        this.requests = new ArrayList<>();
        this.users = new ArrayList<>();
    }


    public void addRequest(Request request) {
        this.requests.add(request);
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

    public Account getAccountByID(String accountID){
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

    public Account getAccountByCard(String creditID){
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

    public void signUp(String name, String lastName, String phoneNumber, String securityNumber, String password) {
        SimCard simCard = this.getSim(MainActivity.getFariBank(), phoneNumber);
        Authentication newAuthentication = new Authentication(phoneNumber);
        this.addUser(new SimpleUser(name, lastName, simCard, securityNumber, password, newAuthentication));
        this.addAuthentication(newAuthentication);
    }


    public SimCard getSim(NeoBank neoBank, String phoneNumber){
        SimCard simCard = neoBank.getManagerData().getSimCard(phoneNumber);
        if (simCard == null) {
            simCard = new SimCard(phoneNumber, false);
            neoBank.getManagerData().addSimCard(simCard);
        }
        return simCard;
    }



/*

    public List<Request> allRequests(Admin currentAdmin) {
        if (this.requests.isEmpty()) {
            System.out.println(ColorConsole.RED + "There are no requests" + ColorConsole.RESET);
            return null;
        }
        List<Request> requestList = new ArrayList<>();
        for (Request request : requests){
            if (currentAdmin.getAbilities().hasAbility(request.getSection())){
                requestList.add(request);
            }
        }
        return requestList;
    }


    public List<Request> filteredRequestsBySection(Admin currentAdmin) {
        RequestSection section = input.nextRequestSections();
        if (section == null) {
            return null;
        }
        if (!currentAdmin.getAbilities().hasAbility(section)){
            System.out.println(ColorConsole.RED + "You cant choose this section you are not allowed to" + ColorConsole.RESET);
        }
        List<Request> list = new ArrayList<>();
        if (this.requests.isEmpty()) {
            System.out.println(ColorConsole.RED + "There are no requests" + ColorConsole.RESET);
            return null;
        }
        for (Request request : this.requests) {
            if (request.getSection().equals(section) ) {
                list.add(request);
            }
        }
        return list;
    }

    public List<Request> filteredRequestsByPerson(NeoBank neoBank, Admin currentAdmin) {
        List<Request> list = new ArrayList<>();
        String phoneNumber = input.nextRequestPerson(neoBank);
        if (phoneNumber == null) {
            return null;
        } else if (this.requests.isEmpty()) {
            System.out.println(ColorConsole.RED + "There are no requests" + ColorConsole.RESET);
            return null;
        }
        for (Request request : this.requests) {
            if (request.getPhoneNumber().equals(phoneNumber) && currentAdmin.getAbilities().hasAbility(request.getSection())) {
                list.add(request);
            }
        }
        return list;
    }

    public List<Request> filteredRequestsByStatus(Admin currentAdmin) {
        RequestStatus status = input.nextRequestStatus();
        List<Request> list = new ArrayList<>();
        if (status == null) {
            return null;
        } else if (this.requests.isEmpty()) {
            System.out.println(ColorConsole.RED + "There are no requests" + ColorConsole.RESET);
            return null;
        }
        for (Request request : this.requests) {
            if (request.getStatus().equals(status) && currentAdmin.getAbilities().hasAbility(request.getSection())) {
                list.add(request);
            }
        }
        return list;
    }

    public List<SimpleUser> search() {
        List<SimpleUser> result = new ArrayList<>(this.users);
        String name = input.nextSearchName();
        String lastName = input.nextSearchLastName();
        String phoneNumber = input.nextSearchPhoneNumber();
        if (name == null && lastName == null && phoneNumber == null) {
            return null;
        }
        if (name != null) {
            result = fuzzySearchName(name, result);
        }
        if (lastName != null) {
            result = fuzzySearchLastName(lastName, result);
        }
        if (phoneNumber != null) {
            result = fuzzySearchPhone(phoneNumber, result);
        }
        if (result.isEmpty()) {
            System.out.println(ColorConsole.RED + "no user found matching those descriptions!" + ColorConsole.RESET);
        }
        return result;
    }

    public List<SimpleUser> fuzzySearchName(String name, List<SimpleUser> list) {
        List<SimpleUser> result = new ArrayList<>();
        for (SimpleUser user : list) {
            Fuzzy similarity = new Fuzzy(name, user.getName());
            if (similarity.getSimilarity() >= 0.6) {
                result.add(user);
            }
        }
        return result;
    }

    public static List<SimpleUser> fuzzySearchLastName(String lastName, List<SimpleUser> list) {
        List<SimpleUser> result = new ArrayList<>();
        for (SimpleUser user : list) {
            Fuzzy similarity = new Fuzzy(lastName, user.getLastName());
            if (similarity.getSimilarity() >= 0.6) {
                result.add(user);
            }
        }
        return result;
    }

    public static List<SimpleUser> fuzzySearchPhone(String phoneNumber, List<SimpleUser> list) {
        List<SimpleUser> result = new ArrayList<>();
        for (SimpleUser user : list) {
            Fuzzy similarity = new Fuzzy(phoneNumber, user.getSimCard().getPhoneNumber());
            if (similarity.getSimilarity() >= 0.6) {
                result.add(user);
            }
        }
        return result;
    }


    public List<SimpleUser> getAllUsers() {
        return new ArrayList<>(this.users);
    }

    public List<Object> getUsers(){
        return new ArrayList<>(this.users);
    }

     */
}

package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SimpleUser extends UserPerson {
    private String securityNumber;
    private String password;
    private Account account;
    private boolean contactOption;
    private Authentication authenticated;
    private boolean hasRemainsFund;
    private List<Contact> contacts;
    private List<Request> requests;
    private List<Fund> funds;
    private boolean blocked;

    public List<Fund> getFunds() {
        return funds;
    }

    public String getSecurityNumber() {
        return securityNumber;
    }

    public void setSecurityNumber(String securityNumber) {
        this.securityNumber = securityNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public boolean isContactOption() {
        return contactOption;
    }

    public void setContactOption(boolean contactOption) {
        this.contactOption = contactOption;
    }

    public Authentication getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Authentication authenticated) {
        this.authenticated = authenticated;
    }

    public boolean isHasRemainsFund() {
        return hasRemainsFund;
    }

    public void setHasRemainsFund(boolean hasRemainsFund) {
        this.hasRemainsFund = hasRemainsFund;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public SimpleUser(String name, String lastName, SimCard simCard, String securityNumber, String password, Authentication authentication) {
        super(name, lastName, simCard);
        setSecurityNumber(securityNumber);
        setPassword(password);
        this.requests = new ArrayList<>();
        this.contacts = new ArrayList<>();
        setContactOption(true);
        setAuthenticated(authentication);
        this.funds = new ArrayList<>();
        setHasRemainsFund(false);
        setBlocked(false);
    }

    public boolean contactExistence(Contact currentContact) {
        for (Contact contact : contacts) {
            if (contact.equals(currentContact)) {
                return true;
            }
        }
        return false;
    }

    public List<Contact> getContacts() {
        return this.contacts;
    }

    public List<Request> getRequests() {
        return this.requests;
    }


    public void addContact(Contact newContact) {
        contacts.add(newContact);
    }

    public void addRequest(Request newRequest) {
        requests.add(newRequest);
    }

    public void removeContact(Contact contact) {
        this.contacts.remove(contact);
    }

    public void removeFund(Fund fund) {
        this.funds.remove(fund);
    }

    public void addFund(Fund fund) {
        this.funds.add(fund);
    }


    public void transferByCreditID(SimpleUser receiver, String value, Context context) {
        List<Boolean> facts = new ArrayList<>();
        facts.add(false);
        facts.add(false);
        this.getAccount().transfer(value, receiver, facts, context);
    }

    public void transferByAccountID(SimpleUser receiver, String value, Context context) {
        List<Boolean> facts = new ArrayList<>();
        facts.add(false);
        facts.add(false);
        this.getAccount().transfer(value, receiver, facts, context);
    }

    public void transferByContact(SimpleUser receiver, String value, Context context) {
        List<Boolean> facts = new ArrayList<>();
        facts.add(false);
        facts.add(false);
        this.getAccount().transfer(value, receiver, facts, context);
    }

    public Contact findContact(String phoneNumber) {
        for (Contact contact : contacts) {
            if (contact.getSimCard().getPhoneNumber().equals(phoneNumber)) {
                return contact;
            }
        }
        return null;
    }

    public Fund findFund(String fundID){
        for (Fund fund : funds){
            if (fund.getFundID().equals(fundID)){
                return fund;
            }
        }
        return null;
    }

    public void addRequest(RequestSection section, String text, NeoBank neoBank, Context context) {
        Request newRequest = new Request(text, section, this.getSimCard().getPhoneNumber());
        this.addRequest(newRequest);
        neoBank.getBankData().addRequest(this, newRequest);
        Toast.makeText(context, "Request successfully noted!", Toast.LENGTH_LONG).show();
    }


    public RemainsFund getRemainsFund() {
        for (Fund fund : this.funds) {
            if (fund instanceof RemainsFund) {
                return (RemainsFund) fund;
            }
        }
        return null;
    }

    public void addFund(String fundType, Context context) {
        switch (fundType) {
            case "1", "Savings Fund" -> {
                SavingsFund newFund = new SavingsFund(this, MainActivity.getFariBank());
                this.addFund(newFund);
                Toast.makeText(context, "Fund successfully created!", Toast.LENGTH_SHORT).show();
            }
            case "2", "Remains Fund" -> {
                if (this.isHasRemainsFund()) {
                    Toast.makeText(context, "You can't have 2 remains funds", Toast.LENGTH_SHORT).show();
                    return;
                }
                RemainsFund newFund = new RemainsFund(this, MainActivity.getFariBank());
                this.addFund(newFund);
                this.setHasRemainsFund(true);
                Toast.makeText(context, "Fund successfully created!", Toast.LENGTH_SHORT).show();
            }
            case "3", "Bonus Fund" -> {
            }
            default -> System.out.println("haha");
        }
    }

    public void createBonusFund( Context context, String value, String numOfMonths) {
        BonusFund newFund = new BonusFund(this, MainActivity.getFariBank(), Integer.parseInt(numOfMonths));
        this.addFund(newFund);
        newFund.setBalance(Double.parseDouble(value));
        MainActivity.getFariBank().getManagerData().addBonusFund(newFund);
        BonusThread thread = new BonusThread(MainActivity.getFariBank(), newFund);
        Thread newThread = new Thread(thread);
        newThread.start();
    }
/*
    public void showFunds(NeoBank neoBank) {
        if (this.funds == null || this.funds.isEmpty()) {
            System.out.println(ColorConsole.RED + "There are no funds" + ColorConsole.RESET);
            return;
        }
        Pagination<Fund> fundList = new Pagination<>(this.funds, 5);
        String command;
        do {
            if (this.funds.isEmpty()) {
                System.out.println(ColorConsole.RED + "There are no funds" + ColorConsole.RESET);
                return;
            }
            fundList.showPage();
            System.out.println(ColorConsole.BLUE + "Enter 'next' to go to the next page, 'previous' to go back or the number of the transaction you want" + ColorConsole.RESET);
            command = input.nextLine();
            if (!input.exitPoint(command)) {
                return;
            } else if (command.matches("[0-9]+")) {
                this.selectFund(command, neoBank);
            } else if ("next".equals(command) || "previous".equals(command)) {
                fundList.changePage(command);
            } else {
                System.out.println(ColorConsole.RED + "No other option! Please try again!" + ColorConsole.RESET);
            }
        } while (!"return".equals(command));
    }

    public void selectFund(String answer, NeoBank neoBank) {
        if (Integer.parseInt(answer) > 0 && Integer.parseInt(answer) <= this.funds.size()) {
            int index = Integer.parseInt(answer) - 1;
            this.funds.get(index).manageFund(neoBank);
            return;
        }
        System.out.println(ColorConsole.RED + "Wrong input try again!" + ColorConsole.RESET);
    }

     */
}
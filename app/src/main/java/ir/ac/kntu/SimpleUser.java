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

    /*
    public void changePassword() {
        System.out.println(ColorConsole.CYAN_BOLD + "Would you like to change your password? (previous phone number : " + ColorConsole.PURPLE + this.getPassword() + ColorConsole.CYAN_BOLD + ")" + ColorConsole.RESET);
        String answer = input.nextLine();
        if ("no".equalsIgnoreCase(answer) || !input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            String password = input.nextPassword();
            if (password != null) {
                this.setPassword(password);
            }
            return;
        } else {
            System.out.println(ColorConsole.RED + "Wrong input! Try again" + ColorConsole.RESET);
        }
        this.changePassword();
    }

    public void editSignUpInfo(NeoBank neoBank) {
        neoBank.getBankData().removeAuthentication(this);
        this.changeName();
        this.changeLastName();
        this.changePhoneNumber(neoBank, this, "user");
        this.changeSecurityNumber(neoBank);
        this.changePassword();
        neoBank.getBankData().addAuthentication(new Authentication(this.getSimCard().getPhoneNumber()));
        this.setAuthenticated(new Authentication(this.getSimCard().getPhoneNumber()));
        System.out.println(ColorConsole.GREEN + "Sign Up info changed!" + ColorConsole.RESET);
    }

    public void changeInfo(NeoBank neoBank) {
        System.out.println(ColorConsole.BLUE + "Do you want to change your information?" + ColorConsole.RESET);
        String answer = input.nextLine();
        if ("no".equalsIgnoreCase(answer) || !input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            this.editSignUpInfo(neoBank);
            return;
        } else {
            System.out.println(ColorConsole.RED + "Wrong input! Try again!" + ColorConsole.RESET);
        }
        this.changeInfo(neoBank);
    }
*/

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

    /*
        public void transferByRecent(NeoBank neoBank, CentralBank centralBank) {
            Recent recent = this.getAccount().showRecentList(neoBank);
            if (recent == null) {
                return;
            }
            SimpleUser receiver = recent.getPerson();
            boolean byContact = recent.isByContact();
            if (byContact) {
                if (!checkContactForTransfer(receiver)) {
                    return;
                }
            }
            String value = input.nextValue(receiver, 8000000.0);
            if (value == null) {
                return;
            }
            this.getAccount().showTransferOptionsForRecent(value, receiver, neoBank, centralBank);
        }

        public boolean checkContactForTransfer(SimpleUser receiver) {
            if (!receiver.contactExistence(new Contact(" ", " ", this.getSimCard()))) {
                System.out.println(ColorConsole.RED + "You can't transfer to a contact if they don't have you as a contact!" + ColorConsole.RESET);
                return false;
            } else if (!receiver.isContactOption()) {
                System.out.println(ColorConsole.RED + "You can't transfer money to this user by contact!" + ColorConsole.RESET);
                return false;
            }
            return true;
        }
    */
    public Contact findContact(String phoneNumber) {
        for (Contact contact : contacts) {
            if (contact.getSimCard().getPhoneNumber().equals(phoneNumber)) {
                return contact;
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
/*
    public void addFund(NeoBank neoBank) {
        String fundType = input.nextFundType();
        if (fundType == null) {
            return;
        }
        switch (fundType) {
            case "1", "Savings Fund" -> {
                SavingsFund newFund = new SavingsFund(this, neoBank);
                this.addFund(newFund);
                System.out.println(ColorConsole.GREEN + "Fund successfully created!" + ColorConsole.RESET);
                newFund.transferToFund(neoBank, "Savings Fund");
            }
            case "2", "Remains Fund" -> {
                if (this.isHasRemainsFund()) {
                    System.out.println(ColorConsole.RED + "You can't have 2 remains funds" + ColorConsole.RESET);
                    return;
                }
                RemainsFund newFund = new RemainsFund(this, neoBank);
                this.addFund(newFund);
                this.setHasRemainsFund(true);
                System.out.println(ColorConsole.GREEN + "Fund successfully created!" + ColorConsole.RESET);
            }
            case "3", "Bonus Fund" -> {
                this.createBonusFund(neoBank);
            }
            default -> System.out.println("haha");
        }
    }

    public void createBonusFund(NeoBank neoBank) {
        System.out.println(ColorConsole.BLUE + "How long would you like to have this fund for?" + ColorConsole.RESET);
        String answer = input.nextLine();
        if (!input.exitPoint(answer)) {
            return;
        } else if (!answer.matches("[0-9]+")) {
            System.out.println(ColorConsole.RED + "Wrong format!" + ColorConsole.RESET);
            this.createBonusFund(neoBank);
        }
        BonusFund newFund = new BonusFund(this, neoBank, Integer.parseInt(answer));
        newFund.transferToFund(neoBank, "Bonus Fund");
        if (newFund.getBalance() == 0) {
            System.out.println(ColorConsole.RED + "You can't have an empty bonus fund!" + ColorConsole.RESET);
            return;
        }
        this.addFund(newFund);
        neoBank.getManagerData().addBonusFund(newFund);
        BonusThread thread = new BonusThread(neoBank, newFund);
        Thread newThread = new Thread(thread);
        newThread.start();
        System.out.println(ColorConsole.GREEN + "Fund successfully created" + ColorConsole.RESET);
    }

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
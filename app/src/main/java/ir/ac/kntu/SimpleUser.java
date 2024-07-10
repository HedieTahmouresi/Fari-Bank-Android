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

    public List<Contact> getContacts(){
        return this.contacts;
    }

    public List<Request> getRequests(){
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

    /*public Contact showContacts() {
        if (this.contacts == null || this.contacts.isEmpty()) {
            return null;
        }
        Pagination<Contact> contactList = new Pagination<>(this.contacts, 5);
        String command;
        do {
            contactList.showPage();
            System.out.println(ColorConsole.BLUE + "Enter 'next' to go to the next page, 'previous' to go back or the number of the transaction you want" + ColorConsole.RESET);
            command = input.nextLine();
            if (!input.exitPoint(command)) {
                return null;
            } else if (command.matches("[0-9]+")) {
                return this.selectContact(command);
            } else if ("next".equals(command) || "previous".equals(command)) {
                contactList.changePage(command);
            } else {
                System.out.println(ColorConsole.RED + "No other option! Please try again!" + ColorConsole.RESET);
            }
        } while (!"return".equals(command));
        return null;
    }

    public Contact selectContact(String answer) {
        if (Integer.parseInt(answer) > 0 && Integer.parseInt(answer) < contacts.size() + 1) {
            return this.contacts.get(Integer.parseInt(answer) - 1);
        }
        System.out.println(ColorConsole.RED + "Index Out of Bound! Try again!" + ColorConsole.RESET);
        return null;
    }

    public void changeSecurityNumber(NeoBank neoBank) {
        System.out.println(ColorConsole.CYAN_BOLD + "Would you like to change your social security number? (previous phone number : " + ColorConsole.PURPLE + this.getSecurityNumber() + ColorConsole.CYAN_BOLD + ")" + ColorConsole.RESET);
        String answer = input.nextLine();
        if ("no".equalsIgnoreCase(answer) || !input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            String securityNumber = input.nextSecurityNumber(neoBank.getBankData());
            if (securityNumber != null) {
                this.setSecurityNumber(securityNumber);
            }
            return;
        } else {
            System.out.println(ColorConsole.RED + "Wrong input! Try again" + ColorConsole.RESET);
        }
        this.changeSecurityNumber(neoBank);
    }

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

    public void transferByCreditID(NeoBank neoBank, String value, String creditCardId){
        if (creditCardId==null){
            return;
        }
        SimpleUser receiver = neoBank.getBankData().getUserByCreditID(creditCardId);
        boolean confirmed = input.nextConfirmation(receiver, value);
        if (!confirmed){
            System.out.println(ColorConsole.RED + "Transfer failed!" + ColorConsole.RESET);
            return;
        }
        List<Boolean> facts = new ArrayList<>();
        facts.add(false);
        facts.add(false);
        this.getAccount().transfer(neoBank, value, receiver, facts);
    }

    public void transferByAccountID(NeoBank neoBank, String value, String accountID) {
        if (accountID == null) {
            return;
        }
        SimpleUser receiver = neoBank.getBankData().getUserByAccountID(accountID);
        boolean confirmed = input.nextConfirmation(receiver, value);
        if (!confirmed) {
            System.out.println(ColorConsole.RED + "Transfer failed!" + ColorConsole.RESET);
            return;
        }
        List<Boolean> facts = new ArrayList<>();
        facts.add(false);
        facts.add(false);
        this.getAccount().transfer(neoBank, value, receiver, facts);
    }

    public void transferByContact(NeoBank neoBank) {
        Contact receiverContact = showContacts();
        if (receiverContact == null) {
            return;
        }
        SimpleUser receiver = neoBank.getBankData().getUserByPhone(receiverContact.getSimCard().getPhoneNumber());
        if (!this.checkContactForTransfer(receiver)) {
            return;
        }
        String value = input.nextValue(receiver, 8000000.0);
        if (value == null) {
            return;
        }
        this.getAccount().showOnlyFari(neoBank, value, receiver, true);
    }

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
/*
    public void showAccountInfo() {
        System.out.println(ColorConsole.PINK + "Your Account ID : " + ColorConsole.PURPLE + this.getAccount().getAccountId() + ColorConsole.RESET);
        System.out.println(ColorConsole.PINK + "Your Credit Card ID : " + ColorConsole.PURPLE + this.getAccount().getCreditCard().getCreditCardId() + ColorConsole.RESET);
    }

    public void changeOrSetPassCode() {
        if (this.getAccount().getCreditCard().hasSetPassword()) {
            this.getAccount().getCreditCard().changePassCode();
        } else {
            this.getAccount().getCreditCard().setPassCode();
        }
    }

    public void changeContactOption() {
        String changeCondition = "";
        if (this.isContactOption()) {
            changeCondition = "off";
        } else {
            changeCondition = "on";
        }
        System.out.println(ColorConsole.BLUE + "Would you like to turn it " + ColorConsole.PURPLE_BOLD + changeCondition + ColorConsole.BLUE + "?" + ColorConsole.RESET);
        String answer = input.nextLine();
        if (!input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer) && this.isContactOption()) {
            this.setContactOption(false);
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            this.setContactOption(true);
            return;
        } else if ("no".equalsIgnoreCase(answer)) {
            return;
        }
        System.out.println(ColorConsole.RED + "Please enter yes or no! I can't understand this!" + ColorConsole.RESET);
        this.changeContactOption();
    }

    public void addRequest(NeoBank neoBank) {
        RequestSection section = input.nextRequestSections();
        if (section == null) {
            return;
        }
        System.out.println(ColorConsole.BLUE_BOLD + "Please Enter your problem." + ColorConsole.RESET);
        String text = input.nextLine();
        if (!input.exitPoint(text)) {
            return;
        }
        Request newRequest = new Request(text, section, this.getSimCard().getPhoneNumber());
        this.addRequest(newRequest);
        neoBank.getBankData().addRequest(newRequest);
        System.out.println(ColorConsole.GREEN + "Request successfully noted!" + ColorConsole.RESET);
    }

    public void showRequests(NeoBank neoBank) {
        if (requests == null || requests.isEmpty()) {
            return;
        }
        Pagination<Request> requestList = new Pagination<>(requests, 5);
        String command;
        do {
            requestList.showPage();
            System.out.println(ColorConsole.BLUE + "Enter 'next' to go to the next page, 'previous' to go back or the number of the transaction you want" + ColorConsole.RESET);
            command = input.nextLine();
            if (!input.exitPoint(command)) {
                return;
            } else if (command.matches("[0-9]+")) {
                this.selectRequest(command, neoBank);
            } else if ("next".equals(command) || "previous".equals(command)) {
                requestList.changePage(command);
            } else {
                System.out.println(ColorConsole.RED + "No other option! Please try again!" + ColorConsole.RESET);
            }
        } while (!"return".equals(command));
    }

    public void selectRequest(String answer, NeoBank neoBank) {
        if (Integer.parseInt(answer) > 0 && Integer.parseInt(answer) <= this.requests.size()) {
            int index = Integer.parseInt(answer) - 1;
            this.requests.get(index).showInfo(neoBank.getBankData());
            return;
        }
        System.out.println(ColorConsole.RED + "Wrong input try again!" + ColorConsole.RESET);

    }


    public void showUserInfo(NeoBank neoBank) {
        System.out.println(ColorConsole.PURPLE + "Name : " + ColorConsole.BLUE_BOLD + this.getName() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Last Name : " + ColorConsole.BLUE_BOLD + this.getLastName() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Phone Number : " + ColorConsole.BLUE_BOLD + this.getSimCard().getPhoneNumber() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Account Id : " + ColorConsole.BLUE_BOLD + this.getAccount().getAccountId() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Transactions : " + ColorConsole.RESET);
        this.getAccount().showTransactionList(neoBank, this.getAccount().addAllTransactions(neoBank));
    }

    public RemainsFund getRemainsFund() {
        for (Fund fund : this.funds) {
            if (fund instanceof RemainsFund) {
                return (RemainsFund) fund;
            }
        }
        return null;
    }

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

    @Override
    public String toString() {
        return ColorConsole.CYAN + "Simple User{" + super.toString() + ColorConsole.CYAN + "}" + ColorConsole.RESET;
    }

     */
}
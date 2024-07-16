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
    private List<Loan> loans;
    private List<LoanRequest> loanRequests;
    private int negativePoints;

    public List<Loan> getLoans() {
        return loans;
    }

    public Loan findLoan(String id) {
        for (Loan loan : this.loans) {
            if (id.equals(loan.getId())) {
                return loan;
            }
        }
        return null;
    }

    public void addLoanRequest(LoanRequest loanRequest) {
        this.loanRequests.add(loanRequest);
    }

    public List<LoanRequest> getLoanRequests() {
        return loanRequests;
    }

    public void addLoan(Loan loan) {
        this.loans.add(loan);
    }

    public int getNegativePoints() {
        return negativePoints;
    }

    public void setNegativePoints(int negativePoints) {
        this.negativePoints = negativePoints;
    }

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
        setNegativePoints(0);
        this.loanRequests = new ArrayList<>();
        this.loans = new ArrayList<>();
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

    public Fund findFund(String fundID) {
        for (Fund fund : funds) {
            if (fund.getFundID().equals(fundID)) {
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

    public void createBonusFund(Context context, String value, String numOfMonths) {
        double remains = this.isHasRemainsFund() ? this.getRemainsFund().calculateRemains(value) : 0;
        if (Double.parseDouble(value) + remains > this.getAccount().getBalance()) {
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        BonusFund newFund = new BonusFund(this, MainActivity.getFariBank(), Integer.parseInt(numOfMonths));
        this.addFund(newFund);
        this.getAccount().setBalance(this.getAccount().getBalance() - Double.parseDouble(value) - remains);
        newFund.setBalance(Double.parseDouble(value));
        Transaction newTransaction = new TransferInsideTransaction(Double.parseDouble(value), MainActivity.getFariBank().getTracingNumber(), "Account", "Bonus Fund", newFund.getFundID(), this.getAccount().getBalance());
        this.getAccount().addTransaction(newTransaction);
        newTransaction = new TransferInsideTransaction(Double.parseDouble(value), MainActivity.getFariBank().getTracingNumber() + 1, "Account", "Bonus Fund", newFund.getFundID(), newFund.getBalance());
        newFund.addTransaction(newTransaction);
        MainActivity.getFariBank().getManagerData().addBonusFund(newFund);
        MainActivity.getFariBank().setTracingNumber(MainActivity.getFariBank().getTracingNumber() + 2);
        BonusThread thread = new BonusThread(MainActivity.getFariBank(), newFund);
        Thread newThread = new Thread(thread);
        newThread.start();
    }


    public double getMinusSideTransfers() {
        double amount = 0;
        Transaction prev = this.getAccount().getTransactions().get(0);
        for (Transaction transaction : this.getAccount().getTransactions()) {
            if (transaction instanceof TransferInsideTransaction insideTransfer) {
                if ("Bonus Fund".equals(insideTransfer.getSender())) {
                    amount = amount + insideTransfer.getValue();
                }
            }
            prev = transaction;
        }
        return amount;
    }

}
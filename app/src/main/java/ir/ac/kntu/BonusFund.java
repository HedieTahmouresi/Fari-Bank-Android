package ir.ac.kntu;

import android.os.Parcel;

import androidx.annotation.NonNull;

import ir.ac.kntu.util.Calendar;

import java.time.*;

public class BonusFund extends Fund {
    private Instant manufacture;
    private Instant expiration;
    private Instant lastDeposit;

    public Instant getLastDeposit() {
        return lastDeposit;
    }

    public void setLastDeposit(Instant lastDeposit) {
        this.lastDeposit = lastDeposit;
    }

    public Instant getExpiration() {
        return expiration;
    }

    public void setExpiration(Instant expiration) {
        this.expiration = expiration;
    }

    public Instant getManufacture() {
        return manufacture;
    }

    public void setManufacture(Instant manufacture) {
        this.manufacture = manufacture;
    }

    public BonusFund(SimpleUser owner, NeoBank neoBank, int numOfMonths) {
        super(owner, neoBank);
        Instant now = Calendar.now();
        setManufacture(now);
        ZonedDateTime zonedDateTime = now.atZone(ZoneId.systemDefault());
        ZonedDateTime futureDateTime = zonedDateTime.plusMonths(numOfMonths);
        Instant futureInstant = futureDateTime.toInstant();
        setExpiration(futureInstant);
        setLastDeposit(now);
    }

    /*@Override
    public void dissolveFund(NeoBank neoBank) {
        if (Calendar.now().isBefore(this.getExpiration())) {
            ZonedDateTime zonedDateTime = this.getExpiration().atZone(ZoneId.systemDefault());
            LocalDate datePart = zonedDateTime.toLocalDate();
            ZonedDateTime nowZonedDateTime = Calendar.now().atZone(ZoneId.systemDefault());
            LocalDate nowDate = nowZonedDateTime.toLocalDate();
            System.out.println(ColorConsole.PINK + "You can't do anything with this fund!" + ColorConsole.RESET);
            System.out.println(ColorConsole.CYAN + "The expiration date : " + ColorConsole.PURPLE + datePart + ColorConsole.RESET);
            System.out.println(ColorConsole.CYAN + "Today : " + ColorConsole.PURPLE + nowDate + ColorConsole.RESET);
            return;
        }
        System.out.println(ColorConsole.RED + "The expiration is due!" + ColorConsole.RESET);
        this.getOwner().getAccount().setBalance(this.getOwner().getAccount().getBalance() + this.getBalance());
        Transaction newTransaction = new TransferInsideTransaction(this.getBalance(), neoBank.getTracingNumber(), "Bonus Fund", "Account", this.getFundID());
        neoBank.setTracingNumber(neoBank.getTracingNumber()+1);
        this.getOwner().getAccount().addTransaction(newTransaction, "Inside Transfer");
        double remains = this.getOwner().isHasRemainsFund() ? this.getOwner().getRemainsFund().calculateRemains(Double.toString(this.getBalance())) : 0;
        if (this.getOwner().isHasRemainsFund()) {
            this.getOwner().getRemainsFund().saveRemains(remains, neoBank);
        }
        this.getOwner().removeFund(this);
    }

    public void transferBonus(NeoBank neoBank) {
        double bonus = (this.getBalance() * neoBank.getManagerData().getBonusPercentage()) / 100;
        this.getOwner().getAccount().setBalance(this.getOwner().getAccount().getBalance() + bonus);
    }

    @Override
    public void manageFund(NeoBank neoBank) {
        this.dissolveFund(neoBank);
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Bonus " + super.toString();
    }

    public boolean depositBonus(NeoBank neoBank){
        Instant now = Calendar.now();
        Duration oneDay = Duration.ofDays(1);
        Instant endTime = this.getExpiration().plus(oneDay);
        if (now.isAfter(endTime)){
            return false;
        }
        ZonedDateTime zonedDateTime = this.getLastDeposit().atZone(ZoneId.systemDefault());
        ZonedDateTime futureDateTime = zonedDateTime.plusMonths(1);
        Instant nextDeposit = futureDateTime.toInstant();
        if (now.isBefore(nextDeposit) ){
            return false;
        }
        this.transferBonus(neoBank);
        double bonus = (this.getBalance() * neoBank.getManagerData().getBonusPercentage()) / 100;
        setLastDeposit(nextDeposit);
        Transaction newTransaction = new TransferInsideTransaction(bonus, neoBank.getTracingNumber(), "Bonus Fund", "Account", this.getFundID());
        neoBank.setTracingNumber(neoBank.getTracingNumber()+1);
        this.getOwner().getAccount().addTransaction(newTransaction, "Inside Transfer");
        return true;
    }

     */
}

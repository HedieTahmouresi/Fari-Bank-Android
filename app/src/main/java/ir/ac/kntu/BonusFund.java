package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

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

    public void dissolveFund(Context context) {
        NeoBank neoBank = MainActivity.getFariBank();
        if (Calendar.now().isBefore(this.getExpiration())) {
            ZonedDateTime zonedDateTime = this.getExpiration().atZone(ZoneId.systemDefault());
            LocalDate datePart = zonedDateTime.toLocalDate();
            ZonedDateTime nowZonedDateTime = Calendar.now().atZone(ZoneId.systemDefault());
            LocalDate nowDate = nowZonedDateTime.toLocalDate();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Account Info").setMessage("You can't do anything with this fund!" + "\nThe expiration date : " + datePart + "\nToday : " + nowDate).setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
            AlertDialog alert = builder.create();
            alert.show();
            return;
        }
        Toast.makeText(context, "The expiration is due!", Toast.LENGTH_SHORT).show();
        this.getOwner().getAccount().setBalance(this.getOwner().getAccount().getBalance() + this.getBalance());
        Transaction newTransaction = new TransferInsideTransaction(this.getBalance(),neoBank.getTracingNumber(), "Bonus Fund", "Account", this.getFundID());
        neoBank.setTracingNumber(neoBank.getTracingNumber()+1);
        this.getOwner().getAccount().addTransaction(newTransaction);
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
        this.getOwner().getAccount().addTransaction(newTransaction);
        return true;
    }
}

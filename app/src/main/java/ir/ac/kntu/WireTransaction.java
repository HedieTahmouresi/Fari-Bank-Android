package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class WireTransaction extends TransferTransaction {
    private boolean hasBeenDone;

    public boolean isHasBeenDone() {
        return hasBeenDone;
    }

    public void setHasBeenDone(boolean hasBeenDone) {
        this.hasBeenDone = hasBeenDone;
    }

    public WireTransaction(double value, int tracingNumber, UserPerson receiver, String receiverInfo, String sign, SimpleUser sender, Boolean isReceiver) {
        super(value, tracingNumber, receiver, false, receiverInfo, sign, sender, isReceiver);
        setHasBeenDone(false);
    }

    public void completeTransaction(CentralBank centralBank, NeoBank neoBank) {
        if (this.isHasBeenDone()) {
            return;
        }
        double remains = this.getSender().isHasRemainsFund() ? this.getSender().getRemainsFund().calculateRemains(Double.toString(this.getValue())) : 0;
        double removeValue = this.getValue() + neoBank.getManagerData().getWireWage() + remains;
        this.getSender().getAccount().setBalance(this.getSender().getAccount().getBalance() - removeValue);
        this.getSender().getAccount().addTransaction(new WireTransaction(removeValue, this.getTracingNumber(), this.getReceiver(), this.getReceiverInfo(), this.getSign(), this.getSender(), false));
        this.getSender().getAccount().addRecentCentral(this, this.getReceiver().getSimCard().getPhoneNumber(), centralBank);
        SimpleUser receiver = centralBank.getUserBySim(this.getReceiver().getSimCard().getPhoneNumber());
        receiver.getAccount().setBalance(receiver.getAccount().getBalance() + this.getValue());
        receiver.getAccount().addTransaction(new WireTransaction(this.getValue(), neoBank.getTracingNumber() + 1, receiver, receiver.getAccount().getAccountId(), "+", this.getSender(), true));
        neoBank.setTracingNumber(neoBank.getTracingNumber() + 2);
        if (this.getSender().isHasRemainsFund()) {
            this.getSender().getRemainsFund().saveRemains(remains, neoBank);
        }
        setHasBeenDone(true);
    }

}

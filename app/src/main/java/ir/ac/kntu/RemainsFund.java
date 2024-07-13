package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

public class RemainsFund extends Fund {

    public RemainsFund(SimpleUser owner, NeoBank neoBank) {
        super(owner, neoBank);
    }


    public double calculateRemains(String amount) {
        if (amount.contains("\\.")) {
            amount = amount.substring(0, amount.length() - 2);
        }
        int len = amount.length();
        double rem = (double) (3 * len) / 4;
        len = (int) Math.ceil(rem);
        if (Double.parseDouble(amount) % (Math.pow(10, len)) == 0) {
            return 0;
        }
        String value = amount.substring(amount.length() - len);
        double intValue = Double.parseDouble(value);
        return Math.pow(10, len) - intValue;
    }

    public void saveRemains(double value, NeoBank neoBank) {
        this.setBalance(this.getBalance() + value);
        Transaction newTransaction = new TransferInsideTransaction(this.getBalance(), neoBank.getTracingNumber(), "Account", "Remains Fund", this.getFundID());
        this.getOwner().getAccount().addTransaction(newTransaction);
    }

    @Override
    public void transferFromFund(NeoBank neoBank, String fundType, String value, Context context) {
        super.transferFromFund(neoBank, "Remains Fund", value, context);
    }

    @Override
    public void transferToFund(NeoBank neoBank, String fundType, String value, Context context) {
        super.transferToFund(neoBank, "Remains Fund", value, context);
    }

    @Override
    public void dissolveFund(NeoBank neoBank, Context context) {
        super.dissolveFund(neoBank, context);
        this.getOwner().setHasRemainsFund(false);
    }


}

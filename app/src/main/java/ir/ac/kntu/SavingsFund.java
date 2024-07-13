package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;

public class SavingsFund extends Fund {


    public SavingsFund(SimpleUser owner, NeoBank neoBank) {
        super(owner, neoBank);
    }

    @Override
    public void transferFromFund(NeoBank neoBank, String fundType, String value, Context context) {
        super.transferFromFund(neoBank, "Savings Fund", value, context);
    }

    @Override
    public void transferToFund(NeoBank neoBank, String fundType, String value, Context context) {
        super.transferToFund(neoBank, "Savings Fund", value, context);
    }

}

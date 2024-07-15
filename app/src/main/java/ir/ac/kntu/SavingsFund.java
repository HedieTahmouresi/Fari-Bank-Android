package ir.ac.kntu;

import android.content.Context;
import android.os.Parcel;
import android.widget.TextView;

public class SavingsFund extends Fund {


    public SavingsFund(SimpleUser owner, NeoBank neoBank) {
        super(owner, neoBank);
    }

    @Override
    public void transferFromFund(String fundType, String value, Context context, TextView textView) {
        super.transferFromFund("Savings Fund", value, context, textView);
    }

    @Override
    public void transferToFund(String fundType, String value, Context context, TextView textView) {
        super.transferToFund("Savings Fund", value, context, textView);
    }

}

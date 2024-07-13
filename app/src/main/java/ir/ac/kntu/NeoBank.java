package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;
import java.util.Random;

public class NeoBank {
    private Data bankData;
    private int tracingNumber;
    private String creditCardStarter;
    private int baseFundID;
    private ManagerData managerData;


    public NeoBank(String creditCardStarter) {
        Data newData = new Data();
        setManagerData(new ManagerData(newData));
        setBankData(newData);
        Random random = new Random();
        setTracingNumber(random.nextInt(8999999) + 1000000);
        setBaseFundID(random.nextInt(999999999));
        setCreditCardStarter(creditCardStarter);

    }

    public String getCreditCardStarter() {
        return creditCardStarter;
    }

    public void setCreditCardStarter(String creditCardStarter) {
        this.creditCardStarter = creditCardStarter;
    }

    public ManagerData getManagerData() {
        return managerData;
    }

    public void setManagerData(ManagerData managerData) {
        this.managerData = managerData;
    }


    public int getBaseFundID() {
        return baseFundID;
    }

    public void setBaseFundID(int baseFundID) {
        this.baseFundID = baseFundID;
    }


    public int getTracingNumber() {
        return tracingNumber;
    }

    public void setTracingNumber(int tracingNumber) {
        this.tracingNumber = tracingNumber;
    }


    public Data getBankData() {
        return bankData;
    }

    public void setBankData(Data bankData) {
        this.bankData = bankData;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NeoBank neoBank)) {
            return false;
        }
        return Objects.equals(getCreditCardStarter(), neoBank.getCreditCardStarter());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getCreditCardStarter());
    }

}

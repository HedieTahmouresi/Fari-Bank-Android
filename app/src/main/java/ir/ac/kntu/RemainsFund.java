package ir.ac.kntu;

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
        if (Double.parseDouble(amount)%(Math.pow(10,len))==0){
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
/*
    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Remains " + ColorConsole.RESET + super.toString();
    }

    @Override
    public void transfer(NeoBank neoBank) {
        System.out.println(ColorConsole.BLUE + "What would you like to do?" + ColorConsole.RESET);
        System.out.println(ColorConsole.BLUE + "  1. Transfer from your Fund" + ColorConsole.RESET);
        System.out.println(ColorConsole.BLUE + "  1. Transfer to your Fund" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "1", "Transfer from your Fund":
                this.transferFromFund(neoBank, "Remains Fund");
                break;
            case "2", "Transfer to your Fund":
                this.transferToFund(neoBank, "Remains Fund");
                break;
            default:
                if (!input.exitPoint(answer)) {
                    return;
                }
                System.out.println(ColorConsole.RED + "No other Option! Try again! " + ColorConsole.RESET);
                break;
        }
        this.transfer(neoBank);
    }

    @Override
    public void dissolveFund(NeoBank neoBank) {
        super.dissolveFund(neoBank);
        this.getOwner().setHasRemainsFund(false);
    }

 */
}

package ir.ac.kntu;

import android.os.Parcel;

public class SavingsFund extends Fund {


    public SavingsFund(SimpleUser owner, NeoBank neoBank) {
        super(owner, neoBank);
    }

/*
    @Override
    public void transfer(NeoBank neoBank) {
        System.out.println(ColorConsole.BLUE + "What would you like to do?" + ColorConsole.RESET);
        System.out.println(ColorConsole.BLUE + "  1. Transfer from your Fund" + ColorConsole.RESET);
        System.out.println(ColorConsole.BLUE + "  1. Transfer to your Fund" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "1", "Transfer from your Fund":
                super.transferFromFund(neoBank, "Savings Fund");
                break;
            case "2", "Transfer to your Fund":
                super.transferToFund(neoBank, "Savings Fund");
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

 */

    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Savings " + ColorConsole.RESET + super.toString();
    }
}

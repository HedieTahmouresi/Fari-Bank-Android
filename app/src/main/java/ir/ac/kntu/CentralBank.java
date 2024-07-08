package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CentralBank{
    private List<NeoBank> banks;


    public CentralBank(){
        this.banks = new ArrayList<>();
    }


    public void addBank(NeoBank neoBank){
        this.banks.add(neoBank);
    }

    public NeoBank findBankByCreditCard(String creditCardStarter){
        for (NeoBank bank : banks){
            if (bank.getCreditCardStarter().equals(creditCardStarter)){
                return bank;
            }
        }
        return null;
    }

    public boolean sameBank(SimpleUser first, SimpleUser second){
        NeoBank firstBank = null;
        for(NeoBank bank : banks){
            if (bank.getBankData().getUserByPhone(first.getSimCard().getPhoneNumber())!=null){
                firstBank = bank;
            }
        }
        NeoBank secondBank =  null;
        for(NeoBank bank : banks){
            if (bank.getBankData().getUserByPhone(second.getSimCard().getPhoneNumber())!=null){
                secondBank = bank;
            }
        }
        return secondBank.equals(firstBank);
    }

    public Account existsAccountId(String accountId){
        for (NeoBank bank : banks){
            if (bank.getBankData().existsAccountID(accountId)){
                return bank.getBankData().getUserByAccountID(accountId).getAccount();

            }
        }
        return null;
    }

    public Account existsCreditCardId(String creditID){
        for (NeoBank bank : banks){
            if(bank.getBankData().existsCreditCard(creditID)){
                return bank.getBankData().getAccountByCard(creditID);
            }
        }
        return null;
    }

    public SimpleUser getUserBySim(String phoneNumber){
        for(NeoBank bank : banks){
            if (bank.getBankData().getUserByPhone(phoneNumber)!=null){
                return bank.getBankData().getUserByPhone(phoneNumber);
            }
        }
        return null;
    }

/*
    public void wireTransfer(NeoBank neoBank, List<SimpleUser> users, String value, CentralBank centralBank){
        SimpleUser receiver = users.get(1);
        SimpleUser sender = users.get(0);
        if (receiver==null){
            return;
        }
        if (value==null){
            return;
        }
        if (!input.nextConfirmation(receiver, value)){
            System.out.println(ColorConsole.RED + "Transfer failed" + ColorConsole.RESET);
            return;
        }
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        if (Double.parseDouble(value) + neoBank.getManagerData().getCardWage() + remains > sender.getAccount().getBalance()) {
            System.out.println(ColorConsole.RED + "transfer failed! you don't have enough money!" + ColorConsole.RESET);
            return;
        }
        WireTransaction newTransaction = new WireTransaction(Double.parseDouble(value), neoBank.getTracingNumber(), receiver, receiver.getAccount().getAccountId(),"-", sender, false);
        neoBank.getManagerData().addTransaction(newTransaction);
        System.out.println(ColorConsole.GREEN + "Transaction is on hold" + ColorConsole.RESET);
        TransactionThread thread = new TransactionThread(centralBank, neoBank, newTransaction);
        Thread newThread = new Thread(thread);
        newThread.start();
    }

    public void cardToCard(NeoBank neoBank, SimpleUser sender, SimpleUser receiver, String value){
        if (receiver==null){
            return;
        }
        if (value==null){
            return;
        }
        if (!input.nextConfirmation(receiver, value)){
            System.out.println(ColorConsole.RED + "Transfer failed" + ColorConsole.RESET);
            return;
        }
        if (receiver.getAccount().getCreditCard().hasSetPassword()) {
            if (!receiver.getAccount().getCreditCard().enterPreviousPassCode()) {
                return;
            }
        }
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        if (Double.parseDouble(value) + neoBank.getManagerData().getCardWage() + remains > sender.getAccount().getBalance()) {
            System.out.println(ColorConsole.RED + "transfer failed! you don't have enough money!" + ColorConsole.RESET);
            return;
        }
        System.out.println(neoBank.getManagerData().getCardWage());
        Double removeValue = Double.parseDouble(value) + neoBank.getManagerData().getCardWage();
        sender.getAccount().setBalance(sender.getAccount().getBalance() - removeValue);
        TransferTransaction newTransaction = new TransferTransaction(removeValue, neoBank.getTracingNumber(), receiver, false, receiver.getAccount().getCreditCard().getCreditCardId(), "-", sender, false);
        sender.getAccount().addTransaction(newTransaction, "Transfer");
        sender.getAccount().addRecentCentral(newTransaction, receiver.getSimCard().getPhoneNumber(), this);
        this.transferBetweenBanks(sender, receiver.getAccount(), value, neoBank);
        System.out.println(ColorConsole.GREEN_BOLD + "Transfer Completed!" + ColorConsole.RESET);
    }

    public void bridgeTransfer(NeoBank neoBank, SimpleUser sender, SimpleUser receiver, String value){
        if (receiver==null){
            return;
        }
        if (value==null){
            return;
        }
        if (!input.nextConfirmation(receiver, value)){
            System.out.println(ColorConsole.RED + "Transfer failed" + ColorConsole.RESET);
            return;
        }
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        double removeValue = (Double.parseDouble(value)*(neoBank.getManagerData().getBridgePercentage()+100))/100 + remains;
        if (removeValue > sender.getAccount().getBalance()) {
            System.out.println(ColorConsole.RED + "transfer failed! you don't have enough money!" + ColorConsole.RESET);
            return;
        }
        sender.getAccount().setBalance(sender.getAccount().getBalance() - removeValue);
        TransferTransaction newTransaction = new TransferTransaction(removeValue, neoBank.getTracingNumber(), receiver, false, receiver.getAccount().getAccountId(), "-", sender, false);
        sender.getAccount().addTransaction(newTransaction, "Transfer");
        sender.getAccount().addRecentCentral(newTransaction, receiver.getSimCard().getPhoneNumber(), this);
        this.transferBetweenBanks(sender, receiver.getAccount(), value, neoBank);
        System.out.println(ColorConsole.GREEN_BOLD + "Transfer Completed!" + ColorConsole.RESET);

    }

    public void transferBetweenBanks(SimpleUser sender, Account receiver, String value, NeoBank neoBank){
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        receiver.setBalance(receiver.getBalance() + Double.parseDouble(value));
        receiver.addTransaction(new TransferTransaction(Double.parseDouble(value), neoBank.getTracingNumber() + 1, receiver.getOwner(), false, receiver.getCreditCard().getCreditCardId(), "+", sender, true), "transfer");
        neoBank.setTracingNumber(neoBank.getTracingNumber() + 2);
        if (sender.isHasRemainsFund()) {
            sender.getRemainsFund().saveRemains(remains, neoBank);
        }
    }

    public void transferByCard(NeoBank neoBank, SimpleUser currentUser, CentralBank centralBank){
        String creditCardID = input.nextCreditCardID(this);
        if (creditCardID==null){
            return;
        }
        SimpleUser receiver = this.existsCreditCardId(creditCardID).getOwner();
        String value = input.nextValue(receiver,8000000.0 );
        if (value==null){
            return;
        }
        String creditCardStarter = creditCardID.substring(0, 8);
        NeoBank bankReceiver = this.findBankByCreditCard(creditCardStarter);
        if (neoBank.equals(bankReceiver)){
            this.showOnlyFariCard(neoBank, value, currentUser, receiver);
            return;
        }
        this.showTransferOptionsForCard(value, new ArrayList<>(Arrays.asList(currentUser, receiver)), neoBank, centralBank);
    }

    public void transferByAccount(NeoBank neoBank, SimpleUser currentUser, CentralBank centralBank){
        String accountID = input.nextAccountID(this);
        if (accountID==null){
            return;
        }
        SimpleUser receiver = this.existsAccountId(accountID).getOwner();
        String value = input.nextValue(receiver,8000000.0 );
        if (value==null){
            return;
        }
        String creditCardStarter = receiver.getAccount().getCreditCard().getCreditCardId().substring(0, 8);
        NeoBank bankReceiver = this.findBankByCreditCard(creditCardStarter);
        if (neoBank.equals(bankReceiver)){
            this.showOnlyFariAccount(neoBank, value, currentUser, receiver);
            return;
        }
        this.showTransferOptionsForAccount(value, new ArrayList<>(Arrays.asList(currentUser, receiver)), neoBank, centralBank);
    }

    public void showOnlyFariCard(NeoBank neoBank, String value, SimpleUser sender, SimpleUser receiver){
        String answer = this.displayTransferOptions();
        if (!input.exitPoint(answer)){
            return;
        } else if ("4".equals(answer) || "Fari Transfer".equals(answer)){
            sender.transferByCreditID(neoBank, value, receiver.getAccount().getCreditCard().getCreditCardId());
            return;
        }  else if (!answer.matches("[0-9]+")){
            System.out.println(ColorConsole.RED + "Wrong format" + ColorConsole.RESET);
        } else if (Integer.parseInt(answer)>4){
            System.out.println(ColorConsole.RED + "No other Option!" + ColorConsole.RESET);
        }else{
            System.out.println(ColorConsole.RED + "You can't choose these!" + ColorConsole.RESET);
        }
        this.showOnlyFariCard(neoBank, value, sender, receiver);
    }

    public void showOnlyFariAccount(NeoBank neoBank, String value, SimpleUser sender, SimpleUser receiver){
        String answer = this.displayTransferOptions();
        if (!input.exitPoint(answer)){
            return;
        } else if ("4".equals(answer) || "Fari Transfer".equals(answer)){
            sender.transferByAccountID(neoBank, value, receiver.getAccount().getAccountId());
            return;
        }  else if (!answer.matches("[0-9]+")){
            System.out.println(ColorConsole.RED + "Wrong format" + ColorConsole.RESET);
        } else if (Integer.parseInt(answer)>4){
            System.out.println(ColorConsole.RED + "No other Option!" + ColorConsole.RESET);
        }else{
            System.out.println(ColorConsole.RED + "You can't choose these!" + ColorConsole.RESET);
        }
        this.showOnlyFariCard(neoBank, value, sender, receiver);
    }

    public String displayTransferOptions(){
        System.out.println(ColorConsole.BLUE + "Choose way : " + ColorConsole.RESET);
        System.out.println(ColorConsole.YELLOW + "1. Wire Transfer" + ColorConsole.RESET);
        System.out.println(ColorConsole.YELLOW + "2. Bridge Transfer" + ColorConsole.RESET);
        System.out.println(ColorConsole.YELLOW + "3. Card to Card Transfer" + ColorConsole.RESET);
        System.out.println(ColorConsole.YELLOW + "4. Fari Transfer" + ColorConsole.RESET);
        return input.nextLine();
    }

    public void showTransferOptionsForCard(String value, List<SimpleUser> senderReceiver, NeoBank neoBank, CentralBank centralBank){
        String answer = this.displayTransferOptions();
        switch (answer){
            case "1", "Wire Transfer"-> {
                if (checkWire(value)) {
                    this.wireTransfer(neoBank, senderReceiver, value, centralBank);
                    return;
                }
            }
            case "2", "Bridge Transfer"-> {
                if (checkBridge(value)) {
                    this.bridgeTransfer(neoBank, senderReceiver.get(0), senderReceiver.get(1), value);
                    return;
                }
            }
            case "3", "Card to Card Transfer" -> {
                if (checkCard(value, true)) {
                    this.cardToCard(neoBank, senderReceiver.get(0), senderReceiver.get(1), value);
                    return;
                }
            }
            case "4", "Fari Transfer" -> {
                System.out.println(ColorConsole.RED + "You can't choose this option" + ColorConsole.RESET);
            }
            default-> {
                if (!input.exitPoint(answer)) {
                    return;
                }
                System.out.println(ColorConsole.RED + "No other option" + ColorConsole.RESET);
            }
        }
        this.showTransferOptionsForCard(value, senderReceiver, neoBank, centralBank);
    }

    public boolean checkCard(String value, boolean byCard){
        if (!byCard || Double.parseDouble(value)>100000.0){
            System.out.println(ColorConsole.RED + "You can't choose this");
            return false;
        }
        return true;
    }

    public boolean checkBridge(String value){
        if (Double.parseDouble(value)>5000000.0){
            System.out.println(ColorConsole.RED + "Higher than the maximum limit" + ColorConsole.RESET);
            return false;
        }
        return true;
    }

    public boolean checkWire(String value){
        if (Double.parseDouble(value)>5000000.0){
            System.out.println(ColorConsole.RED + "Higher than the maximum limit" + ColorConsole.RESET);
            return false;
        }
        return true;
    }

    public void showTransferOptionsForAccount(String value, List<SimpleUser> senderReceiver, NeoBank neoBank, CentralBank centralBank){
        String answer = this.displayTransferOptions();
        switch (answer){
            case "1", "Wire Transfer"-> {
                if (checkWire(value)) {
                    this.wireTransfer(neoBank, senderReceiver, value, centralBank);
                    return;
                }
            }
            case "2", "Bridge Transfer"-> {
                if (checkBridge(value)) {
                    this.bridgeTransfer(neoBank, senderReceiver.get(0), senderReceiver.get(1), value);
                    return;
                }
                break;
            }
            case "3", "Card to Card Transfer" -> {
                if (checkCard(value, false)) {
                    this.bridgeTransfer(neoBank, senderReceiver.get(0), senderReceiver.get(1), value);
                    return;
                }
            }
            case "4", "Fari Transfer" -> {
                System.out.println(ColorConsole.RED + "You can't choose this option" + ColorConsole.RESET);
            }
            default-> {
                if (!input.exitPoint(answer)) {
                    return;
                }
                System.out.println(ColorConsole.RED + "No other option" + ColorConsole.RESET);
            }
        }
        this.showTransferOptionsForCard(value, senderReceiver, neoBank, centralBank);
    }

 */
}

package ir.ac.kntu;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

public class CentralBank {
    private List<NeoBank> banks;


    public CentralBank() {
        this.banks = new ArrayList<>();
    }


    public void addBank(NeoBank neoBank) {
        this.banks.add(neoBank);
    }

    public NeoBank findBankByCreditCard(String creditCardStarter) {
        for (NeoBank bank : banks) {
            if (bank.getCreditCardStarter().equals(creditCardStarter)) {
                return bank;
            }
        }
        return null;
    }

    public boolean sameBank(SimpleUser first, SimpleUser second) {
        NeoBank firstBank = null;
        for (NeoBank bank : banks) {
            if (bank.getBankData().getUserByPhone(first.getSimCard().getPhoneNumber()) != null) {
                firstBank = bank;
            }
        }
        NeoBank secondBank = null;
        for (NeoBank bank : banks) {
            if (bank.getBankData().getUserByPhone(second.getSimCard().getPhoneNumber()) != null) {
                secondBank = bank;
            }
        }
        return secondBank.equals(firstBank);
    }

    public Account existsAccountId(String accountId) {
        for (NeoBank bank : banks) {
            if (bank.getBankData().existsAccountID(accountId)) {
                return bank.getBankData().getUserByAccountID(accountId).getAccount();

            }
        }
        return null;
    }

    public Account existsCreditCardId(String creditID ) {
        for (NeoBank bank : banks) {
            if (bank.getBankData().existsCreditCard(creditID)) {
                return bank.getBankData().getAccountByCard(creditID);
            }
        }
        return null;
    }

    public void getUserByCard(String creditID, Context context) {
        for (NeoBank bank : banks) {
            if (bank.getBankData().existsCreditCard(creditID)) {
                SimpleUser user = bank.getBankData().getUserByCreditID(creditID);
                Toast.makeText(context, user.getName(), Toast.LENGTH_SHORT).show();
            }
        }
        return;
    }

    public SimpleUser getUserBySim(String phoneNumber) {
        for (NeoBank bank : banks) {
            if (bank.getBankData().getUserByPhone(phoneNumber) != null) {
                return bank.getBankData().getUserByPhone(phoneNumber);
            }
        }
        return null;
    }


    public void wireTransfer(SimpleUser sender, SimpleUser receiver, String value, Context context){

        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        if (Double.parseDouble(value) + MainActivity.getFariBank().getManagerData().getCardWage() + remains > sender.getAccount().getBalance()) {
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        WireTransaction newTransaction = new WireTransaction(Double.parseDouble(value), MainActivity.getFariBank().getTracingNumber(), receiver, receiver.getAccount().getAccountId(),"-", sender, false);
        MainActivity.getFariBank().getManagerData().addTransaction(newTransaction);
        Toast.makeText(context, "Transaction is on hold", Toast.LENGTH_SHORT).show();
        TransactionThread thread = new TransactionThread(this, MainActivity.getFariBank(), newTransaction, context);
        Thread newThread = new Thread(thread);
        newThread.start();
    }

    public void cardToCard(SimpleUser sender, SimpleUser receiver, String value, Context context){
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        if (Double.parseDouble(value) + MainActivity.getFariBank().getManagerData().getCardWage() + remains > sender.getAccount().getBalance()) {
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        double removeValue = Double.parseDouble(value) + MainActivity.getFariBank().getManagerData().getCardWage();
        sender.getAccount().setBalance(sender.getAccount().getBalance() - removeValue);
        TransferTransaction newTransaction = new TransferTransaction(removeValue, MainActivity.getFariBank().getTracingNumber(), receiver, false, receiver.getAccount().getCreditCard().getCreditCardId(), "-", sender, false);
        sender.getAccount().addTransaction(newTransaction);
        sender.getAccount().addRecentCentral(newTransaction, receiver.getSimCard().getPhoneNumber(), this);
        this.transferBetweenBanks(sender, receiver.getAccount(), value, MainActivity.getFariBank());
        Toast.makeText(context, "Transfer completed!", Toast.LENGTH_SHORT).show();
    }

    public void bridgeTransfer( SimpleUser sender, SimpleUser receiver, String value, Context context){
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        double removeValue = (Double.parseDouble(value)*(MainActivity.getFariBank().getManagerData().getBridgePercentage()+100))/100 + remains;
        if (removeValue > sender.getAccount().getBalance()) {
            Toast.makeText(context, "transfer failed! you don't have enough money!", Toast.LENGTH_SHORT).show();
            return;
        }
        sender.getAccount().setBalance(sender.getAccount().getBalance() - removeValue);
        TransferTransaction newTransaction = new TransferTransaction(removeValue, MainActivity.getFariBank().getTracingNumber(), receiver, false, receiver.getAccount().getAccountId(), "-", sender, false);
        sender.getAccount().addTransaction(newTransaction);
        sender.getAccount().addRecentCentral(newTransaction, receiver.getSimCard().getPhoneNumber(), this);
        this.transferBetweenBanks(sender, receiver.getAccount(), value, MainActivity.getFariBank());
        Toast.makeText(context, "Transfer completed!", Toast.LENGTH_SHORT).show();
    }

    public void transferBetweenBanks(SimpleUser sender, Account receiver, String value, NeoBank neoBank){
        double remains = sender.isHasRemainsFund() ? sender.getRemainsFund().calculateRemains(value) : 0;
        receiver.setBalance(receiver.getBalance() + Double.parseDouble(value));
        receiver.addTransaction(new TransferTransaction(Double.parseDouble(value), neoBank.getTracingNumber() + 1, receiver.getOwner(), false, receiver.getAccountId(), "+", sender, true));
        neoBank.setTracingNumber(neoBank.getTracingNumber() + 2);
        if (sender.isHasRemainsFund()) {
            sender.getRemainsFund().saveRemains(remains, neoBank);
        }
    }
/*
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

   */

    public void completeTransferInfoCredit(SimpleUser currentUser, SimpleUser receiver,String[] info, Context context){
        String value = info[0];
        String way = info[1];
        Account receiverAccount = receiver.getAccount();
        if (receiverAccount ==null){
            Toast.makeText(context, "There is no user with this credit card ID", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure?\n Receiver Name : " + receiver.getName() + " " + receiver.getLastName() + "\n Amount : " + value);
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (way){
                    case "Card to Card"->cardToCard(currentUser, receiver, value, context);
                    case "Bridge Transfer" -> bridgeTransfer(currentUser, receiver, value, context);
                    case "Wire Transfer" -> wireTransfer(currentUser, receiver, value, context);
                    case "Fari Transfer" -> currentUser.transferByCreditID(receiver, value, context);
                    default -> {
                        return;
                    }
                }
                Intent intent = new Intent(context, DashBoard.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                startActivity(context, intent, Bundle.EMPTY);
            }
        });
        builder.setNegativeButton("Noo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Transfer");
        warning.show();

    }

    public void completeTransferInfoAccount(SimpleUser currentUser, SimpleUser receiver,String[] info, Context context){
        String value = info[0];
        String way = info[1];
        Account receiverAccount = receiver.getAccount();
        if (receiverAccount ==null){
            Toast.makeText(context, "There is no user with this credit card ID", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure?\n Receiver Name : " + receiver.getName() + " " + receiver.getLastName() + "\n Amount : " + value);
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (way){
                    case "Card to Card"->cardToCard(currentUser, receiver, value, context);
                    case "Bridge Transfer" -> bridgeTransfer(currentUser, receiver, value, context);
                    case "Wire Transfer" -> wireTransfer(currentUser, receiver, value, context);
                    case "Fari Transfer" -> currentUser.transferByAccountID(receiver, value, context);
                    default -> {
                        return;
                    }
                }
                Intent intent = new Intent(context, DashBoard.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                startActivity(context, intent, Bundle.EMPTY);
            }
        });
        builder.setNegativeButton("Noo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Transfer");
        warning.show();

    }

    public void completeTransferInfoContact(SimpleUser currentUser, SimpleUser receiver,String[] info, Context context){
        String value = info[0];
        String way = info[1];
        Account receiverAccount = receiver.getAccount();
        if (receiverAccount ==null){
            Toast.makeText(context, "There is no user with this credit card ID", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation!");
        builder.setMessage("Are you sure?\n Receiver Name : " + receiver.getName() + " " + receiver.getLastName() + "\n Amount : " + value);
        builder.setPositiveButton("Yes!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                currentUser.transferByContact(receiver, value, context);
                Intent intent = new Intent(context, DashBoard.class);
                intent.putExtra("Phone Number", currentUser.getSimCard().getPhoneNumber());
                startActivity(context, intent, Bundle.EMPTY);
            }
        });
        builder.setNegativeButton("Noo!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog warning = builder.create();
        warning.setTitle("Transfer");
        warning.show();

    }


    public boolean checkWays(String id, String value, String way, boolean byCredit){
        String creditCardStarter = id.substring(0, 8);
        NeoBank bankReceiver = this.findBankByCreditCard(creditCardStarter);
        if ("Fari Transfer".equals(way)) {
            if (MainActivity.getFariBank().equals(bankReceiver)) {
                return true;
            }
            return false;
        }else if ("Bridge Transfer".equals(way) && checkBridge(value)){
            if (MainActivity.getFariBank().equals(bankReceiver)) {
                return false;
            }
            return true;
        }else if ("Card to Card".equals(way) && checkCard(value, byCredit)){
            if (MainActivity.getFariBank().equals(bankReceiver)) {
                return false;
            }
            return true;
        }else if ("Wire Transfer".equals(way) && checkWire(value)){
            if (MainActivity.getFariBank().equals(bankReceiver)) {
                return false;
            }
            return true;
        }
        return false;
    }
    /*

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
*/
    public boolean checkCard(String value, boolean byCard){
        if (!byCard || Double.parseDouble(value)>100000.0){
            return false;
        }
        return true;
    }


    public boolean checkBridge(String value){
        return !(Double.parseDouble(value) > 5000000.0);
    }

    public boolean checkWire(String value){
        return !(Double.parseDouble(value) > 5000000.0);
    }
/*
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

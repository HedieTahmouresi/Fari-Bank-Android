package ir.ac.kntu;

import android.content.Context;

import java.time.Duration;
import java.time.Instant;

public class TransactionThread implements Runnable{
    private CentralBank centralBank;
    private NeoBank neoBank;
    private WireTransaction transaction;
    private Context context;

    public TransactionThread(CentralBank centralBank, NeoBank neoBank, WireTransaction transaction, Context context){
        setCentralBank(centralBank);
        setNeoBank(neoBank);
        setTransaction(transaction);
        this.context = context;
    }

    public CentralBank getCentralBank() {
        return centralBank;
    }

    public void setCentralBank(CentralBank centralBank) {
        this.centralBank = centralBank;
    }

    public NeoBank getNeoBank() {
        return neoBank;
    }

    public void setNeoBank(NeoBank neoBank) {
        this.neoBank = neoBank;
    }

    public WireTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(WireTransaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public void run() {
        Duration oneDay = Duration.ofDays(3);
        Instant endTime = this.getTransaction().getDateAndTime().plus(oneDay);
        Duration duration = Duration.between(this.getTransaction().getDateAndTime(), endTime);
        long time = duration.toMillis()/6000;
        try{
            Thread.sleep(time);
            this.getTransaction().completeTransaction(this.getCentralBank(), this.getNeoBank(), this.context);
        }catch (InterruptedException error){
            System.out.println(ColorConsole.RED + "Thread Error" + ColorConsole.RESET);
        }
    }
}

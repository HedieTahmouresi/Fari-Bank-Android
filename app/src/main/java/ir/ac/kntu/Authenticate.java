package ir.ac.kntu;

import ir.ac.kntu.util.Calendar;

public class Authenticate implements Runnable {
    private NeoBank neoBank;
    private SimpleUser user;

    public NeoBank getNeoBank() {
        return neoBank;
    }

    public void setNeoBank(NeoBank neoBank) {
        this.neoBank = neoBank;
    }

    public SimpleUser getUser() {
        return user;
    }

    public void setUser(SimpleUser user) {
        this.user = user;
    }

    public Authenticate(NeoBank neoBank, SimpleUser user) {
        this.neoBank = neoBank;
        this.user = user;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(60000);
            this.getUser().getAuthenticated().authenticateUser(this.getNeoBank(), this.getUser());
        } catch (InterruptedException error) {
            System.out.println("Thread Error");
        }
    }
}


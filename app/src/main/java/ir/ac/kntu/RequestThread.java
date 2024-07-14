package ir.ac.kntu;

public class RequestThread implements Runnable {
    private SimpleUser user;
    private NeoBank neoBank;
    private Request request;

    public SimpleUser getUser() {
        return user;
    }

    public void setUser(SimpleUser user) {
        this.user = user;
    }

    public NeoBank getNeoBank() {
        return neoBank;
    }

    public void setNeoBank(NeoBank neoBank) {
        this.neoBank = neoBank;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public RequestThread(SimpleUser user, NeoBank neoBank, Request request) {
        this.user = user;
        this.neoBank = neoBank;
        this.request = request;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(120000);
            this.getRequest().closeRequest();
        } catch (InterruptedException error) {
            System.out.println("Thread Error");
        }
    }
}

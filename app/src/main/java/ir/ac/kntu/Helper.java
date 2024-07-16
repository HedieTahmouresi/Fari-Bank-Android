package ir.ac.kntu;

public class Helper {
    public void initiateFariUsers(NeoBank neoBank) {
        SimpleUser hedie = new SimpleUser("Hedie", "Tahmouresi", new SimCard("09109056296", false), "0025755900", "H@tah1384", new Authentication("09109056296"));
        hedie.getAuthenticated().authenticateUser(neoBank, hedie);
        neoBank.getBankData().addUser(hedie);
        SimpleUser aylin = new SimpleUser("Aylin", "Jabbari", new SimCard("09901917812", false), "0215021470", "A#Jab1384", new Authentication("09901917812"));
        aylin.getAuthenticated().authenticateUser(neoBank, aylin);
        neoBank.getBankData().addUser(aylin);
        aylin.setContactOption(false);
        SimpleUser amir = new SimpleUser("Amir", "Tahmouresi", new SimCard("09028789000", false), "0105213054", "A@tah1379", new Authentication("09028789000"));
        neoBank.getBankData().addUser(amir);
        amir.getAuthenticated().authenticateUser(neoBank, amir);
        this.initiateSimCards(neoBank, hedie, aylin, amir);
        this.initiateContacts(hedie, amir);
    }

    public void initiateContacts(SimpleUser firstUser, SimpleUser secondUser) {
        firstUser.addContact(new Contact("amir", "topolo", new SimCard("09028789000", true)));
        firstUser.addContact(new Contact("Malake ziba", "Elsaii", new SimCard("09901917812", true)));
        secondUser.addContact(new Contact("humourless", " ", new SimCard("09109056296", true)));
    }


    public void initiateSimCards(NeoBank neoBank, SimpleUser hedie, SimpleUser aylin, SimpleUser amir) {
        neoBank.getManagerData().addSimCard(hedie.getSimCard());
        neoBank.getManagerData().addSimCard(amir.getSimCard());
        neoBank.getManagerData().addSimCard(aylin.getSimCard());
    }


    public void initiateFari(NeoBank neoBank) {
        this.initiateFariUsers(neoBank);
    }

    public void initiateHediUsers(NeoBank neoBank) {
        SimpleUser mohsen = new SimpleUser("Mohsen", "Tahmouresi", new SimCard("09122153905", true), "0102020202", "M@tah1345", new Authentication("09124464876"));
        neoBank.getBankData().addUser(mohsen);
        mohsen.getAuthenticated().authenticateUser(neoBank, mohsen);
        System.out.println(mohsen.getAccount().getCreditCard().getCreditCardId());
    }

    public String initiateToriUsers(NeoBank neoBank) {
        SimpleUser ghazale = new SimpleUser("Ghazale", "Roostaei", new SimCard("09122562348", false), "0215021470", "A#Jab1384", new Authentication("09122562348"));
        ghazale.getAuthenticated().authenticateUser(neoBank, ghazale);
        neoBank.getBankData().addUser(ghazale);
        return ghazale.getAccount().getAccountId();
    }

    public String initiateCentralBank(CentralBank centralBank, NeoBank neoBank) {
        NeoBank toriBank = new NeoBank("63621411");
        String id = this.initiateToriUsers(toriBank);
        NeoBank hediBank = new NeoBank("50221314");
        this.initiateHediUsers(hediBank);
        centralBank.addBank(neoBank);
        centralBank.addBank(toriBank);
        centralBank.addBank(hediBank);
        return id;
    }
}

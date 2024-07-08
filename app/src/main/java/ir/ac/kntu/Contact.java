package ir.ac.kntu;

import android.os.Parcel;

import androidx.annotation.NonNull;

public class Contact extends UserPerson {


    public Contact(String name, String lastName, SimCard simCard) {
        super(name, lastName, simCard);
    }

/*
    public void showInfo() {
        System.out.println(ColorConsole.PURPLE + this.toString() + ColorConsole.RESET);
        System.out.println(ColorConsole.CYAN + "Phone Number : " + this.getSimCard().getPhoneNumber() + ColorConsole.RESET);
    }

    public void addContact(NeoBank neoBank, SimpleUser currentUser) {
        if (currentUser.contactExistence(this)) {
            System.out.println(ColorConsole.RED_BOLD + "You already have a contact with this phone number!" + ColorConsole.RESET);
            return;
        }
        if (!neoBank.getBankData().getUserByPhone(this.getSimCard().getPhoneNumber()).getAuthenticated().isAuthenticated()) {
            System.out.println(ColorConsole.RED_BOLD + "This user hasn't completed setup!" + ColorConsole.RESET);
            return;
        }
        currentUser.addContact(this);
        System.out.println(ColorConsole.GREEN + "Contact successfully added!" + ColorConsole.RESET);
    }

    public void changeContactInfo(NeoBank neoBank, SimpleUser user) {
        System.out.println(ColorConsole.BLUE + "Would you like to change your contact info?");
        System.out.println("   1.yes");
        System.out.println("   2.no" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "yes":
                this.changeName();
                this.changeLastName();
                this.changePhoneNumber(neoBank, user, "contact");
                return;
            case "no":
                return;
            default:
                if (input.exitPoint(answer)) {
                    System.out.println(ColorConsole.RED + "THERE IS NO OTHER OPTION! Please input something else!" + ColorConsole.RESET);
                } else {
                    return;
                }

        }
        this.changeContactInfo(neoBank, user);
    }

    public void deleteContact(SimpleUser user) {
        System.out.println(ColorConsole.BLUE + "Would you like to delete your contact?");
        System.out.println("   1.yes");
        System.out.println("   2.no" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "1", "yes":
                user.removeContact(this);
                break;
            case "2", "no":
                break;
            default:
                if (!input.exitPoint(answer)) {
                    return;
                } else {
                    System.out.println(ColorConsole.RED + "Wrong input!" + ColorConsole.RESET);
                    this.deleteContact(user);
                }
                break;
        }
    }

    public void contactListOptions(NeoBank neoBank, SimpleUser currentUser) {
        this.showInfo();
        this.changeContactInfo(neoBank, currentUser);
        this.deleteContact(currentUser);
    }

 */
}

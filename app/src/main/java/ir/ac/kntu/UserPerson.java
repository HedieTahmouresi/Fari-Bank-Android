package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class UserPerson {
    private String name;
    private String lastName;
    private SimCard simCard;



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SimCard getSimCard() {
        return simCard;
    }

    public void setSimCard(SimCard simCard) {
        this.simCard = simCard;
    }

    public UserPerson(String name, String lastName, SimCard simCard) {
        setName(name);
        setLastName(lastName);
        simCard.setHasAccount(true);
        setSimCard(simCard);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UserPerson that)) {
            return false;
        }
        return Objects.equals(getSimCard(), that.getSimCard());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getSimCard());
    }


    /*public void changeName() {
        System.out.println(ColorConsole.CYAN + "Would you like to change the name? (previous name : " + ColorConsole.PURPLE + this.getName() + ColorConsole.CYAN + ")" + ColorConsole.RESET);
        String answer = input.nextLine();
        if ("no".equalsIgnoreCase(answer) || !input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            System.out.println(ColorConsole.CYAN + "Write the name you like!" + ColorConsole.RESET);
            String name = input.nextLine();
            if (input.exitPoint(name)) {
                this.setName(name);
            }
            return;
        } else {
            System.out.println(ColorConsole.RED + "Wrong input! Try again" + ColorConsole.RESET);
        }
        this.changeName();
    }

    public void changeLastName() {
        System.out.println(ColorConsole.CYAN + "Would you like to change the last name? (previous last name : " + ColorConsole.PURPLE + this.getLastName() + ColorConsole.CYAN + ")" + ColorConsole.RESET);
        String answer = input.nextLine();
        if ("no".equalsIgnoreCase(answer) || !input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            System.out.println(ColorConsole.CYAN + "Write the last name you like!" + ColorConsole.RESET);
            String lastName = input.nextLine();
            if (input.exitPoint(lastName)) {
                this.setLastName(lastName);
            }
            return;
        } else {
            System.out.println(ColorConsole.RED + "Wrong input! Try again" + ColorConsole.RESET);
        }
        this.changeLastName();
    }

    public void changePhoneNumber(NeoBank neoBank, SimpleUser user, String usage) {
        System.out.println(ColorConsole.CYAN_BOLD + "Would you like to change the phone number? (previous phone number : " + ColorConsole.PURPLE + this.getSimCard().getPhoneNumber() + ColorConsole.CYAN_BOLD + ")" + ColorConsole.RESET);
        String answer = input.nextLine();
        if ("no".equalsIgnoreCase(answer) || !input.exitPoint(answer)) {
            return;
        } else if ("yes".equalsIgnoreCase(answer)) {
            String need = "contact".equals(usage) ? "should exist" : "shouldn't exist";
            String phoneNumber = input.nextPhoneNumber(neoBank.getBankData(), need);
            if (phoneNumber != null) {
                if ("contact".equals(usage) && user.contactExistence(new Contact(" ", " ", new SimCard(phoneNumber, false)))) {
                    System.out.println(ColorConsole.RED_BOLD + "You already have a contact with this phone number" + ColorConsole.RESET);
                    this.changePhoneNumber(neoBank, user, usage);
                }
                SimCard sim = neoBank.getManagerData().getSimCard(phoneNumber);
                if (sim == null) {
                    sim = new SimCard(phoneNumber, true);
                    neoBank.getManagerData().addSimCard(sim);
                }
                this.setSimCard(sim);
            }
            return;
        } else {
            System.out.println(ColorConsole.RED + "Wrong input! Try again" + ColorConsole.RESET);
        }
        this.changePhoneNumber(neoBank, user, usage);
    }

    @Override
    public String toString() {
        return ColorConsole.PURPLE_BOLD + "Name : " + ColorConsole.PINK + this.getName() +
                ColorConsole.PURPLE_BOLD + ", Last Name : " + ColorConsole.PINK + this.getLastName() +
                ColorConsole.PURPLE_BOLD + ", Phone Number : " + ColorConsole.PINK + this.getSimCard().getPhoneNumber() +
                ColorConsole.RESET;

    }

     */
}

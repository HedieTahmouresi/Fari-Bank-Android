package ir.ac.kntu;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Request {
    private String request;
    private String answer;
    private String phoneNumber;
    private RequestStatus status;
    private RequestSection section;

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestSection getSection() {
        return section;
    }

    public void setSection(RequestSection section) {
        this.section = section;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String securityNumber) {
        this.phoneNumber = securityNumber;
    }

    public Request(String request, RequestSection section, String phoneNumber) {
        setRequest(request);
        setSection(section);
        setAnswer("No answer");
        setStatus(RequestStatus.NOTED);
        setPhoneNumber(phoneNumber);
    }


    /*
    @Override
    public String toString() {
        return ColorConsole.PURPLE + "Request{" +
                "phoneNumber='" + this.getPhoneNumber() + '\'' +
                " Section=" + this.getSection() +
                ", Status=" + this.getStatus() +
                '}' + ColorConsole.RESET;
    }

    public void showInfo(Data data) {
        SimpleUser currentUser = data.getUserByPhone(this.getPhoneNumber());
        System.out.println(ColorConsole.PURPLE + "***" + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Phone Number : " + ColorConsole.CYAN + currentUser.getSimCard().getPhoneNumber() + ColorConsole.RESET);
        System.out.println(ColorConsole.PURPLE + "Request Section : " + ColorConsole.CYAN + this.getSection());
        System.out.println(ColorConsole.PURPLE + "Problem : " + ColorConsole.CYAN + this.getRequest());
        System.out.println(ColorConsole.PURPLE + "Request Status : " + ColorConsole.CYAN + this.getStatus());
        System.out.println(ColorConsole.PURPLE + "Admins Answer : " + ColorConsole.CYAN + this.getAnswer());
        System.out.println(ColorConsole.PURPLE + "***" + ColorConsole.RESET);
    }

    public void processRequest(NeoBank neoBank) {
        this.showInfo(neoBank.getBankData());
        System.out.println(ColorConsole.BLUE + "Would you like to process this request?" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "yes":
                this.setStatus(RequestStatus.IN_PROCESS);
                this.closeRequest();
                return;
            case "no":
                return;
            default:
                if (!input.exitPoint(answer)) {
                    return;
                }
                System.out.println(ColorConsole.RED + "THERE IS NO OTHER OPTION! Please input something else!" + ColorConsole.RESET);
                break;

        }
        this.processRequest(neoBank);
    }

    public void closeRequest() {
        System.out.println(ColorConsole.BLUE + "Would you like to answer this request?" + ColorConsole.RESET);
        String answer = input.nextLine();
        switch (answer) {
            case "yes":
                System.out.println(ColorConsole.BLUE + "Please enter your answer!" + ColorConsole.RESET);
                String text = input.nextLine();
                if (!input.exitPoint(text)) {
                    return;
                }
                this.setStatus(RequestStatus.PROCESSED);
                this.setAnswer(answer);
                return;
            case "no":
                return;
            default:
                if (!input.exitPoint(answer)) {
                    return;
                }
                System.out.println(ColorConsole.RED + "THERE IS NO OTHER OPTION! Please input something else!" + ColorConsole.RESET);
        }
        this.closeRequest();
    }

     */
}

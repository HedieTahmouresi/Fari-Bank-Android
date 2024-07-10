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

    @Override
    public String toString() {
        return "   *status : " + this.getStatus().toString();
    }

    public String showInfo(Data data) {
        SimpleUser currentUser = data.getUserByPhone(this.getPhoneNumber());
        return "***" + "\nPhone Number : " + currentUser.getSimCard().getPhoneNumber() + "\nRequest Section : " + this.getSection()  + "\nProblem : " + this.getRequest() +  "\nRequest Status : " +  this.getStatus() + "\nAdmins Answer : " +  this.getAnswer() +"\n***" ;
    }

    public void closeRequest() {
        this.setStatus(RequestStatus.IN_PROCESS);
        this.setAnswer("Our associates will be in touch with you as soon as possible");

    }

}

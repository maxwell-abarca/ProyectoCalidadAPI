package com.project.demo.logic.entity.email;

public class EmailDetails {
    private EmailInfo fromAddress;
    private EmailInfo toAddress;
    private String suject;
    private String emailBody;

    public EmailDetails(EmailInfo fromAddress, EmailInfo toAddress, String subject, String emailBody) {
        setFromAddress(fromAddress);
        setToAddress(toAddress);
        setSuject(subject);
        setEmailBody(emailBody);
    }

    public EmailInfo getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(EmailInfo fromAddress) {
        this.fromAddress = fromAddress;
    }

    public EmailInfo getToAddress() {
        return toAddress;
    }

    public void setToAddress(EmailInfo toAddress) {
        this.toAddress = toAddress;
    }

    public String getSuject() {
        return suject;
    }

    public void setSuject(String suject) {
        this.suject = suject;
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }
}

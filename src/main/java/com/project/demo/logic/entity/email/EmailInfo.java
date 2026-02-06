package com.project.demo.logic.entity.email;


public class EmailInfo {
    String name;
    String emailAddress;

    public EmailInfo(String name, String emailAddress) {
        setName(name);
        setEmailAddress(emailAddress);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}

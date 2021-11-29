package com.raven.model;

import javax.swing.Icon;

public class ModelName {

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Icon getProfile() {
        return profile;
    }

    public void setProfile(Icon profile) {
        this.profile = profile;
    }

    public ModelName(String firstName, String lastName, Icon profile) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile = profile;
    }

    public ModelName() {
    }

    private String firstName;
    private String lastName;
    private Icon profile;

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
}

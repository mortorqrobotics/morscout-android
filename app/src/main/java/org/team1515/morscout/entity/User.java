package org.team1515.morscout.entity;

/**
 * Created by prozwood on 1/26/16.
 */
public class User extends Entity {
    private String firstName;
    private String lastName;
    private String id;
    private String profPicPath;
    private String email;
    private String phone;

    public User(String id, String firstName, String lastName, String profPicPath, String email, String phone) {
        super(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.profPicPath = profPicPath;
        this.email = email;
        this.phone = phone;

    }

    public User(String firstName, String lastName, String id, String profPicPath) {
        this(firstName, lastName, id, profPicPath, "", "");
    }

    public User(String firstName, String lastName, String profPicPath) {
        this(firstName, lastName, "", profPicPath);
    }

    public User(String id) {
        this(id, "", "", "");
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String formatPhoneNumber(String number) {
        return "(" + number.substring(0, 3) + ") " + number.substring(3, 6) + "-" + number.substring(6, number.length());
    }

    public String getPhoneFormatted() {
        return formatPhoneNumber(phone);
    }
}


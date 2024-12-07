package ca.ulaval.glo2003.domain.reservation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {
    private String name;
    private String email;
    private String phoneNumber;

    public Customer(String name, String email, String phoneNumber) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public Customer() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void validate() {
        this.validateName();
        this.validateEmail();
        this.validatePhoneNumber();
    }

    private void validateName() {
        if (this.name == null) throw new IllegalStateException(
            "The name of the customer cannot be null"
        );
    }

    private void validateEmail() {
        if (this.email == null) throw new IllegalStateException(
            "The email of the customer cannot be null"
        );

        String regex = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        if (
            !isMatchingStringToRegex(this.email, regex)
        ) throw new IllegalArgumentException(
            "The email of the customer is not valid"
        );
    }

    private void validatePhoneNumber() {
        if (this.phoneNumber == null) throw new IllegalStateException(
            "The phone number of the customer cannot be null"
        );
        if (this.phoneNumber.length() != 10) {
            throw new IllegalArgumentException("The phone number is not valid");
        }
    }

    private boolean isMatchingStringToRegex(
        String stringToMatch,
        String regex
    ) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(stringToMatch);
        return matcher.matches();
    }
}

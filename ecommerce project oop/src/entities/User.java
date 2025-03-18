package entities;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public abstract class User {
    private String UserName;
    private String Password;
    private String DateOfBirth;
    public User(){}
    public User(String UserName, String Password, String DateOfBirth) {
        setUserName(UserName);
        setPassword(Password);
        setDateOfBirth(DateOfBirth);
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        if (UserName == null || UserName.isEmpty()) {
            throw new IllegalArgumentException("User name is not valid");
        }
        this.UserName = UserName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        if (Password == null || Password.length() < 8) {
            throw new IllegalArgumentException("Password is not valid (must be 8 or more characters)");
        }
        this.Password = Password;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        if (dateOfBirth == null || dateOfBirth.isEmpty()) {
            throw new IllegalArgumentException("Date of birth cannot be null or empty");
        }
        String dateFormat = "^\\d{4}-\\d{2}-\\d{2}$";
        if (!dateOfBirth.matches(dateFormat)) {
            throw new IllegalArgumentException("Date must be in YYYY-MM-DD format");
        }

        try {
            LocalDate parsedDate = LocalDate.parse(dateOfBirth);
            LocalDate now = LocalDate.now();
            if (parsedDate.isAfter(now)) {
                throw new IllegalArgumentException("Date of birth cannot be in the future");
            }

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date values (e.g., month > 12 or invalid day for month)", e);
        }

        this.DateOfBirth = dateOfBirth;
    }
    @Override
    public String toString() {
        return "User{" + "UserName=" + UserName + ", Password=****" + ", DateOfBirth=" + DateOfBirth + '}';
    }

}
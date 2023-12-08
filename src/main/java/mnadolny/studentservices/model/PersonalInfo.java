package mnadolny.studentservices.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.pl.PESEL;

@Embeddable
public class PersonalInfo {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @PESEL
    private String pesel;

    public PersonalInfo() {
    }

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

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }
}

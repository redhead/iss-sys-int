package cz.cvut.iss.sysint.ws;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "address")
@XmlAccessorType(XmlAccessType.FIELD)
public class Address {

    @XmlElement
    private String firstName;

    @XmlElement
    private String lastName;

    @XmlElement
    private String street;

    @XmlElement
    private String city;

    @XmlElement
    private String zipCode;

    public Address() {
    }

    public Address(String firstName, String lastName, String street, String city, String zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
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

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Address)) return false;

        Address address = (Address) o;

        if (!city.equals(address.city)) return false;
        if (!firstName.equals(address.firstName)) return false;
        if (!lastName.equals(address.lastName)) return false;
        if (!street.equals(address.street)) return false;
        if (!zipCode.equals(address.zipCode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = firstName.hashCode();
        result = 31 * result + lastName.hashCode();
        result = 31 * result + street.hashCode();
        result = 31 * result + city.hashCode();
        result = 31 * result + zipCode.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Address [firstName=" + firstName + ", lastName=" + lastName
            + ", street=" + street + ", city=" + city + ", zipCode=" + zipCode + "]";
    }
}

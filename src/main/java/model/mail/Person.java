package model.mail;

/**
 * Content of a Person
 *
 * @author Olivier liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class Person {
    private final String firstName;
    private final String lastName;
    private final String address;

    /**
     * Constructor
     *
     * @param firstName the first name of a person
     * @param lastName  last name of a person
     * @param address   email address of a person
     */
    public Person(String firstName, String lastName, String address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    /**
     * Get first name
     *
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get last name
     *
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get address
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }
}
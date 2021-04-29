package model.prank;

import model.mail.Mail;
import model.mail.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Prank
 *
 * @author Olivier Liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class Prank {
    private Person sender;
    private final List<Person> victims = new ArrayList<>();
    private final List<String> addressesToCc = new ArrayList<>();
    private String message;

    /**
     * Get sender
     *
     * @return sender
     */
    public Person getSender() {
        return sender;
    }

    /**
     * Set sender
     *
     * @param sender
     */
    void setSender(Person sender) {
        this.sender = sender;
    }

    /**
     * Get message
     *
     * @return Message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set message
     *
     * @param message
     */
    void setMessage(String message) {
        this.message = message;
    }

    /**
     * Add victim to a list of victims
     *
     * @param victims
     */
    void addVictims(List<Person> victims) {
        this.victims.addAll(victims);
    }

    /**
     * Add addresses to a list of cc
     *
     * @param addresses
     */
    void addAddressesToCc(List<String> addresses) {
        this.addressesToCc.addAll(addresses);
    }

    /**
     * Get victims
     *
     * @return victims
     */
    public List<Person> getVictims() {
        return new ArrayList<>(victims);
    }

    /**
     * Get addresses to cc
     *
     * @return addresses to cc
     */
    public List<String> getAddressesToCc() {
        return new ArrayList<>(addressesToCc);
    }

    /**
     * Generate Message corresponding to the Prank
     *
     * @return the pranked message
     */
    public Mail generateMessage() {

        // Check sender is not null
        if (sender == null) {
            throw new NullPointerException("sender should not be null to generate a Message");
        }

        Mail message = new Mail();

        // Set from
        message.setFrom(sender.getAddress());

        // Set to
        ArrayList<String> toList = new ArrayList<>();
        for (Person p : victims) {
            toList.add(p.getAddress());
        }
        message.setTo(toList);

        // Set cc
        message.setCc(addressesToCc);

        // Get subject and body
        String subject = "-";
        String body = this.message;
        if (this.message != null) {
            String[] data = this.message.split("\r\n", 2);
            if (data[0].startsWith("Subject: ")) {
                subject = data[0].replace("Subject: ", "");
                body = data[1];
            }
        }

        // Set subject and (body + sender signature)
        message.setSubject(subject);
        message.setBody(body + "\r\n" + sender.getFirstName() + " " + sender.getLastName());

        return message;
    }

}

package model.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Group of a prank
 *
 * @author Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class Group {
    private final Person sender;
    private final List<Person> recipients;

    /**
     * Constructor
     *
     * @param sender     a Person who will send the prank
     * @param recipients List<Person> that will receive the prank
     */
    public Group(Person sender, List<Person> recipients) {
        if (sender == null) {
            throw new NullPointerException("A sender is required !");
        }
        if (recipients == null || recipients.size() < 2) {
            throw new NullPointerException("A group must contain at least 2 recipients");
        }

        this.sender = sender;
        this.recipients = recipients;
    }

    /**
     * Get sender
     *
     * @return a Person corresponding to the group sender
     */
    public Person getSender() {
        return sender;
    }

    /**
     * Get recipients
     *
     * @return Recipients of the group
     */
    public List<Person> getRecipients() {
        return new ArrayList<>(recipients);
    }
}

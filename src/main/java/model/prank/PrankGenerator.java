package model.prank;

import config.ConfigManager;
import model.mail.Group;
import model.mail.Mail;
import model.mail.Person;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generate prank
 *
 * @author Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class PrankGenerator {

    private final ConfigManager configManager;

    /**
     * Constructor
     *
     * @param config ConfigManager of the pranks
     */
    public PrankGenerator(ConfigManager config) {
        if (config == null) {
            throw new NullPointerException("Please provide a ConfigManager !");
        }
        this.configManager = config;
    }

    /**
     * Generate pranked mails
     *
     * @return a list of pranked mails
     */
    public List<Mail> generateMails() {
        // Create groups
        List<Group> groups = generateGroups();

        // Get and shuffle messages
        List<String> messages = configManager.getMessages();
        Collections.shuffle(messages);

        // Generate a pranked Mail for each group
        List<Mail> mails = new ArrayList<>();
        int messageIndex = 0;
        for (Group group : groups) {
            mails.add(generatePrank(group, messages.get(messageIndex)));
            messageIndex = (messageIndex + 1) % messages.size();
        }

        return mails;
    }

    /**
     * Generate pranked mail to a Group containing a message
     *
     * @param group   the group to play the prank
     * @param message the message contained in the prank
     * @return the pranked mail
     */
    private Mail generatePrank(Group group, String message) {
        // Get subject and body
        String subject = "";
        String body = message;
        if (message != null) {
            String[] data = message.split("\r\n", 2);
            if (data[0].startsWith("Subject: ")) {
                subject = data[0].replace("Subject: ", "");
                body = data[1];
            }
        }

        // Get sender then sign body with sender's first and last name
        Person sender = group.getSender();
        body += "\r\n" + sender.getFirstName() + " " + sender.getLastName();

        // Get recipients address
        List<String> to = new ArrayList<>();
        for (Person recipient : group.getRecipients()) {
            to.add(recipient.getAddress());
        }

        // Return a pranked Mail
        return new Mail(sender.getAddress(), to, configManager.getAddressesToCC(),
                subject, "text/plain; charset=\"utf-8\"", body);
    }

    /**
     * Generate random Groups
     *
     * @return a list of random groups
     */
    private List<Group> generateGroups() {
        // Get all victims and nb of groups
        List<Person> victims = configManager.getVictims();
        int nbGroups = configManager.getNbGroups();

        // Throw error if groups can't contain at least 3 persons
        int groupSize = victims.size() / nbGroups;
        if (groupSize < 3) {
            throw new RuntimeException("Each group should contain at least 3 persons, " +
                    "please reduce the number of groups or add more victims");
        }

        // Shuffle victims
        Collections.shuffle(victims);

        // Set indexes
        int indexFirst = 0;
        int indexLast = (victims.size() % groupSize == 0 ? groupSize : groupSize + 1);

        // Create groups filled with the victims
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < nbGroups; ++i) {
            groups.add(new Group(victims.get(indexFirst), victims.subList(indexFirst + 1, indexLast)));
            indexFirst = indexLast;
            indexLast += groupSize;
        }

        return groups;
    }

}

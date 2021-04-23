package model.prank;

import config.ConfigManager;
import model.mail.Group;
import model.mail.Person;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Generate prank
 *
 * @author Olivier Liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class PrankGenerator {

    private final ConfigManager configManager;

    /**
     * Constructor
     *
     * @throws IOException
     */
    public PrankGenerator() throws IOException {
        this.configManager = new ConfigManager();
    }

    /**
     * Generate
     * @return a prank
     */
    public List<Prank> generatePranks() {
        List<Prank> pranks = new ArrayList<>();
        List<String> messages = configManager.getMessages();
        Collections.shuffle(messages);
        int messageIndex = 0;
        int numberOfGroups = configManager.getNumberOfGroups();
        int numberOfVictims = configManager.getVictims().size();

        if (numberOfVictims / numberOfGroups < 3) {
            throw new RuntimeException("Minimum size for a group is 3 people");
        }

        List<Group> groups = generateGroups(configManager.getVictims(), numberOfGroups);
        for (Group group : groups) {
            Prank prank = new Prank();
            List<Person> victims = group.getMembers();
            Collections.shuffle(victims);
            Person sender = victims.remove(0);
            prank.setSender(sender);
            prank.addVictims(victims);
            prank.addAddressesToCc(configManager.getAddressesToCC());

            String message = messages.get(messageIndex);
            messageIndex = (messageIndex + 1) % messages.size();
            prank.setMessage(message);
            pranks.add(prank);
        }
        return pranks;
    }

    private List<Group> generateGroups(List<Person> victims, int numberOfGroups) {
        List<Person> availableVictims = new ArrayList<>(victims);
        Collections.shuffle(availableVictims);
        List<Group> groups = new ArrayList<>();
        for (int i = 0; i < numberOfGroups; i++) {
            Group group = new Group();
            groups.add(group);
        }

        int turn = 0;
        Group targetGroup;
        while (availableVictims.size() > 0) {
            targetGroup = groups.get(turn);
            turn = (turn + 1) % groups.size();
            Person victim = availableVictims.remove(0);
            targetGroup.addMember(victim);
        }
        return groups;
    }

}

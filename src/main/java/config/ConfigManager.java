package config;

import com.google.gson.Gson;
import model.mail.Person;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Configuration manager
 *
 * @author Olivier Liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class ConfigManager {
    private final static String CONFIG_FILENAME = "config.properties";
    private final static String VICTIMS_FILENAME = "victims.json";
    private final static String MESSAGES_FILENAME = "messages.utf8";
    private final static String MESSAGE_SEPARATOR = "==";

    private String smtpServerAddress;
    private int smtpServerPort;
    private int nbGroups;
    private List<Person> victims;
    private List<String> messages;
    private List<String> addressesToCC;

    /**
     * Constructor
     *
     * @throws IOException - if config files failed to open/read
     */
    public ConfigManager(String dirPath) throws IOException {
        loadProperties(dirPath + "/" + CONFIG_FILENAME);
        loadVictims(dirPath + "/" + VICTIMS_FILENAME);
        loadMessages(dirPath + "/" + MESSAGES_FILENAME);
    }

    /**
     * Load properties from config.properties
     *
     * @throws IOException - If file could not be open/read
     */
    private void loadProperties(String filename) throws IOException {
        // Load properties file
        Properties properties = new Properties();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), StandardCharsets.UTF_8))) {
            properties.load(in);
        }

        // Get server address & port, throws exception if null
        smtpServerAddress = properties.getProperty("smtpServerAddress");
        if (smtpServerAddress == null || smtpServerAddress.isEmpty()) {
            throw new RuntimeException("smtpServerAddress should not be empty !");
        }
        smtpServerPort = Integer.parseInt(properties.getProperty("smtpServerPort"));

        // Get number of groups, throws exception if null or <= 0
        nbGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));
        if (nbGroups <= 0) {
            throw new RuntimeException("numberOfgroups should be > 0 !");
        }

        // Get addresses to cc
        String[] witnesses = properties.getProperty("witnessesToCC").split(",");
        addressesToCC = new ArrayList<>(Arrays.asList(witnesses));
    }

    /**
     * Load Persons from a json file
     *
     * @throws IOException - If file could not be open/read
     */
    private void loadVictims(String filename) throws IOException {
        Gson gson = new Gson();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), StandardCharsets.UTF_8))) {
            // Read json file with Gson
            victims = new ArrayList<>(Arrays.asList(gson.fromJson(in, Person[].class)));
        }
    }

    /**
     * Load messages from utf8 file, each message separated by separator
     *
     * @throws IOException - If file could not be open/read
     */
    private void loadMessages(String filename) throws IOException {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename), StandardCharsets.UTF_8))) {
            // Read each message separated by separator
            messages = new ArrayList<>();
            String line;
            while ((line = in.readLine()) != null) {
                StringBuilder body = new StringBuilder();
                while ((line != null) && (!line.equals(MESSAGE_SEPARATOR))) {
                    body.append(line);
                    body.append("\r\n");
                    line = in.readLine();
                }
                messages.add(body.toString());
            }
        }
    }

    /**
     * Get victims
     *
     * @return A list of Person corresponding to the victims
     */
    public List<Person> getVictims() {
        return new ArrayList<>(victims);
    }

    /**
     * Get messages
     *
     * @return A list of Messages corresponding to the message to send
     */
    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    /**
     * Get addresses to cc
     *
     * @return A list of String corresponding to the addresses to cc
     */
    public List<String> getAddressesToCC() {
        return new ArrayList<>(addressesToCC);
    }

    /**
     * Get smtp server address
     *
     * @return A String corresponding to the smtp server ip address
     */
    public String getSmtpServerAddress() {
        return smtpServerAddress;
    }

    /**
     * Get smtp server port
     *
     * @return An integer corresponding to the smtp server port
     */
    public int getSmtpServerPort() {
        return smtpServerPort;
    }

    /**
     * Get the number of groups
     *
     * @return An integer corresponding to the number of groups to prank
     */
    public int getNbGroups() {
        return nbGroups;
    }
}
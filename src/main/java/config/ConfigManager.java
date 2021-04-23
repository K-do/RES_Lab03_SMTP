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
    private String smtpServerAddress;
    private int smtpServerPort;
    private int numberOfGroups;
    private final List<Person> victims;
    private final List<String> messages;
    private List<String> addressesToCC;

    /**
     * Constructor
     *
     * @throws IOException - if config files failed to open/read
     */
    public ConfigManager() throws IOException {
        victims = loadPersonsFromFile();
        messages = loadMessagesFromFile();
        loadProperties();
    }

    /**
     * Load properties from config.properties
     *
     * @throws IOException - If file could not be open/read
     */
    private void loadProperties() throws IOException {

        // Load properties file
        BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream("./config/config.properties"), StandardCharsets.UTF_8));
        Properties properties = new Properties();
        properties.load(in);

        // Get server address & port
        smtpServerAddress = properties.getProperty("smtpServerAddress");
        if(smtpServerAddress == null){
            throw new RuntimeException("smtpServerAddress should not be empty");
        }
        smtpServerPort = Integer.parseInt(properties.getProperty("smtpServerPort"));

        // Get number of groups
        numberOfGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));

        // Get addresses to cc
        String[] witnesses = properties.getProperty("witnessesToCC").split(",");
        addressesToCC = new ArrayList<>(Arrays.asList(witnesses));
    }

    /**
     * Load Persons from a json file
     *
     * @return a list of Persons
     * @throws IOException - If file could not be open/read
     */
    private List<Person> loadPersonsFromFile() throws IOException {
        Gson gson = new Gson();
        List<Person> result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream("./config/victims.json"), StandardCharsets.UTF_8))) {

            // Read json file with Gson
            result = new ArrayList<>(Arrays.asList(gson.fromJson(in, Person[].class)));
        }
        return result;
    }

    /**
     * Load messages from utf8 file
     *
     * @return a list of string corresponding to the messages
     * @throws IOException - If file could not be open/read
     */
    private List<String> loadMessagesFromFile() throws IOException {
        List<String> result;
        try (BufferedReader in = new BufferedReader(new InputStreamReader(
                new FileInputStream("./config/messages.utf8"), StandardCharsets.UTF_8))) {

            // Read each message separated by "=="
            result = new ArrayList<>();
            String line;
            while ((line = in.readLine()) != null) {
                StringBuilder body = new StringBuilder();
                while ((line != null) && (!line.equals("=="))) {
                    body.append(line);
                    body.append("\r\n");
                    line = in.readLine();
                }
                result.add(body.toString());
            }
        }
        return result;
    }

    /**
     * Get victims
     *
     * @return A list of Person corresponding to the victims
     */
    public List<Person> getVictims() {
        return victims;
    }

    /**
     * Get messages
     *
     * @return A list of Messages corresponding to the victims
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Get addresses to cc
     *
     * @return A list of String corresponding to the addresses to cc
     */
    public List<String> getAddressesToCC() {
        return addressesToCC;
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
     * @return an integer corresponding to the number of groups to prank
     */
    public int getNumberOfGroups() {
        return numberOfGroups;
    }
}
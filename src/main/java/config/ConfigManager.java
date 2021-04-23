package config;

import model.mail.Person;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConfigManager {
    private String smtpServerAddress;
    private int smtpServerPort;
    private final List<Person> victims;
    private final List<String> messages;
    private int numberOfGroups;
    private List<Person> witnessesToCC;

    public ConfigManager() throws IOException {
        victims = loadAddressesFromFile();
        messages = loadMessagesFromFile();
        loadProperties();
    }

    private void loadProperties() throws IOException {
        FileInputStream fis = new FileInputStream("./config/config.properties");
        Properties properties = new Properties();
        properties.load(fis);
        this.smtpServerAddress = properties.getProperty("smtpServerAddress");
        this.smtpServerPort = Integer.parseInt(properties.getProperty("smtpServerPort"));
        this.numberOfGroups = Integer.parseInt(properties.getProperty("numberOfGroups"));

        this.witnessesToCC = new ArrayList<>();
        String witnesses = properties.getProperty("witnessesToCC");
        String[] witnessesAddresses = witnesses.split(",");
        for(String address : witnessesAddresses) {
            this.witnessesToCC.add(new Person(address));
        }
    }

    private List<Person> loadAddressesFromFile() throws IOException {
        List<Person> result;
        try(FileInputStream fis = new FileInputStream("./config/victims.utf8")) {
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try(BufferedReader reader = new BufferedReader(isr)) {
                result = new ArrayList<>();
                String address = reader.readLine();
                while(address != null) {
                    result.add(new Person(address));
                    address = reader.readLine();
                }
            }
        }
        return result;
    }

    private List<String> loadMessagesFromFile() throws IOException {
        List<String> result;
        try(FileInputStream fis = new FileInputStream("./config/messages.utf8")) {
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            try(BufferedReader reader = new BufferedReader(isr)) {
                result = new ArrayList<>();
                String line = reader.readLine();
                while(line != null) {
                    StringBuilder body = new StringBuilder();
                    while((line != null) && (!line.equals("=="))) {
                        body.append(line);
                        body.append("\r\n");
                        line = reader.readLine();
                    }
                    result.add(body.toString());
                    line = reader.readLine();
                }
            }
        }
        return result;
    }

    public List<Person> getVictims() {
        return victims;
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<Person> getWitnessesToCC() {
        return witnessesToCC;
    }

    public String getSmtpServerAddress() {
        return smtpServerAddress;
    }

    public int getSmtpServerPort() {
        return smtpServerPort;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }
}

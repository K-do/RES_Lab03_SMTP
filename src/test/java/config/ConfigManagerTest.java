package config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.mail.Person;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class ConfigManagerTest {

    private final static String dirPath = "./src/test/tmpConfig";

    @BeforeAll
    public static void setUpConfig() {

        // Create tmp config dir
        File directory = new File(dirPath);
        directory.mkdirs();

        // Create victims.json
        List<Person> victims = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            victims.add(new Person("test", "test", "test_" + i + "@res.ch"));
        }
        try (OutputStreamWriter writer = new OutputStreamWriter(
                new FileOutputStream(dirPath + "/victims.json"), StandardCharsets.UTF_8)) {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(victims, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create messages.utf8
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/messages.utf8"),
                        StandardCharsets.UTF_8))) {

            for (int i = 0; i < 5; ++i) {
                writer.print("Subject: test_" + i + "\r\nThis is a simple message\r\n==\r\n");
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void loadVictimsAndGetThemShouldWork() {

        // Create config.properties
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/config.properties"),
                        StandardCharsets.UTF_8))) {

            writer.println("smtpServerAddress=localhost");
            writer.println("smtpServerPort=4224");
            writer.println("numberOfGroups=2");
            writer.println("witnessesToCC=test_cc@res.ch");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConfigManager manager = new ConfigManager(dirPath);
            List<Person> persons = manager.getVictims();
            assertEquals(8, persons.size());

            System.out.println("Victims: ");
            for (int i = 0; i < persons.size(); ++i) {
                Person p = persons.get(i);
                System.out.println("- " + p.getAddress());
                assertEquals("test_" + i + "@res.ch", p.getAddress());
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }


    @Test
    public void loadMessagesAndGetThemShouldWork() {

        // Create config.properties
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/config.properties"),
                        StandardCharsets.UTF_8))) {

            writer.println("smtpServerAddress=localhost");
            writer.println("smtpServerPort=4224");
            writer.println("numberOfGroups=2");
            writer.println("witnessesToCC=test_cc@res.ch");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConfigManager manager = new ConfigManager(dirPath);
            List<String> messages = manager.getMessages();
            assertEquals(5, messages.size());

            System.out.println("Messages: ");
            for (int i = 0; i < messages.size(); ++i) {
                String s = messages.get(i);
                System.out.println(s);
                assertEquals("Subject: test_" + i + "\r\nThis is a simple message\r\n", s);
            }

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void loadPropertiesAndGetThemShouldWork() {

        // Create config.properties
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/config.properties"),
                        StandardCharsets.UTF_8))) {

            writer.println("smtpServerAddress=localhost");
            writer.println("smtpServerPort=4224");
            writer.println("numberOfGroups=2");
            writer.println("witnessesToCC=test_cc@res.ch");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            ConfigManager manager = new ConfigManager(dirPath);
            int servPort = manager.getSmtpServerPort();
            System.out.println("server port = " + servPort);

            String servAddress = manager.getSmtpServerAddress();
            System.out.println("server address = " + servAddress);

            int nbGroups = manager.getNbGroups();
            System.out.println("number of groups = " + nbGroups);

            List<String> cc = manager.getAddressesToCC();
            System.out.println("addresses to cc:");
            for (String s : cc) {
                System.out.println("- " + s);
            }

            assertEquals(4224, servPort);
            assertEquals("localhost", servAddress);
            assertEquals(2, nbGroups);
            assertEquals(1, cc.size());

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void loadPropertiesShouldThrowWithNullSmtpServerAddress() {

        // Create config.properties
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/config.properties"),
                        StandardCharsets.UTF_8))) {

            writer.println("smtpServerAddress=");
            writer.println("smtpServerPort=2525");
            writer.println("numberOfGroups=2");
            writer.println("witnessesToCC=");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Throws RunTimeException because smtServer address is empty");
        assertThrows(RuntimeException.class, () -> {
            new ConfigManager(dirPath);
        });
    }

    @Test
    public void loadPropertiesShouldThrowWithInvalidSmtpServerPort() {

        // Create config.properties
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/config.properties"),
                        StandardCharsets.UTF_8))) {

            writer.println("smtpServerAddress=localhost");
            writer.println("smtpServerPort=notANumber");
            writer.println("numberOfGroups=2");
            writer.println("witnessesToCC=");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Throws RunTimeException because smtServer port is notANumber");
        assertThrows(RuntimeException.class, () -> {
            new ConfigManager(dirPath);
        });
    }

    @Test
    public void loadPropertiesShouldThrowWithInvalidNumberOfGroups() {

        // Create config.properties
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(dirPath + "/config.properties"),
                        StandardCharsets.UTF_8))) {

            writer.println("smtpServerAddress=localhost");
            writer.println("smtpServerPort=4224");
            writer.println("numberOfGroups=0");
            writer.println("witnessesToCC=");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Throws RunTimeException because numberOfGroups is <= 0");
        assertThrows(RuntimeException.class, () -> {
            new ConfigManager(dirPath);
        });
    }

    @AfterAll
    public static void removeConfig() {
        File dir = new File(dirPath);
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

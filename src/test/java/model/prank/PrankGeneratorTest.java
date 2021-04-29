package model.prank;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import config.ConfigManager;
import model.mail.Mail;
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

public class PrankGeneratorTest {

    private final static String dirPath = "./src/test/tmpConfig";

    @BeforeAll
    public static void setUpConfig() {

        // Create tmp config dir
        File directory = new File(dirPath);
        directory.mkdirs();

        // Create victims.json
        List<Person> victims = new ArrayList<>();
        for (int i = 0; i < 8; ++i) {
            victims.add(new Person("john", "_" + i, "test_" + i + "@res.ch"));
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
    }

    @Test
    public void creationShouldThrowsWithNullParameter() {
        System.out.println("Throws NullPointerException because param is null");
        assertThrows(NullPointerException.class, () -> {
            new PrankGenerator(null);
        });
    }

    @Test
    public void itShouldGeneratePrankedMails() {
        try {
            ConfigManager config = new ConfigManager(dirPath);

            PrankGenerator generator = new PrankGenerator(config);
            List<Mail> pranks = generator.generateMails();

            List<String> valid_addresses = new ArrayList<>();
            for (Person p : config.getVictims()) {
                valid_addresses.add(p.getAddress());
            }

            List<String> prank_addresses = new ArrayList<>();
            System.out.println("pranks:");
            for (Mail m : pranks) {
                System.out.println("from: " + m.getFrom());
                System.out.println("...");
                System.out.println("subject: " + m.getSubject());
                System.out.println("body:" + m.getBody());
                System.out.println();

                // Check from
                assertTrue(valid_addresses.contains(m.getFrom()));
                prank_addresses.add(m.getFrom());

                // Check to
                for (String s : m.getTo()) {
                    assertTrue(valid_addresses.contains(s));
                    if (prank_addresses.contains(s)) {
                        fail();
                    } else {
                        prank_addresses.add(s);
                    }
                }

                // Check cc
                assertEquals(config.getAddressesToCC(), m.getCc());
            }

            assertEquals(config.getNbGroups(), pranks.size());

        } catch (IOException e) {
            e.printStackTrace();
        }

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

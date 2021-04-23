package config;

import model.mail.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

public class ConfigManagerTest {

    @Test
    public void loadPersonFromfileShouldWork() {

        try {
            ConfigManager manager = new ConfigManager();
            List<Person> persons = manager.getVictims();
            List<String> messages = manager.getMessages();

            for (Person p : persons){
                System.out.println(p.getFirstName());
            }

            for (String m : messages){
                System.out.print(m);
            }

            assertTrue(true);

        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }


    }


}

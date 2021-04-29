package model.mail;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class GroupTest {

    @Test
    public void creationShouldThrowWithNullSender() {
        System.out.println("Throws NullPointerException because sender is null");
        assertThrows(NullPointerException.class, () -> {
            new Group(null, new ArrayList<>());
        });
    }

    @Test
    public void creationShouldThrowWithNotEnoughRecipients() {
        System.out.println("Throws NullPointerException because number of recipients < 2");
        List<Person> recipients = new ArrayList<>();
        recipients.add(new Person("", "", ""));

        assertThrows(NullPointerException.class, () -> {
            new Group(new Person("", "", ""), recipients);
        });
    }

    @Test
    public void creationShouldWorkWithValidParams() {
        Person sender = new Person("troll", "master", "test_0@res.ch");
        List<Person> recipients = new ArrayList<>();
        recipients.add(new Person("Paul", "DelaCroix", "test_1@res.ch"));
        recipients.add(new Person("Pierre", "DuRond", "test_2@res.ch"));

        Group g = new Group(sender, recipients);
        String g_sender_addr = g.getSender().getAddress();
        System.out.println("sender: " + g_sender_addr);
        assertEquals("test_0@res.ch", g_sender_addr);

        System.out.println("recipients: ");
        List<Person> g_recipients = g.getRecipients();
        for (Person p : g_recipients) {
            System.out.println("- " + p.getAddress());
        }
        assertEquals(2, g.getRecipients().size());
    }

}

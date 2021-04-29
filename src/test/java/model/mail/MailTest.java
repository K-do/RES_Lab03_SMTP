package model.mail;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MailTest {

    @Test
    public void creationShouldThrowWithNullFrom() {
        System.out.println("Throws NullPointerException because from is null");
        List<String> to = new ArrayList<>();
        to.add("test_1@res.ch");

        assertThrows(NullPointerException.class, () -> {
            new Mail(null, to, null, null, null, null);
        });
    }

    @Test
    public void creationShouldThrowWithNotEnoughRecipients() {
        System.out.println("Throws NullPointerException because to is empty");
        List<String> to = new ArrayList<>();

        assertThrows(NullPointerException.class, () -> {
            new Mail("test_0@res.ch", to, null, null, null, null);
        });
    }

    @Test
    public void creationShouldWorkWithValidParams() {
        String from = "test_0@res.ch";
        List<String> to = new ArrayList<>();
        to.add("test_1@res.ch");
        to.add("test_2@res.ch");
        List<String> cc = new ArrayList<>();
        cc.add("cc_0@res.ch");
        String subject = "Test";
        String contentType = "text/plain; charset=\"utf-8\"";
        String body = "A Simple test";

        Mail mail = new Mail(from, to, cc, subject, contentType, body);

        String m_from = mail.getFrom();
        System.out.println("from: " + m_from);
        assertEquals(from, m_from);

        List<String> m_to = mail.getTo();
        System.out.println("to:");
        for (String s : m_to) {
            System.out.println("- " + s);
        }
        assertEquals(to, m_to);

        List<String> m_cc = mail.getCc();
        System.out.println("cc:");
        for (String s : m_cc) {
            System.out.println("- " + s);
        }
        assertEquals(cc, m_cc);

        String m_subject = mail.getSubject();
        System.out.println("subject: " + m_subject);
        assertEquals(subject, m_subject);

        String m_contentType = mail.getContentType();
        System.out.println("contentType: " + m_contentType);
        assertEquals(contentType, m_contentType);
    }

}

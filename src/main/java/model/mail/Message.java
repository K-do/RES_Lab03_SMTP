package model.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Content of a mail
 *
 * @author Olivier Liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class Message {
    private String from;
    private List<String> to = new ArrayList<>();
    private List<String> cc = new ArrayList<>();
    private String subject;
    private String body;

    /**
     * Get from
     *
     * @return from
     */
    public String getFrom() {
        return from;
    }

    /**
     * Get to
     *
     * @return to
     */
    public List<String> getTo() {
        return to;
    }

    /**
     * Get cc
     *
     * @return cc
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * Get subject
     *
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Set subject
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Get body
     *
     * @return body
     */
    public String getBody() {
        return body;
    }

    /**
     * Set to
     *
     * @param to
     */
    public void setTo(List<String> to) {
        this.to = to;
    }

    /**
     * Set cc
     *
     * @param cc
     */
    public void setCc(List<String> cc) {
        this.cc = cc;
    }


    /**
     * Set from
     *
     * @param from
     */
    public void setFrom(String from) {
        this.from = from;
    }


    /**
     * Set body
     *
     * @param body
     */
    public void setBody(String body) {
        this.body = body;
    }
}

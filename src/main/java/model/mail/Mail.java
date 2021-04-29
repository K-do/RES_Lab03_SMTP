package model.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * Content of a Mail
 *
 * @author Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class Mail {
    private final String from;
    private final List<String> to;
    private final List<String> cc;
    private final String subject;
    private final String contentType;
    private final String body;

    /**
     * Constructor
     *
     * @param from        String address
     * @param to          List<String> addresses
     * @param cc          List<String> addresses
     * @param subject     String mail subject
     * @param contentType String content type of the body
     * @param body        String body of the mail
     */
    public Mail(String from, List<String> to, List<String> cc, String subject, String contentType, String body) {
        // Check mandatory parameters
        if (from == null || from.isEmpty()) {
            throw new NullPointerException("From is required !");
        }
        if (to == null || to.size() == 0) {
            throw new NullPointerException("At least one 'to' is required !");
        }

        this.from = from;
        this.to = to;
        this.cc = (cc == null ? new ArrayList<>() : cc);
        this.subject = subject;
        this.contentType = contentType;
        this.body = body;
    }


    /**
     * Get from
     *
     * @return the mail of the sender
     */
    public String getFrom() {

        return from;
    }

    /**
     * Get to
     *
     * @return mails of victims
     */
    public List<String> getTo() {

        return to;
    }

    /**
     * Get cc
     *
     * @return mails to cc
     */
    public List<String> getCc() {
        return cc;
    }

    /**
     * Get subject
     *
     * @return subject of the mail
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Get content type
     *
     * @return content-type of the mail
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Get body
     *
     * @return body of the mail
     */
    public String getBody() {
        return body;
    }

}

package model.prank;

import model.mail.Mail;
import model.mail.Person;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Prank {
    private Person victimeSender;
    private final List<Person> victimeRecipents = new ArrayList<>();
    private final List<Person> witnessRecipients = new ArrayList<>();
    private String message;

    public Person getVictimSender() {
        return victimSender;
    }

    void setVictimSender(Person victim) {
        this.victimSender = victim;
    }

    public String getMessage() {
        return message;
    }

    void setMessage(String message) {
        this.message = message;
    }

    void addVictimRecipients(List<Person> victims) {
        victimRecipients.addAll(victims);
    }

    void addWitnessRecipients(List<Person> witnesses) {
        witnessRecipients.addAll(witnesses);
    }

    public List<Person> getVictimRecipients() {
        return new ArrayList<>(victimRecipients);
    }

    public List<Person> getWitnessRecipients() {
        return new ArrayList<>(witnessRecipients);
    }

    public Mail generalMailMessage() {
        Mail message = new Mail();
        message.setFrom(victimSender.getAddress());

        String[] to = victimRecipients
                .stream()
                .map(Person::getAddress)
                .collect(Collectors.toList())
                .toArray(new String[]{});
        message.setTo(to);

        String[] cc = witnessRecipients
                .stream()
                .map(Person::getAddress)
                .collect(Collectors.toList())
                .toArray(new String[]{});
        message.setCc(cc);

        message.setBody(this.message + "\r\n" + victimSender.getFirstName() + " " + victimSender.getLastName());

        return message;
    }





}

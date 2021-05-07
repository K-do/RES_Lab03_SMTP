package smtp;

import model.mail.Mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

/**
 * Implementation of SMTP protocol
 *
 * @author Olivier Liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class SmtpClient {
    private final String servAddress;
    private final int servPort;

    /**
     * Constructor
     *
     * @param address ip address of smtp server
     * @param port    port number of smtp server
     */
    public SmtpClient(String address, int port) {
        this.servAddress = address;
        this.servPort = port;
    }

    /**
     * Send mails to the server
     *
     * @param mails
     * @throws IOException - If there is a SMTP error
     */
    public void send(List<Mail> mails) throws IOException {
        Socket socket = new Socket(servAddress, servPort);
        System.out.println("Successfully connected");
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        reader.readLine();

        // Start of SMTP Session
        writer.print("EHLO " + servAddress + "\r\n");
        writer.flush();

        // Server response
        String line;
        do {
            line = reader.readLine();
            if(!line.startsWith("250")) {
                throw new IOException("SMTP error: " + line);
            }
        } while (!line.startsWith("250 "));

        // Process each mail
        for (Mail mail : mails) {
            // from
            writer.print("MAIL FROM: " + mail.getFrom() + "\r\n");
            writer.flush();
            reader.readLine();

            // to
            for (String to : mail.getTo()) {
                writer.print("RCPT TO: " + to + "\r\n");
                writer.flush();
                reader.readLine();
            }

            // cc
            for (String cc : mail.getCc()) {
                writer.print("RCPT TO: " + cc + "\r\n");
                writer.flush();
                reader.readLine();
            }

            // data
            writer.print("DATA\r\n");
            writer.flush();
            reader.readLine();

            // content-type
            writer.print("Content-Type: " + mail.getContentType() + "\r\n");

            // body-from
            writer.print("From: " + mail.getFrom() + "\r\n");

            // body-to
            writer.print("To: " + mail.getTo().get(0));
            for (int i = 1; i < mail.getTo().size(); ++i) {
                writer.print(", " + mail.getTo().get(i));
            }
            writer.print("\r\n");

            // body-cc
            writer.print("Cc: " + mail.getCc().get(0));
            for (int i = 1; i < mail.getCc().size(); ++i) {
                writer.print(", " + mail.getCc().get(i));
            }
            writer.print("\r\n");

            // subject
            Base64.Encoder encoder = Base64.getEncoder();
            String subject = "=?utf-8?B?" +
                    encoder.encodeToString(mail.getSubject().getBytes(StandardCharsets.UTF_8)) + "?=";
            writer.print("Subject: " + subject + "\r\n\r\n");

            // body-data
            writer.print(mail.getBody());

            // end-body
            writer.print("\r\n.\r\n");
            writer.flush();
            System.out.println(reader.readLine());

            // display mail
            System.out.println("Message send:");
            System.out.println(mail.getBody() + "\r\n\r\n");
        }

        // End
        writer.write("QUIT\r\n");
        writer.flush();

        writer.close();
        reader.close();
        socket.close();
    }
}
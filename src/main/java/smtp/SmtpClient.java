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
     * @throws IOException
     */
    public void send(List<Mail> mails) throws IOException {
        Socket socket = new Socket(servAddress, servPort);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        // Display message => Log successfully
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer.print("EHLO TEST\r\n");
        writer.flush();

        // Commenter
        String line;
        do {
            line = reader.readLine();
            System.out.println(">" + line);
        } while (!line.startsWith("250 "));
        System.out.println();

        // traiter chaque mail
        for (Mail mail : mails) {
            // from
            writer.print("MAIL FROM: " + mail.getFrom() + "\r\n");
            writer.flush();
            System.out.println(">" + reader.readLine());

            // to
            for (String to : mail.getTo()) {
                writer.print("RCPT TO: " + to + "\r\n");
                writer.flush();
                System.out.println(">" + reader.readLine());
            }

            // cc
            for (String cc : mail.getCc()) {
                writer.print("RCPT TO: " + cc + "\r\n");
                writer.flush();
                System.out.println(">" + reader.readLine());
            }

            // data
            writer.print("DATA\r\n");
            writer.flush();
            System.out.println(">" + reader.readLine());

            // content type
            writer.print(mail.getContentType() + "\r\n");
            writer.flush();

            // body-from
            writer.print("From: " + mail.getFrom() + "\r\n");
            //writer.flush();

            // body-to
            writer.print("To: " + mail.getTo().get(0));
            for (int i = 1; i < mail.getTo().size(); ++i) {
                writer.print(", " + mail.getTo().get(i));
            }
            writer.print("\r\n");
            //writer.flush();

            // body-cc
            writer.print("Cc: " + mail.getCc().get(0));
            for (int i = 1; i < mail.getCc().size(); ++i) {
                writer.print(", " + mail.getCc().get(i));
            }
            writer.print("\r\n");
            //writer.flush();

            // subject
            Base64.Encoder encoder = Base64.getEncoder();
            String subject = "=?utf-8?B?" +
                    encoder.encodeToString(mail.getSubject().getBytes(StandardCharsets.UTF_8)) + "?=";
            writer.print("Subject: " + subject + "\r\n\r\n");
            //writer.flush();

            // body-data
            writer.print(mail.getBody());
            //writer.flush();

            // end-body
            writer.print("\r\n.\r\n");
            writer.flush();
            System.out.println(">" + reader.readLine());
        }

        // End
        writer.write("QUIT\r\n");
        writer.flush();

        writer.close();
        reader.close();
        socket.close();
    }
}
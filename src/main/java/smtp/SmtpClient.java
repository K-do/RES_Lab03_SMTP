package smtp;

import model.mail.Message;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of SMTP protocol
 *
 * @author Olivier Liechti, Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class SmtpClient {
    private static final Logger LOG = Logger.getLogger(SmtpClient.class.getName());
    private final String smtpServerAddress;
    private final int smtpServerPort;

    /**
     * Constructor
     *
     * @param address ip address of smtp server
     * @param port port number of smtp server
     */
    public SmtpClient(String address, int port) {
        this.smtpServerAddress = address;
        this.smtpServerPort = port;
    }

    public void sendMessage(Message message) throws IOException {
        LOG.info("Sending message via SMTP");
        Socket socket = new Socket(smtpServerAddress, smtpServerPort);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        // Get serv infos
        String line = reader.readLine();
        LOG.info(line);
        writer.printf("EHLO localhost\r\n");
        line = reader.readLine();
        LOG.info(line);

        // Get smtp server configuration details
        if(!line.startsWith("250")) {
            throw new IOException("SMTP error: " + line);
        }
        while(line.startsWith("250-")) {
            line = reader.readLine();
            LOG.info(line);
        }

        // Send sender
        writer.write("MAIL FROM:");
        writer.write(message.getFrom());
        writer.write("\r\n");
        writer.flush();
        line = reader.readLine();
        LOG.info(line);

        // Send victims
        for(String to : message.getTo()) {
            writer.write("RCPT TO:");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();
            line = reader.readLine();
            LOG.info(line);
        }

        // Send witnesses
        for(String to : message.getCc()) {
            writer.write("RCPT TO:");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();
            line = reader.readLine();
            LOG.info(line);
        }

        // Data
        writer.write("DATA");
        writer.write("\r\n");
        writer.flush();
        line = reader.readLine();
        LOG.info(line);

        // Content-type header
        writer.write("Content-Type: text/plain; charset=\"utf-8\"\r\n");

        // From header
        writer.write("From: " + message.getFrom() + "\r\n");

        // To header
        List<String> to = message.getTo();
        writer.write("To: " + to.get(0));
        for(int i = 1; i < to.size(); i++) {
            writer.write(", " + to.get(i));
        }
        writer.write("\r\n");

        // Cc header
        List<String> cc = message.getCc();
        writer.write("Cc: " + cc.get(0));
        for(int i = 1; i < cc.size(); i++) {
            writer.write(", " + cc.get(i));
        }
        writer.write("\r\n");
        writer.flush();

        // Encode subject
        Base64.Encoder encoder = Base64.getEncoder();
        message.setSubject("=?utf-8?B?" + encoder.encodeToString(message.getSubject().getBytes(StandardCharsets.UTF_8)) + "?=");

        // Subject header
        writer.write("Subject: " + message.getSubject());
        writer.write("\r\n");
        writer.write("\r\n");

        // Body
        writer.write(message.getBody());
        writer.write("\r\n.\r\n");
        writer.flush();
        line = reader.readLine();
        LOG.info(line);

        // Quit smtp serv
        writer.write("QUIT\r\n");
        writer.flush();

        // Clean
        writer.close();
        reader.close();
        socket.close();
    }
}
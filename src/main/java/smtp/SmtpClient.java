package smtp;

import model.mail.Mail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;


public class SmtpClient {

    private static final Logger LOG = Logger.getLogger(SmtpClient.class.getName());
    private String smtpServerAddress;
    private int smtpServerPort;
    private PrintWriter writer;
    private BufferedReader reader;

    public SmtpClient(String address, int port) {
        this.smtpServerAddress = address;
        this.smtpServerPort = port;
    }

    @Override
    public void sendMessage(Mail message) throws IOException {
        LOG.info("Sending message via SMTP");
        Socket socket = new Socket(smtpServerAddress, smtpServerPort);
        writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

        String line = reader.readLine();
        LOG.info(line);
        writer.printf("EHLO localhost\r\n");
        line = reader.readLine();
        LOG.info(line);

        if(!line.startsWith("250")) {
            throw new IOException("SMTP error: " + line);
        }
        while(line.startsWith("250-")) {
            line = reader.readLine();
            LOG.info(line);
        }

        writer.write("MAIL FROM:");
        writer.write(message.getFrom());
        writer.write("\r\n");
        writer.flush();
        line = reader.readLine();
        LOG.info(line);

        for(String to : message.getTo()) {
            writer.write("RCPT TO:");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();
            line = reader.readLine();
            LOG.info(line);
        }

        for(String to : message.getCc()) {
            writer.write("RCPT TO:");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();
            line = reader.readLine();
            LOG.info(line);
        }

        for(String to : message.getBcc()) {
            writer.write("RCPT TO:");
            writer.write(to);
            writer.write("\r\n");
            writer.flush();
            line = reader.readLine();
            LOG.info(line);
        }

        writer.write("DATA");
        writer.write("\r\n");
        writer.flush();
        line = reader.readLine();
        LOG.info(line);
        writer.write("Content-Type: text/plain; charset=\"utf-8\"\r\n");

        writer.write("From: " + message.getFrom() + "\r\n");

        writer.write("To: " + message.getTo()[0]);
        for(int i = 1; i < message.getTo().length; i++) {
            writer.write(", " + message.getTo()[i]);
        }
        writer.write("\r\n");

        writer.write("Cc: " + message.getCc()[0]);
        for(int i = 1; i < message.getCc().length; i++) {
            writer.write(", " + message.getCc()[i]);
        }
        writer.write("\r\n");
        writer.flush();

        LOG.info(message.getBody());
        String[] completeBody = message.getBody().split("\r\n", 2);
        if(completeBody[0].startsWith("Subject: ")) {
            completeBody[0] = completeBody[0].replace("Subject: ", "");
        }
        message.setSubject(MimeUtility.encodeText(completeBody[0], "utf-8", "B"));
        message.setBody(completeBody[1]);
        writer.write("Subject: " + message.getSubject());
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write(message.getBody());
        writer.write("\r\n");
        writer.flush();

        writer.write("\r\n.\r\n");
        writer.flush();
        line = reader.readLine();
        LOG.info(line);

        writer.write("QUIT\r\n");
        writer.flush();
        writer.close();
        reader.close();
        socket.close();
    }
}

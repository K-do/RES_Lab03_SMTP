package smtp;

import model.mail.Mail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
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
     * @param port port number of smtp server
     */
    public SmtpClient(String address, int port) {
        this.servAddress = address;
        this.servPort = port;
    }

    /**
     * Send mails to the server
     * @param mails
     * @throws IOException
     */
    public void send(List<Mail> mails) throws IOException {
        Socket socket = new Socket(servAddress, servPort);
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
        writer.print("EHLO TEST\r\n");
        writer.flush();
        do{
            System.out.println(">" +reader.readLine());
        } while(!reader.equals("250 OK"));
        System.out.println(">" + reader.readLine());

        for(Mail mail : mails){
            writer.print("MAIL FROM: " + mail.getFrom() + "\r\n");
            writer.flush();
            System.out.println(">" + reader.readLine());
            for(String to : mail.getTo()){
                writer.print("RCPT TO: " + to + "\r\n");
                writer.flush();
                System.out.println(">" + reader.readLine());
            }
            for(String cc : mail.getCc()){
                writer.print("RCPT TO: " + cc + "\r\n");
                writer.flush();
                System.out.println(">" + reader.readLine());
            }
            writer.print("DATA\r\n");
            writer.flush();
            System.out.println(">" + reader.readLine());
            writer.print("From: " + mail.getFrom() + "\r\n");
            writer.print("To: " + mail.getTo().get(0));
            for(String to : mail.getTo()){
                writer.print(",",)
            }


        }








    }
}
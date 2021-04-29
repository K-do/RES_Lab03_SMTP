import config.ConfigManager;
import model.mail.Mail;
import model.prank.PrankGenerator;
import smtp.SmtpClient;

import java.io.IOException;
import java.util.List;

/**
 * Main
 *
 * @author Alexandra Cerottini, Miguel Do Vale Lopes
 */
public class MailRobot {

    public static void main(String[] args) {
        try {
            ConfigManager configManager = new ConfigManager("./config");

            // Generate the pranks
            List<Mail> pranks = new PrankGenerator(configManager).generateMails();

            // Send the pranks
            SmtpClient client = new SmtpClient(configManager.getSmtpServerAddress(), configManager.getSmtpServerPort());
            client.send(pranks);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

import config.ConfigManager;
import model.prank.Prank;
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
            // Generate the pranks
            List<Prank> pranks = new PrankGenerator().generatePranks();

            // Send each prank to the smtpServer
            ConfigManager configManager = new ConfigManager();
            for (Prank p : pranks) {
                SmtpClient client = new SmtpClient(configManager.getSmtpServerAddress(), configManager.getSmtpServerPort());
                client.send(p.generateMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

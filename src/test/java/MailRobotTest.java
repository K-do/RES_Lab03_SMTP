import config.ConfigManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.*;

public class MailRobotTest {

    public static void main(String[] args) {

        try {
            ConfigManager configManager = new ConfigManager();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

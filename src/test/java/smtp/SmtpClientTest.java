package smtp;

import model.prank.Prank;
import model.prank.PrankGenerator;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class SmtpClientTest {

    @Test
    public void test() throws IOException {

        SmtpClient client = new SmtpClient("localhost", 2525);


        List<Prank> pranks = new PrankGenerator().generatePranks();

        client.send(pranks.get(0).generateMessage());

    }


}

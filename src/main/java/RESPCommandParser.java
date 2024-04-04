import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RESPCommandParser {
    public List<String> readRespCommands(InputStream inputStream) {

        // *2\r\n$5\r\nhello\r\n$5\r\nworld\r\n

        List<String> respCommands = new ArrayList<>();
        int b = inputStream.read();

            while (true) {
                char type = (char) b;

                switch (type) {
                    case '*':
                        StringBuilder numCommandBuilder = new StringBuilder();
                        while ((b = inputStream.read()) != '\r') {
                            numCommandBuilder.append((char) b);
                        }

                        inputStream.read();

                        int numCommands = Integer.parseInt(numCommandBuilder.toString());

                            for (int i = 0; i < numCommands; i++) {
                                StringBuilder componentString = new StringBuilder();
                                inputStream.read();

                                while ((b = inputStream.read()) != '\r') {
                                    componentString.append((char) b);
                                }
                                inputStream.read();

                                int numComponentStr = Integer.parseInt(componentString.toString());
                                StringBuilder finalStr = new StringBuilder();

                                for (int j = 0; j < numComponentStr; j++) {
                                    finalStr.append((char) inputStream.read());
                                }

                                respCommands.add(finalStr.toString());
                                inputStream.read();
                                inputStream.read();
                            }
                        
                        return respCommands;
                        
                    default:
                    System.out.println("invalid protocol.");
                    //throw new IOException("Invalid protocol: " + type);
                }

            }
    }

}
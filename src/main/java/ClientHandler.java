import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.List;

public class ClientHandler implements Runnable {

    private final Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try { 
            //BufferedReader inReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream outputStream = clientSocket.getOutputStream();
            PrintWriter printwriter = new PrintWriter(outputStream,true);
            //String input;
            String response = "+PONG\r\n";
            // while ((input = inReader.readLine()) != null) {
            //     System.out.println(input);

            //     if (input.equals("ping")) {
            //         outputStream.write(response.getBytes(Charset.defaultCharset()));
            //     }

            // }

            while (true) {
                RESPCommandParser respCommandParser = new RESPCommandParser();
                List<String> respCommands = respCommandParser.readRespCommands(clientSocket.getInputStream());

                if (respCommands.isEmpty()) {
                    System.out.println("Empty command");
                    continue;
                }
                if (respCommands.get(0).equalsIgnoreCase("PING")){
                    outputStream.write(response.getBytes(Charset.defaultCharset()));
                }

                if(respCommands.get(0).equalsIgnoreCase("ECHO")){
                    
                    response = "+" + respCommands.get(1) + "\r\n";
                    System.out.println(response);
                    outputStream.write(response.getBytes(Charset.defaultCharset()));
                    //printwriter.write("+" + respCommands.get(1) + "\r");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
            //System.out.println("IOException: " + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
                //System.out.println("IOException: " + e.getMessage());
            }
        }
    }

}
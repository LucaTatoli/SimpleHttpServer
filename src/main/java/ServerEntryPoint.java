import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerEntryPoint {
    private ServerSocket serverSocket;
    private boolean running = false;

    public ServerEntryPoint(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        running = true;
        Socket socket;
        while (running) {
            try {
                socket = serverSocket.accept();
                HTTPController controller = new HTTPController(socket);
                Thread t = new Thread(controller);
                t.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isRunning()
    {
        return running;
    }
}

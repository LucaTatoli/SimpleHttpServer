import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static javax.swing.text.html.FormSubmitEvent.MethodType.POST;

public class ServerEntryPoint {
    private ServerSocket serverSocket;
    private boolean running = false;
    private HashMap<String, String> headers;

    public ServerEntryPoint(int port) {
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        headers = new HashMap<>();
    }

    public void listen() {
        running = true;
        Socket socket;
        String newConnectionString = "==========================================\nNew connection from: %s\n==========================================\n\n\n";
        String endConnectionString = "==========================================\nEnd connection with: %s\n==========================================\n\n\n";
        while (running) {
            try {
                socket = serverSocket.accept();
                String addr = socket.getInetAddress().toString();
                System.out.printf(newConnectionString, addr);
                requestHandler(socket);
                System.out.printf(endConnectionString, addr);
                socket.close();
                headers.clear();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestHandler(Socket connection) throws IOException {
        String request = getRequest(connection.getInputStream());
        String http_method = request.substring(0, request.indexOf("\n")).split(" ")[0];
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        if (!ApplicationConstants.HTTP_METHODS.contains(http_method))
            bw.write(HttpResponse.methodNotSupportedResponse());
        else
            bw.write(HttpResponse.okResponse());
        bw.flush();
        System.out.println(request);
    }

    public String getRequest(InputStream stream) throws IOException {
        StringBuilder request = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        getHeaders(br, request);
        if(headers.containsKey("Content-Length") || headers.containsKey("Transfer-Encoding"))
            getBody(br, request);
        return request.toString();
    }

    public void getHeaders(BufferedReader br, StringBuilder request) throws IOException
    {
        String line;
        boolean headerEnded = false;
        boolean skipFirst = true;
        String[] split;
        while (!headerEnded) {
            if (br.ready())
            {
                line = br.readLine();
                if (line.length() == 0)
                {
                    headerEnded = true;
                }
                else if(!skipFirst)
                {
                    split = line.split(":");
                    headers.put(split[0], split[1]);
                }
                else skipFirst = false;
                request.append(line);
                request.append("\n");
            }
        }
    }

    public void getBody(BufferedReader br, StringBuilder request) throws IOException
    {
        String line;
        boolean bodyEnded = false;
        while (!bodyEnded) {
            if (br.ready())
            {
                line = br.readLine();
                if (line.length() == 0)
                {
                    bodyEnded = true;
                }
                else
                {
                    request.append(line);
                    request.append("\n");
                }
            }
        }
    }
}

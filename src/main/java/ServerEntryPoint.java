import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class ServerEntryPoint {
    private ServerSocket serverSocket;
    private boolean running = false;
    private HashMap<String, String> headers;

    public ServerEntryPoint(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        headers = new HashMap<>();
    }

    public void listen()
    {
        running = true;
        Socket socket;
        String newConnectionString = "==========================================\nNew connection from: %s\n==========================================\n\n\n";
        String endConnectionString = "\n\n==========================================\nEnd connection with: %s\n==========================================\n\n\n";
        while (running)
        {
            try
            {
                socket = serverSocket.accept();
                String addr = socket.getInetAddress().toString();
                System.out.printf(newConnectionString, addr);
                requestHandler(socket);
                System.out.printf(endConnectionString, addr);
                socket.close();
                headers.clear();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void requestHandler(Socket connection) throws IOException
    {
        String request = getRequest(connection.getInputStream());
        if(request.length() > 0)
        {
            String http_method = request.substring(0, request.indexOf("\n")).split(" ")[0];
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            if (!ApplicationConstants.HTTP_METHODS.contains(http_method))
                bw.write(HttpResponse.methodNotSupportedResponse());
            else
                bw.write(HttpResponse.okResponse());
            bw.flush();
            System.out.println(request);
        }
    }

    public String getRequest(InputStream stream) throws IOException
    {
        StringBuilder request = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        getHeaders(br, request);
        if(headers.containsKey("Content-Length") || headers.containsKey("Transfer-Encoding"))
            getBody(br, request);
        return request.toString();
    }

    public void getHeaders(BufferedReader br, StringBuilder request) throws IOException
    {
        boolean skipFirst = true;
        String[] split;
        String line = br.readLine();
        while (line != null && line.length() != 0)
        {
            System.out.println(line);
            if(!skipFirst)
            {
                split = line.split(":");
                headers.put(split[0].strip(), split[1].strip());
            }
            else
                skipFirst = false;
            request.append(line);
            request.append("\n");
            line = br.readLine();
        }
    }

    public void getBody(BufferedReader br, StringBuilder request) throws IOException
    {
        int length = Integer.parseInt(headers.get("Content-Length"));
        char[] buffer = new char[length];
        br.read(buffer, 0, length);
        request.append(buffer);
    }
}

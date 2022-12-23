import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerEntryPoint
{
    private ServerSocket serverSocket;
    private boolean running = false;

    public ServerEntryPoint(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void listen()
    {
        running = true;
        Socket socket;
        String newConnectionString = "==========================================\nNew connection from: %s\n==========================================\n";
        String endConnectionString = "==========================================\nEnd connection with: %s\n==========================================\n\n\n";
        while(running)
        {
            try
            {
                socket = serverSocket.accept();
                String addr = socket.getInetAddress().toString();
                System.out.printf(newConnectionString, addr);
                requestHandler(socket);
                System.out.printf(endConnectionString, addr);
                socket.close();
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
        String method = request.substring(0, request.indexOf("\n")).split(" ")[0];
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        if(!ApplicationConstants.HTTP_METHODS.contains(method))
            bw.write(HttpResponse.methodNotSupportedResponse());
        else
            bw.write(HttpResponse.okResponse());
        bw.flush();
    }

    public String getRequest(InputStream stream) throws IOException
    {
        StringBuilder request = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        while (br.ready())
        {
            request.append(br.readLine());
            request.append("\n");
        }
        System.out.println(request);
        return request.toString();
    }
}

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

public class HTTPController implements Runnable {

    private final Socket socket;
    private final HashMap<String, String> headers;

    public HTTPController(Socket socketRequest)
    {
        socket = socketRequest;
        headers = new HashMap<>();
    }

    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        try
        {
            String addr = socket.getInetAddress().toString();
            if(Main.printDebugFlag)
            {
                String newConnectionString = "==========================================\nNew connection from: %s\n==========================================\n\n\n";
                System.out.printf(newConnectionString, addr);
            }
            requestHandler(socket);
            if(Main.printDebugFlag)
            {
                String endConnectionString = "\n\n==========================================\nEnd connection with: %s\n==========================================\n\n\n";
                System.out.printf(endConnectionString, addr);
            }
            socket.close();
            headers.clear();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            long endTime = System.currentTimeMillis();
            System.out.println("Execution time: " + (endTime - startTime) + "ms");
        }
    }

    public void requestHandler(Socket connection) throws IOException
    {
        String request = getRequest(connection.getInputStream());
        if(Main.printDebugFlag)
            System.out.println(request);
        if(request.length() > 0)
        {
            String[] header = request.substring(0, request.indexOf("\n")).split(" ");
            String http_method = header[0];
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            if (!ApplicationConstants.HTTP_METHODS.contains(http_method))
            {
                bw.write(HTTPResponse.methodNotSupportedResponse());
                bw.flush();
            }

            else
            {
                String uri = header[1];
                try
                {
                    Class<?> controllerClass = Main.controllers.get(uri.substring(1));
                    if(controllerClass == null)
                    {
                        bw.write(HTTPResponse.notFound404());
                        bw.flush();
                        return;
                    }
                    RequestController controller = (RequestController) controllerClass.getConstructor().newInstance();
                    String response = controller.handleRequest(request);
                    bw.write(response);
                    bw.flush();
                    if(Main.printDebugFlag)
                        System.out.println(response);
                }
                catch (NoSuchMethodException e)
                {
                    bw.write(HTTPResponse.notFound404());
                    bw.flush();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
//                if(headers.containsKey("Expect") && headers.get("Expect").equals("100-continue"))
//                {
//                    bw.write(HTTPResponse.continue100Response());
//                    System.out.println(HTTPResponse.continue100Response());
//                    bw.flush();
//                }
//                else
//                {
//                    bw.write(HTTPResponse.okResponse());
//                    System.out.println(HTTPResponse.okResponse());
//                    bw.flush();
//                }
            }
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

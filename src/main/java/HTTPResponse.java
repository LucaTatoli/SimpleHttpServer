import java.util.Date;

public class HTTPResponse
{

    public static String methodNotSupportedResponse()
    {
        return "METHOD NOT SUPPORTED";
    }

    public static String okResponse()
    {
        Date now = new Date();
        String headerResponse = "HTTP/1.1 200 OK\n" +
                          "Date: " +
                          now + "\n" +
                          "Server: Apache/2.2.14 (Win32)\n" +
                          "Last-Modified: " +
                          now + "\n" +
                          "Content-Type: text/html\n" +
                          "Connection: Closed\n";

        String bodyResponse = """
                <html>
                <head></head>
                <body>
                <div style="border: 2px solid red; width: 200px">
                <h1>STATUS: 200</h1>
                </div>
                <div style="border: 2px solid red; width: 50px; height: 50px">
                <h2>OK</h2>
                </div>
                </body></html>""";

        headerResponse += "Content-Length: " + bodyResponse.length() + "\n\n";

        return headerResponse + bodyResponse;

    }

    public static String continue100Response()
    {
        return "HTTP/1.1 100 Continue";
    }

    public static String notFound404()
    {
        Date now = new Date();
        String headerResponse = "HTTP/1.1 404 Not Found\n" +
                "Date: " +
                now + "\n" +
                "Server: Apache/2.2.14 (Win32)\n" +
                "Last-Modified: " +
                now + "\n" +
                "Content-Type: text/html\n" +
                "Connection: Closed\n";

        String bodyResponse = """
                <html>
                <head></head>
                <body>
                <div style="border: 2px solid red; width: 200px">
                <h1>STATUS: 404</h1>
                </div>
                <div style="border: 2px solid red; width: 150px; height: 50px">
                <h2>Not Found</h2>
                </div>
                </body></html>""";

        headerResponse += "Content-Length: " + bodyResponse.length() + "\n\n";

        return headerResponse + bodyResponse;
    }

}

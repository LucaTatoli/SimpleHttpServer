import java.util.Date;

public class HttpResponse {

    public static String methodNotSupportedResponse()
    {
        return "METHOD NOT SUPPORTED";
    }

    public static String okResponse()
    {
        Date now = new Date();
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK\n");
        sb.append("Date: ");
        sb.append(now+"\n");
        sb.append("Server: Apache/2.2.14 (Win32)\n");
        sb.append("Last-Modified: ");
        sb.append(now+"\n");
        sb.append("Content-Type: text/html\n");
        sb.append("Connection: Closed\n\n");
        sb.append("<html>\n");
        sb.append("<head></head>\n");
        sb.append("<body>\n");
        sb.append("<div style=\"border: 2px solid red; width: 200px\">\n");
        sb.append("<h1>STATUS: 200</h1>\n");
        sb.append("</div>\n");
        sb.append("<div style=\"border: 2px solid red; width: 50px; height: 50px\">\n");
        sb.append("<h2>OK</h2>\n");
        sb.append("</div>\n");
        sb.append("</body>");
        sb.append("</html>");
        return sb.toString();
    }

}

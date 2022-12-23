public class Main
{
    public static void main(String... args)
    {
        ServerEntryPoint server = new ServerEntryPoint(8080);
        server.listen();
    }
}

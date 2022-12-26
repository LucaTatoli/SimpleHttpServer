import java.io.*;
import java.util.HashMap;
import java.util.Objects;

public class Main
{

    public static HashMap<String, Class<?>> controllers = new HashMap<>();
    public static boolean printDebugFlag = true;

    public static void loadConfiguration() throws IOException {
        InputStream configInputStream = Main.class.getClassLoader().getResourceAsStream("configuration.json");
        if(configInputStream == null)
            throw new IOException();
        BufferedReader br = new BufferedReader(new InputStreamReader(configInputStream));
        StringBuilder config = new StringBuilder();
        try
        {
            while (br.ready())
            {
                config.append(br.readLine()).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        parseConfiguration(config.toString());
    }

    public static void parseConfiguration(String config)
    {
        int index = config.indexOf("controllers");
        String controllerConfig = config.substring(config.indexOf("{", index)+1, config.indexOf("}", index));
        for(String controller : controllerConfig.split(","))
        {
            String[] split = controller.split(":");
            String controllerClass = split[1].strip();
            controllerClass = controllerClass.substring(1, controllerClass.length()-1);
            String controllerName = split[0].strip();
            controllerName = controllerName.substring(1, controllerName.length()-1);
            try
            {
                controllers.put(controllerName, Class.forName(controllerClass));
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }

        }
    }


    public static void main(String... args)
    {
        try
        {
            System.out.println("Loading server configuration");
            loadConfiguration();
        }
        catch (IOException e)
        {
            System.out.println("Configuration file not found!");
            return;
        }
        System.out.println("Server configuration loaded!");
        System.out.println("Starting server");
        ServerEntryPoint server = new ServerEntryPoint(8080);
        System.out.println("Server started!");
        server.listen();


    }
}

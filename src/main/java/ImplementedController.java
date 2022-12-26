import Annotations.GetMapping;
import Annotations.PostMapping;

public class ImplementedController extends RequestController
{

    @PostMapping(uri = "/postController")
    public String postController(String request)
    {
        System.out.println("POST CONTROLLER --- START");
        System.out.println(request);
        System.out.println("POST CONTROLLER --- END");
        return HTTPResponse.okResponse();
    }

    @GetMapping(uri = "/getController")
    public String getController(String request)
    {
        System.out.println("GET CONTROLLER --- START");
        System.out.println(request);
        System.out.println("GET CONTROLLER --- END");
        return HTTPResponse.okResponse();
    }
    
}

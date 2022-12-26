import Annotations.PatchMapping;

public class ActivationController extends RequestController {

    @PatchMapping(uri = "/activate")
    public String activateConfirmation(String request)
    {
        System.out.println("PATCH CONTROLLER --- START");
        System.out.println(request);
        System.out.println("PATCH CONTROLLER --- END");
        return HTTPResponse.okResponse();
    }

}

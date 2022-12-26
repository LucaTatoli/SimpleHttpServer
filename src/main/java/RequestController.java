import Annotations.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

public class RequestController
{
    public String handleRequest(String request)
    {
        String response = HTTPResponse.notFound404();

        String[] header = request.substring(0, request.indexOf("\n")).split(" ");
        String http_method = header[0];
        String uri = header[1];
        Class<? extends Annotation> annotation = ApplicationConstants.HTTP_METHODS_ANNOTATIONS.get(http_method);

        Method[] methods = this.getClass().getDeclaredMethods();

        for (Method method : methods)
        {
            if (matchUri(method, annotation, uri))
            {
                try
                {
                    response = (String) method.invoke(this, request);
                    break;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
        return response;
    }

    public boolean matchUri(Method method, Class<? extends Annotation> annotation, String uri)
    {
        if(!method.isAnnotationPresent(annotation))
            return false;

        if(annotation.getName().equals(GetMapping.class.getName()))
            return method.getAnnotation(GetMapping.class).uri().equals(uri);

        if(annotation.getName().equals(PostMapping.class.getName()))
            return method.getAnnotation(PostMapping.class).uri().equals(uri);

        if(annotation.getName().equals(PatchMapping.class.getName()))
            return method.getAnnotation(PatchMapping.class).uri().equals(uri);

        if(annotation.getName().equals(PutMapping.class.getName()))
            return method.getAnnotation(PutMapping.class).uri().equals(uri);

        if(annotation.getName().equals(DeleteMapping.class.getName()))
            return method.getAnnotation(DeleteMapping.class).uri().equals(uri);

        if(annotation.getName().equals(ConnectMapping.class.getName()))
            return method.getAnnotation(ConnectMapping.class).uri().equals(uri);

        if(annotation.getName().equals(HeadMapping.class.getName()))
            return method.getAnnotation(HeadMapping.class).uri().equals(uri);

        if(annotation.getName().equals(TraceMapping.class.getName()))
            return method.getAnnotation(TraceMapping.class).uri().equals(uri);

        if(annotation.getName().equals(OptionsMapping.class.getName()))
            return method.getAnnotation(OptionsMapping.class).uri().equals(uri);

        return false;
    }

}

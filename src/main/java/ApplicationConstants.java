import Annotations.*;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationConstants
{
    public static String POST = "POST";
    public static String GET = "GET";
    public static String PUT = "PUT";
    public static String PATCH = "PATCH";
    public static String DELETE = "DELETE";
    public static String CONNECT = "CONNECT";
    public static String OPTIONS = "OPTIONS";
    public static String TRACE = "TRACE";
    public static String HEAD = "HEAD";
    public static List<String> HTTP_METHODS = List.of(POST, GET, PUT, PATCH, DELETE, CONNECT, OPTIONS, TRACE, HEAD);
    public static Map<String, Class<? extends Annotation>> HTTP_METHODS_ANNOTATIONS = Map.ofEntries(
            Map.entry(POST, PostMapping.class),
            Map.entry(GET, GetMapping.class),
            Map.entry(PUT, PutMapping.class),
            Map.entry(PATCH, PatchMapping.class),
            Map.entry(DELETE, DeleteMapping.class),
            Map.entry(CONNECT, ConnectMapping.class),
            Map.entry(OPTIONS, OptionsMapping.class),
            Map.entry(TRACE, TraceMapping.class),
            Map.entry(HEAD, HeadMapping.class)
    );

}

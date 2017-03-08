package top.wangjingxin.server;

import top.wangjingxin.server.api.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by 王镜鑫 on 2017/2/20 14:57.
 */
public abstract class Context {
    public static final Map<Integer, ArrayList<byte[]>> contextRequest = new ConcurrentHashMap<>();
    public static final Map<String,Session> sessions = new ConcurrentHashMap<>();
    public static final Map<String,Servlet> servlets = new HashMap<>();
    public static final ExecutorService es = Executors.newCachedThreadPool();
    public static final Servlet defaultServlet = new DefaultServlet();
    public static final String path = "C:\\Users\\17854\\Desktop\\demo";
    public static final String ERROR_404 =
            "<html>\n" +
            "\t<body>\n" +
            "\t\t404 NOT FOUND!\n" +
            "\t</body>\n" +
            "</html>";
    public static final String ERROR_405 = "<html>\n" +
            "\t<body>\n" +
            "\t\t405 METHOD NOT SUPPOET!\n" +
            "\t</body>\n" +
            "</html>";

    public static Map<String, Session> getSessions() {
        return sessions;
    }
}

package top.wangjingxin.server;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 王镜鑫 on 2017/2/20 12:26.
 */
public class Bootstrap {
    public static final Log log = LogFactory.getLog(Bootstrap.class);
    public static void main(String[] args) {
        log.info("开始启动微型应用服务器");
        List<Class<Servlet>> servletList = loadClasses();
        log.info("加载class结束");
        servletList.forEach(servlet->{
            String url = HandlerAnnotation(servlet);
            if(url!=null)
                try {
                    Context.servlets.put(url, servlet.newInstance());
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
        });
        int port = 80;
        log.info("开始建立连接");
        new Thread(new NioServerHandler(port)).start();
    }

    private static List<Class<Servlet>> loadClasses() {
        List<Class<Servlet>> servletClassList = new ArrayList<>();
        try {
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL("file://"+Context.path+"/WEB-INF/classes/")});
            List<File> fileList = new ArrayList<>();
            scanFiles(fileList,new File(Context.path+"/WEB-INF/classes"));
            fileList.forEach(file -> {
                try {
                    servletClassList.add((Class<Servlet>) urlClassLoader.
                            loadClass(file.getPath()
                                    .substring((Context.path+"/WEB-INF/classes/").length(),
                            file.getPath().length()-6)
                                    .replaceAll("\\\\",".")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }finally {
            return servletClassList;
        }
    }

    private static void scanFiles(List<File> fileList, File file) {
        if(file.isDirectory()){
            File[] files = file.listFiles();
            Arrays.stream(files).forEach(f->{
                if(f.isFile())
                    fileList.add(f);
                else scanFiles(fileList,f);
            });
        }
    }

    private static String HandlerAnnotation(Class servlet) {
        WebServlet webServlet = (WebServlet) servlet.getAnnotation(WebServlet.class);
        return webServlet.urlPattern();
    }
}

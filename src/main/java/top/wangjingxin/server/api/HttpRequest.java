package top.wangjingxin.server.api;


import lombok.Getter;
import top.wangjingxin.server.Context;
import top.wangjingxin.server.MethodEnum;

import java.util.*;

/**
 * Created by 王镜鑫 on 2017/2/20 14:39.
 */
public class HttpRequest {
    @Getter
    private MethodEnum method;
    @Getter
    private String url;
    @Getter
    private String protocol;
    @Getter
    private String host;
    @Getter
    private Cookie[] cookies;
    private HttpResponse response;
    private Session session;
    private Map<String,String> parameter = new HashMap<>();
    public HttpRequest(HttpResponse response) {
        this.response = response;
    }

    public void init(String head, String body) {
        String[] heads = head.split("\r\n");
        String[] first = heads[0].split(" ");
        if("POST".equals(first[0]))
            this.method = MethodEnum.POST;
        if("GET".equals(first[0]))
            this.method = MethodEnum.GET;
        if(first[1].contains("?"))
            this.url = first[1].substring(0,first[1].indexOf("?"));
        else
            this.url = first[1];
        if("\\".equals(this.url))
            this.url = "index.html";
        this.protocol = first[2];
        heads[0] = null;
        Properties properties = new Properties();
        Arrays.stream(heads).forEach(s->{
            if(s!=null)
                properties.setProperty(s.split(":")[0], s.split(":")[1]);
        });
        this.host = properties.getProperty("Host");
        this.session = setCookies(properties.getProperty("Cookie"));
        resolve(url,body);
    }

    public Session getSession() {
        if(this.session!=null)
            return this.session;
        Session session = new Session();
        String uuid = UUID.randomUUID().toString();
        this.response.setSessionId("JSESSIONID="+uuid);
        Context.sessions.put(uuid,session);
        return session;
    }
    public String getParameter(String key){
        return parameter.get(key);
    }
    private void resolve(String url, String body) {
        String[] ps = null;
        if(url.contains("?")){
            ps = url.substring(url.indexOf("?"),url.length()).split("&");
            Arrays.stream(ps).forEach(s->this.parameter.put(s.split("=")[0],s.split("=")[1]));
        }
        if(body!=null){
            ps = body.split("&");
            Arrays.stream(ps).forEach(s->this.parameter.put(s.split("=")[0],s.split("=")[1]));
        }
    }

    private Session setCookies(String s){
        if(s == null)
            return null;
        String[] ss = s.split("; ");
        Cookie[] cookies = new Cookie[ss.length];
        Session session = null;
        for(int i=0;i<ss.length;i++){
            String key = ss[i].split("=")[0];
            String value = ss[i].split("=")[1];
            cookies[i] = new Cookie(key,value);
            if("JSESSIONID".equals(key))
                session = Context.getSessions().get(value);
        }
        this.cookies = cookies;
        return session;
    }
}

package top.wangjingxin.server;

import top.wangjingxin.server.api.HttpRequest;
import top.wangjingxin.server.api.HttpResponse;
import top.wangjingxin.server.exception.ServletException;

import java.io.*;

/**
 * Created by 王镜鑫 on 2017/2/21 12:19.
 */
public class DefaultServlet extends Servlet {
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws ServletException {
        String fullPath = Context.path+request.getUrl();
        File file = new File(fullPath);
        StringBuffer sb = new StringBuffer("");
        if(file.exists()){
            try {
                String s = null;
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                while ((s=br.readLine())!=null){
                    sb.append(s);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            response.write(sb.toString());
            String type = request.getUrl().substring(request.getUrl().indexOf("."),request.getUrl().length());
            if(".html".equals(type))
                response.writeOver(ContentTypeEnum.HTML,"200 OK");
            else if(".css".equals(type))
                response.writeOver(ContentTypeEnum.CSS,"200 OK");
            else if(".js".equals(type))
                response.writeOver(ContentTypeEnum.APP_JS,"200 OK");
        }else
            throw new ServletException(404,"NOT FOUND");
    }
}

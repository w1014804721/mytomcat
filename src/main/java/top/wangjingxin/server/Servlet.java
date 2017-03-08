package top.wangjingxin.server;



import top.wangjingxin.server.api.ErrorMessage;
import top.wangjingxin.server.api.HttpRequest;
import top.wangjingxin.server.api.HttpResponse;
import top.wangjingxin.server.exception.ServletException;

import java.io.IOException;

/**
 * Created by 王镜鑫 on 2017/2/20 18:48.
 */
public class Servlet {
    protected void service(HttpRequest request, HttpResponse response) throws ServletException {
        if(request.getMethod().equals(MethodEnum.GET))
            doGet(request,response);
        if(request.getMethod().equals(MethodEnum.POST))
            doPost(request,response);
    }

    protected void doGet(HttpRequest request, HttpResponse response) throws ServletException {
        response.write(Context.ERROR_405);
        throw new ServletException(405,"Method Not Allowed");
    }

    protected void doPost(HttpRequest request, HttpResponse response) throws ServletException {
        response.write(Context.ERROR_405);
        throw new ServletException(405,"Method Not Allowed");
    }
}

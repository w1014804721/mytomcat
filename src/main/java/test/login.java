package test;

import top.wangjingxin.server.Servlet;
import top.wangjingxin.server.WebServlet;
import top.wangjingxin.server.api.HttpRequest;
import top.wangjingxin.server.api.HttpResponse;
import top.wangjingxin.server.exception.ServletException;

/**
 * Created by 王镜鑫 on 2017/2/21 14:45.
 */
@WebServlet(urlPattern = "/login")
public class login extends Servlet{
    @Override
    protected void doGet(HttpRequest request, HttpResponse response) throws ServletException {
        request.getSession().setAttribute("user_name","wangjingxin");
        response.write("ok!");
    }
}

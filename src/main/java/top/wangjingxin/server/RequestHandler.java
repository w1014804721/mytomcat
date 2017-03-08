package top.wangjingxin.server;


import top.wangjingxin.server.api.ErrorMessage;
import top.wangjingxin.server.api.HttpRequest;
import top.wangjingxin.server.api.HttpResponse;
import top.wangjingxin.server.exception.ServletException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * Created by 王镜鑫 on 2017/2/20 15:05.
 */
public abstract class RequestHandler {

    public static void process(List<byte[]> completeRequest, SocketChannel sc) {
        if(completeRequest==null||completeRequest.size()==0)
            return;
        final byte[][] request = {completeRequest.get(0)};
        completeRequest.remove(0);
        completeRequest.forEach(bb->{
            byte[] merge = new byte[bb.length+ request[0].length];
            System.arraycopy(request[0],0,merge,0, request[0].length);
            System.arraycopy(bb,0,merge, request[0].length,bb.length);
            request[0] = merge;
        });
        try {
            String s = new String(request[0],"utf-8");
            HttpResponse httpResponse = resolveResponse(sc);
            HttpRequest httpRequest = resolveRequest(s,httpResponse);
            if(httpRequest.getUrl().matches("(.*\\.(html|htm|gif|jpg|jpeg|bmp|png|ico|txt|js|css|woff)$)")){
                try {
                    Context.defaultServlet.service(httpRequest,httpResponse);
                }catch (ServletException e){
                    httpResponse.write(Context.ERROR_404);
                    httpResponse.writeOver(ContentTypeEnum.HTML,e.get());
                    e.printStackTrace();
                    return;
                }
            }
            Servlet servlet = Context.servlets.get(httpRequest.getUrl());
            if(servlet == null){
                httpResponse.write(Context.ERROR_404);
                httpResponse.writeOver(ContentTypeEnum.HTML,"404 NOT FOUND");
            }
            else {
                try {
                    servlet.service(httpRequest,httpResponse);
                } catch (ServletException e) {
                    httpResponse.writeOver(ContentTypeEnum.HTML,e.get());
                    e.printStackTrace();
                    return;
                }
                httpResponse.writeOver(ContentTypeEnum.JSON,"200 OK");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static HttpRequest resolveRequest(String s,HttpResponse response) {
        String[] part = s.split("\r\n\r\n");
        String head = part[0];
        String body = null;
        if (part.length>1)
            body = part[1];
        HttpRequest httpRequest = new HttpRequest(response);
        httpRequest.init(head,body);
        return httpRequest;
    }

    private static HttpResponse resolveResponse(SocketChannel sc) {
        HttpResponse response = new HttpResponse(sc);
        return response;
    }
}

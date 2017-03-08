package top.wangjingxin.server.api;



import lombok.Setter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * Created by 王镜鑫 on 2017/2/20 14:39.
 */
public class HttpResponse {
    private SocketChannel sc = null;
    private StringBuffer setCookies = new StringBuffer();
    @Setter
    private String encode = "utf-8";
    private String userBody = "";
    public HttpResponse(SocketChannel sc){
        this.sc = sc;
    }
    public void writeOver(String type,String code) {
        StringBuffer sb = new StringBuffer("HTTP/1.1 #\n".replaceAll("#",code));
        sb.append("Date:"+new Date()+"\n");
        sb.append(("Content-Type: ?;charset="+encode).replace("?",type)+"\n");
        sb.append("Set-Cookie:"+setCookies.toString()+"\n");
        sb.append("Content-Length:"+userBody.getBytes().length+"\r\n\r\n");
        sb.append(userBody);
        System.out.println(sb.toString());
        byte[] response = sb.toString().getBytes();
        ByteBuffer responseBuffer = ByteBuffer.allocate(response.length);
        responseBuffer.put(response);
        responseBuffer.flip();
        try {
            sc.write(responseBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {}
    }
    public void setSessionId(String s){
        setCookies.append(s).append(";");
    }
    public void write(String userBody) {
        this.userBody = userBody;
    }
}

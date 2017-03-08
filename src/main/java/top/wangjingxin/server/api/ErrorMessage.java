package top.wangjingxin.server.api;

import java.util.Date;

/**
 * Created by 王镜鑫 on 2017/2/20 18:55.
 */
public class ErrorMessage {
    private int code;
    private String message;

    public ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public byte[] getBytes() {
        StringBuffer sb = new StringBuffer("HTTP/1.1 "+code+" "+message+"\n");
        sb.append("Date:"+new Date()+"\n");
        sb.append("Content-Length:"+0+"\n\r\n\r\n");
        return sb.toString().getBytes();
    }
}

package top.wangjingxin.server.exception;


/**
 * Created by 王镜鑫 on 2017/2/21 14:04.
 */
public class ServletException extends Exception {
    private int code;
    private String message;
    public ServletException(int code,String message){
        this.code = code;
        this.message = message;
    }
    public String get(){
        return code+" "+message;
    }
}

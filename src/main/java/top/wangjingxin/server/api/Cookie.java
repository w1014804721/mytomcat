package top.wangjingxin.server.api;


import lombok.Getter;

/**
 * Created by 王镜鑫 on 2017/2/20 17:57.
 */
public class Cookie {
    @Getter
    private String key;
    @Getter
    private String value;
    public Cookie(String key,String value){
        this.key = key;
        this.value = value;
    }
}

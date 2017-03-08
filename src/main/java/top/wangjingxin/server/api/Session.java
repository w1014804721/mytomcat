package top.wangjingxin.server.api;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 王镜鑫 on 2017/2/20 18:05.
 */
public class Session {
    private Map<Object,Object> values = new HashMap<>();
    public void setAttribute(Object key,Object value){
        this.values.put(key,value);
    }
    public Object getAttribute(Object key){
        return values.get(key);
    }
}

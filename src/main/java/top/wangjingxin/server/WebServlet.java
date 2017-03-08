package top.wangjingxin.server;

import java.lang.annotation.*;

/**
 * Created by 王镜鑫 on 2017/2/20 19:20.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebServlet {
    String urlPattern() default "";
}

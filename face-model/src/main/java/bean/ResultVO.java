package bean;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultVO<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    //成功就会返回对象结果 ， 前端如果取成功方向的数据，要取的是 data
    public static <T> ResultVO<T> success(T object) {
        ResultVO<T> r = new ResultVO<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    //失败就返回错误的信息， 前端要取失败方向的数据，要取的是 msg
    public static <T> ResultVO<T> error(String msg) {
        ResultVO r = new ResultVO();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public ResultVO<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}

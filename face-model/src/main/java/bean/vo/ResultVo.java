package bean.vo;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ResultVo<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    //成功就会返回对象结果 ， 前端如果取成功方向的数据，要取的是 data
    public static <T> ResultVo<T> success(T object) {
        ResultVo<T> r = new ResultVo<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    //失败就返回错误的信息， 前端要取失败方向的数据，要取的是 msg
    public static <T> ResultVo<T> error(String msg) {
        ResultVo r = new ResultVo();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public ResultVo<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}

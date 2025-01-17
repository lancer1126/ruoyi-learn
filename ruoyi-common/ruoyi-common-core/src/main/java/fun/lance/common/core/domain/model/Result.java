package fun.lance.common.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 成功
     */
    public static final int SUCCESS = 200;
    /**
     * 失败
     */
    public static final int FAIL = 500;

    private int code;
    private String msg;
    private T data;

    public static <T> Result<T> success() {
        return withResult(null, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(T data) {
        return withResult(data, SUCCESS, "操作成功");
    }

    public static <T> Result<T> success(String msg) {
        return withResult(null, SUCCESS, msg);
    }

    public static <T> Result<T> success(String msg, T data) {
        return withResult(data, SUCCESS, msg);
    }

    public static <T> Result<T> fail() {
        return withResult(null, FAIL, "操作失败");
    }

    public static <T> Result<T> fail(String msg) {
        return withResult(null, FAIL, msg);
    }

    public static <T> Result<T> fail(T data) {
        return withResult(data, FAIL, "操作失败");
    }

    public static <T> Result<T> fail(String msg, T data) {
        return withResult(data, FAIL, msg);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return withResult(null, code, msg);
    }

    private static <T> Result<T> withResult(T data, int code, String msg) {
        Result<T> r = new Result<>();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    }
}

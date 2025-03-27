package fun.lance.common.core.utils;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Component
@Getter
public class OkHttpUtils {

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final MediaType FORM = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private final OkHttpClient okHttpClient;

    /**
     * 构造方法，注入OkHttpClient实例
     */
    public OkHttpUtils(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * 同步 GET 请求
     *
     * @param url 请求URL
     * @return 响应字符串
     */
    public String get(String url) {
        return get(url, null);
    }

    /**
     * 同步 GET 请求，可添加请求头
     *
     * @param url     请求URL
     * @param headers 请求头
     * @return 响应字符串
     */
    public String get(String url, Map<String, String> headers) {
        try {
            Request.Builder builder = new Request.Builder().url(url);
            addHeaders(builder, headers);
            Response response = okHttpClient.newCall(builder.build()).execute();
            return processResponse(response);
        } catch (Exception e) {
            log.error("GET请求异常: {}", url, e);
            return null;
        }
    }

    /**
     * 同步 POST 请求，发送JSON数据
     *
     * @param url  请求URL
     * @param json JSON字符串
     * @return 响应字符串
     */
    public String post(String url, String json) {
        return post(url, json, null);
    }

    /**
     * 同步 POST 请求，发送JSON数据，可添加请求头
     *
     * @param url     请求URL
     * @param json    JSON字符串
     * @param headers 请求头
     * @return 响应字符串
     */
    public String post(String url, String json, Map<String, String> headers) {
        try {
            RequestBody body = RequestBody.create(json, JSON);
            Request.Builder builder = new Request.Builder().url(url).post(body);
            addHeaders(builder, headers);
            Response response = okHttpClient.newCall(builder.build()).execute();
            return processResponse(response);
        } catch (Exception e) {
            log.error("POST JSON请求异常: {}", url, e);
            return null;
        }
    }

    /**
     * 同步 POST 请求，发送表单数据
     *
     * @param url    请求URL
     * @param params 表单参数
     * @return 响应字符串
     */
    public String post(String url, Map<String, String> params) {
        return post(url, params, null);
    }

    /**
     * 同步 POST 请求，发送表单数据，可添加请求头
     *
     * @param url     请求URL
     * @param params  表单参数
     * @param headers 请求头
     * @return 响应字符串
     */
    public String post(String url, Map<String, String> params, Map<String, String> headers) {
        try {
            FormBody.Builder formBuilder = new FormBody.Builder();
            if (params != null) {
                params.forEach(formBuilder::add);
            }
            Request.Builder builder = new Request.Builder().url(url).post(formBuilder.build());
            addHeaders(builder, headers);
            Response response = okHttpClient.newCall(builder.build()).execute();
            return processResponse(response);
        } catch (Exception e) {
            log.error("POST表单请求异常: {}", url, e);
            return null;
        }
    }

    /**
     * 异步 GET 请求
     *
     * @param url      请求URL
     * @param callback 回调接口
     */
    public void getAsync(String url, Callback callback) {
        getAsync(url, null, callback);
    }

    /**
     * 异步 GET 请求，可添加请求头
     *
     * @param url      请求URL
     * @param headers  请求头
     * @param callback 回调接口
     */
    public void getAsync(String url, Map<String, String> headers, Callback callback) {
        Request.Builder builder = new Request.Builder().url(url);
        addHeaders(builder, headers);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    /**
     * 异步 POST 请求，发送JSON数据
     *
     * @param url      请求URL
     * @param json     JSON字符串
     * @param callback 回调接口
     */
    public void postAsync(String url, String json, Callback callback) {
        postAsync(url, json, null, callback);
    }

    /**
     * 异步 POST 请求，发送JSON数据，可添加请求头
     *
     * @param url      请求URL
     * @param json     JSON字符串
     * @param headers  请求头
     * @param callback 回调接口
     */
    public void postAsync(String url, String json, Map<String, String> headers, Callback callback) {
        RequestBody body = RequestBody.create(json, JSON);
        Request.Builder builder = new Request.Builder().url(url).post(body);
        addHeaders(builder, headers);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    /**
     * 异步 POST 请求，发送表单数据
     *
     * @param url      请求URL
     * @param params   表单参数
     * @param callback 回调接口
     */
    public void postAsync(String url, Map<String, String> params, Callback callback) {
        postAsync(url, params, null, callback);
    }

    /**
     * 异步 POST 请求，发送表单数据，可添加请求头
     *
     * @param url      请求URL
     * @param params   表单参数
     * @param headers  请求头
     * @param callback 回调接口
     */
    public void postAsync(String url, Map<String, String> params, Map<String, String> headers, Callback callback) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        if (params != null) {
            params.forEach(formBuilder::add);
        }
        Request.Builder builder = new Request.Builder().url(url).post(formBuilder.build());
        addHeaders(builder, headers);
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    /**
     * 添加请求头
     *
     * @param builder 请求构建器
     * @param headers 请求头Map
     */
    private void addHeaders(Request.Builder builder, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach(builder::addHeader);
        }
    }

    /**
     * 处理响应结果
     *
     * @param response OkHttp响应对象
     * @return 响应字符串
     * @throws IOException IO异常
     */
    private String processResponse(Response response) throws IOException {
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                String result = body.string();
                if (StrUtil.isNotBlank(result)) {
                    return result;
                }
            }
        }
        log.error("HTTP请求失败: {} {}", response.code(), response.message());
        return null;
    }
}

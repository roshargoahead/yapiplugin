package com.inheritech.it.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inheritech.it.entity.UserVO;
import com.inheritech.it.enums.LoginEnum;
import com.inheritech.it.factory.ParseStringFactory;
import com.inheritech.it.ui.LoginWindow;
import com.intellij.openapi.ui.Messages;
import com.intellij.util.ExceptionUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @desc:
 * @author: ShuQiaoShuang
 * @date: 2021-04-23 14:28:53
 */
public class HttpUtil {
    private HttpUtil() {
    }

    /**
     * 用户设备标识
     */
    private static final String USER_AGENT = "EasyCode";

    /**
     * 内容类型标记
     */
    private static final String CONTENT_TYPE = "application/json;charset=UTF-8";

    /**
     * 服务器地址
     */
    private static final String LOGIN_URL = "https://api.inheritech.top/api/user/login";

    /**
     * http客户端
     */
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    /**
     * 请求超时时间设置(10秒)
     */
    private static final int TIMEOUT = 10 * 1000;

    /**
     * 状态码
     */
    private static final String STATE_CODE = "code";

    /**
     * get请求
     *
     * @param url 请求地址
     * @return 返回请求结果
     */
    public static String doGet(String url, Map<String, Object> param) {
        String paramUrl = getParamUrl(url, param);
        HttpGet httpGet = new HttpGet(paramUrl);
        httpGet.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        httpGet.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        String oldCookie = UserVO.getInstance().getCookie();
        if (!StringUtils.isEmpty(oldCookie)) {
            httpGet.addHeader("Cookie", oldCookie);
        }
        httpGet.setConfig(getDefaultConfig());
        String body = handlerRequest(httpGet);
        Map<String, Object> result = ParseStringFactory.parse(body, Map.class);
        if (result.containsKey("errcode") && !"0".equals(String.valueOf(result.get("errcode")))) {
            String cookie = HttpUtil.login();
            // 取消登录/登录失败
            if (StringUtils.isEmpty(cookie)) {
                return null;
            }
            httpGet.addHeader("Cookie", cookie);
            body = handlerRequest(httpGet);
        }
        return body;
    }

    /**
     * get请求参数
     *
     * @param url 请求地址
     * @return 返回请求结果
     */
    private static String getParamUrl(String url, Map<String, Object> param) {
        StringBuilder sb = new StringBuilder();
        sb.append(url).append("?");
        for (Map.Entry<String, Object> entry : param.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        return sb.substring(0, sb.length() - 1);
    }

    /**
     * post json请求
     *
     * @param url   地址
     * @param param 参数
     * @return 请求返回结果
     */
    public static String postJson(String url, Map<String, Object> param) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        httpPost.setConfig(getDefaultConfig());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!MapUtils.isEmpty(param)) {
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(param), "utf-8"));
            }
            return handlerRequest(httpPost);
        } catch (JsonProcessingException e) {
            Messages.showWarningDialog("JSON解析出错！", "Title Info");
            ExceptionUtil.rethrow(e);
        }
        return null;
    }

    /**
     * post json请求
     *
     * @param url   地址
     * @param param 参数
     * @return 请求返回结果
     */
    public static String postLoginJson(String url, Map<String, Object> param) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader(HttpHeaders.USER_AGENT, USER_AGENT);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, CONTENT_TYPE);
        httpPost.setConfig(getDefaultConfig());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (!MapUtils.isEmpty(param)) {
                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(param), "utf-8"));
            }
            return handleLoginHeader(httpPost);
        } catch (JsonProcessingException e) {
            Messages.showWarningDialog("JSON解析出错！", "Title Info");
            ExceptionUtil.rethrow(e);
        }
        return null;
    }

    private static RequestConfig getDefaultConfig() {
        return RequestConfig.custom().setSocketTimeout(TIMEOUT).setConnectTimeout(TIMEOUT).build();
    }

    /**
     * 统一处理请求
     *
     * @param request 请求对象
     * @return 响应字符串
     */
    private static String handlerRequest(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(request);
            String body = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Messages.showWarningDialog("连接到服务器错误！", "Title Info");
                return null;
            }
            HttpClientUtils.closeQuietly(response);
            // 解析JSON数据
            return body;
        } catch (IOException e) {
            Messages.showWarningDialog("无法连接到服务器！", "Title Info");
            ExceptionUtil.rethrow(e);
        }
        return null;
    }

    /**
     * 统一处理请求
     *
     * @param request 请求对象
     * @return 响应字符串
     */
    private static String handleLoginHeader(HttpUriRequest request) {
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(request);
            String body = EntityUtils.toString(response.getEntity());
            if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                Messages.showWarningDialog("连接到服务器错误！", "Title Info");
                return null;
            }
            StringBuilder stringBuilder = new StringBuilder();
            Header[] headers = response.getHeaders("Set-Cookie");
            for (Header header : headers) {
                stringBuilder.append(header.getValue()).append(";");
            }
            if (stringBuilder.length() > 0) {
                UserVO.getInstance().setCookie(stringBuilder.substring(0, stringBuilder.length() - 1));
            }
            HttpClientUtils.closeQuietly(response);
            // 解析JSON数据
            return body;
        } catch (IOException e) {
            Messages.showWarningDialog("无法连接到服务器！", "Title Info");
            ExceptionUtil.rethrow(e);
        }
        return null;
    }

    public static String login() {
        UserVO instance = UserVO.getInstance();
        if (!StringUtils.isEmpty(instance.getUserAccount()) && !StringUtils.isEmpty(instance.getPassword())) {
            if (doLogin(instance.getUserAccount(), instance.getPassword())) {
                return UserVO.getInstance().getCookie();
            }
        }
        LoginWindow loginWindow = new LoginWindow();
        loginWindow.pack();
        loginWindow.setVisible(true);
        // 取消登录
        if ("0".equals(loginWindow.getIsConfirm())) {
            return null;
        }
        // 登录成功
        return UserVO.getInstance().getCookie();
    }

    public static boolean doLogin(String userNameText, String passwordText) {
        HashMap<String, Object> loginMap = new HashMap<>();
        loginMap.put("email", userNameText);
        loginMap.put("password", passwordText);
        String body = postLoginJson(LOGIN_URL, loginMap);
        Map<String, Object> result = ParseStringFactory.parse(body, Map.class);
        int errcode = Integer.parseInt(result.get("errcode").toString());
        if (!Integer.valueOf(0).equals(errcode)) {
            LoginEnum login = LoginEnum.getByCode(errcode);
            if (Objects.isNull(login)) {
                throw new RuntimeException("异常码不存在");
            }
            // 登录失败
            Messages.showInfoMessage(login.getDesc(), "Title Info");
            return false;
        }
        setUserInfo(result);
        return true;
    }

    private static void setUserInfo(Map<String, Object> result) {
        Object data = result.get("data");
        if (data instanceof Map) {
            Map<String, Object> userInfo = (Map<String, Object>) data;
            UserVO instance = UserVO.getInstance();
            instance.setUsername(userInfo.get("username").toString());
            instance.setRole(userInfo.get("role").toString());
            instance.setUid(userInfo.get("uid").toString());
            instance.setEmail(userInfo.get("email").toString());
            instance.setType(userInfo.get("type").toString());
            instance.setStudy(Boolean.parseBoolean(userInfo.get("study").toString()));
        }
    }
}
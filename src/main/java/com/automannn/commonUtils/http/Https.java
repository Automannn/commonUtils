package com.automannn.commonUtils.http;

import com.automannn.commonUtils.util.Strings;
import com.github.kevinsawicki.http.HttpRequest;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author automannn@163.com
 * @time 2019/4/29 10:36
 */
public class Https {
    private HttpsURLConnection connection;

    private int connectTimeout = 1000 * 5;

    private int readTimeout = 1000 * 5;

    private String contentType = "";

    private String acceptCharset = "UTF-8";

    private String acceptType = "text/plain";

    private String connectType = "close";

    private String body = "";

    private String bodyCharset = "UTF-8";

    private boolean encode = true;

    private boolean gzip = false;

    private Https(){}

    public Https connectTimeout(int secs){
        connection.setConnectTimeout(secs * 1000);
        return this;
    }

    public Https readTimeout(int secs){
        readTimeout = secs * 1000;
        return this;
    }

    public Https contentType(String contentType){
        this.contentType = contentType;
        return this;
    }

    public Https acceptType(String acceptType){
        this.acceptType = acceptType;
        return this;
    }

    public Https acceptCharset(String acceptCharset){
        this.acceptCharset = acceptCharset;
        return this;
    }

    public Https connectType(String connectType){
        this.connectType = connectType;
        return this;
    }

    public Https useCache(boolean useCache){
        connection.setUseCaches(useCache);
        return this;
    }

    public Https body(String body){
        this.body = body;
        return this;
    }

    public Https bodyCharset(String charset){
        this.bodyCharset = charset;
        return this;
    }

    public Https ssLSocketFactory(SSLSocketFactory factory){
        connection.setSSLSocketFactory(factory);
        return this;
    }

    public Https header(final String name, final String value) {
        connection.setRequestProperty(name, value);
        return this;
    }

    public String request(){
        prepareRequest();
        return doRequest();
    }

    private void prepareRequest() {

        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setConnectTimeout(connectTimeout);
        connection.setReadTimeout(readTimeout);
        header("Accept-Charset", acceptCharset);
        header("Connection", connectType);

        if (gzip){
            header("Accept-Encoding", "gzip, deflate");
        }
        if (!Strings.isNullOrEmpty(contentType)){
            header("Content-Type", connectType);
        }
        if (!Strings.isNullOrEmpty(acceptType)){
            header("Accept", acceptType);
        }

        if (!Strings.isNullOrEmpty(body)){
            header("Content-Length", String.valueOf(body.length()));
        }
    }

    private String doRequest() {
        if (!Strings.isNullOrEmpty(body)){
            try (OutputStream out = connection.getOutputStream()){
                out.write(body.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        int respCode;
        try {
            respCode = connection.getResponseCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (InputStream in = respCode == HttpURLConnection.HTTP_OK ?
                connection.getInputStream() : connection.getErrorStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
            return content.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Https get(String url){
        return get(url, true);
    }

    public static Https get(String url, Boolean encode){
        Https https = new Https();
        https.connection = createConnection(url, "GET", encode);
        return https;
    }

    public static Https post(String url){
        return post(url, true);
    }

    public static Https post(String url, Boolean encode){
        Https https = new Https();
        https.connection = createConnection(url, "POST", encode);
        return https;
    }

    private static HttpsURLConnection createConnection(String url, String method, Boolean encode) {
        try {
            URL u = new URL(encode ? HttpRequest.encode(url) : url);
            HttpsURLConnection conn = (HttpsURLConnection) u.openConnection();
            conn.setRequestMethod(method);
            return conn;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

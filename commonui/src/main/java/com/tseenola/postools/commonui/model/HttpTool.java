package com.tseenola.postools.commonui.model;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: ZhengJuE
 * @CreateDate: 2020/8/31 20:19
 * @Description:
 * @UpdateUser: ZhengJuE
 * @UpdateDate: 2020/8/31 20:19
 * @UpdateRemark:
 * @Version: 1.0
 */
public class HttpTool {

    public HttpTool() {
    }

    private static void init(HttpURLConnection conn, int connectTimeout, int readTimeout, boolean useCaches, String method, Map<String, String> headers) throws Exception {
        conn.setConnectTimeout(connectTimeout);
        conn.setReadTimeout(readTimeout);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(useCaches);
        conn.setRequestMethod(method);
        if (headers != null) {
            Iterator iterator = headers.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, String> entry = (Map.Entry)iterator.next();
                conn.setRequestProperty((String)entry.getKey(), (String)entry.getValue());
            }
        }

    }

    public static byte[] doPost(String urlStr, Map<String, String> headers, byte[] params, int start, int length, int connectTimeout, int readTimeout, boolean useCaches) throws Exception {
        HttpURLConnection conn = null;
        ByteArrayOutputStream output = null;
        Object var10 = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            init(conn, connectTimeout, readTimeout, useCaches, "POST", headers);
            if (params == null) {
                params = new byte[start + length];
            }

            conn.setRequestProperty("Content-Length", String.valueOf(length));
            conn.connect();
            conn.getOutputStream().write(params, start, length);
            conn.getOutputStream().flush();
            if (conn.getResponseCode() != 200) {
                throw new IOException("无法连接服务器，响应码：" + conn.getResponseCode());
            } else {
                InputStream input = conn.getInputStream();
                output = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                boolean var14 = false;

                int count;
                while((count = input.read(buffer)) > 0) {
                    output.write(buffer, 0, count);
                }

                output.flush();
                byte[] result = output.toByteArray();
                return result;
            }
        } catch (Exception var18) {
            throw var18;
        } finally {
            if (output != null) {
                output.close();
            }

            if (conn != null) {
                conn.disconnect();
            }

        }
    }
}

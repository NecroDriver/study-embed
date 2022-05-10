package com.xin.web.utils.http;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * http请求工具
 *
 * @author creator mafh 2019/12/25 18:01
 * @author updater
 * @version 1.0.0
 */
public class HttpUtils {


    public static String sendPostWithFile(String url) {
        DataOutputStream out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            // 建立url连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 发送post请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 定义数据分割线
            String BOUNDARY = "--------------------asdf82r8sfawer";
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.108 Safari/537.36");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
            conn.connect();

            out = new DataOutputStream(conn.getOutputStream());
            // 定义最后数据分隔线
            byte[] end_data = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
            // 添加参数luid
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition:form-data;name=\"luid\"");
            sb.append("\r\n");
            sb.append("\r\n");
            sb.append("123");
            sb.append("\r\n");
            out.write(sb.toString().getBytes());
            // 添加参数file
            File file = new File("C:\\Users\\mafh\\Desktop\\1.png");
            StringBuilder fileSb = new StringBuilder();
            fileSb.append("--");
            fileSb.append(BOUNDARY);
            fileSb.append("\r\n");
            fileSb.append("Content-Disposition:form-data;name=\"file\";filename=\"" + file.getName() + "\"");
            fileSb.append("\r\n");
            fileSb.append("Content-Type:application/octet-stream");
            fileSb.append("\r\n");
            fileSb.append("\r\n");
            out.write(fileSb.toString().getBytes());

            DataInputStream fileIn = new DataInputStream(new FileInputStream(file));
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = fileIn.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 多个文件时，二个文件之间加入这个
            out.write("\r\n".getBytes());
            fileIn.close();
            out.write(end_data);

            // flush输出文件流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null){
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送post请求异常" + e);
        } finally {
            try {
                if (out != null){
                    out.close();
                }
                if (in != null){
                    in.close();
                }
            }catch (Exception e){

            }
        }
        return result;
    }
}

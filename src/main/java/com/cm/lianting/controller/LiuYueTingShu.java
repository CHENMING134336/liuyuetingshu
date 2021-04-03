package com.cm.lianting.controller;
import com.fasterxml.jackson.core.JsonParser;
import net.sf.json.JSONObject;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.net.www.http.HttpClient;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * @Author CM
 * @Date 2021/4/3 19:34
 * @Description
 */
@RestController
@CrossOrigin
public class LiuYueTingShu {

    @Value("${TestHttpClient.doGetHttp.lytsurl}")
    public String bookurl;

    @Value("${TestHttpClient.doGetHttp.lytssize}")
    public int booksize;

    @GetMapping(value = "/getlyts")
    public void getbookurl(HttpServletResponse httpresponse) throws IOException, InterruptedException {

        LinkedHashMap<String,String> songurlmap = new LinkedHashMap<>();
        for (int i = 1; i <= booksize; i++) {
            int sleep = new Random().nextInt(2);
            Thread.sleep(sleep);
            String res = "";
            HttpURLConnection conn = null;
            try {
                URL url = new URL(bookurl+i);
                conn = (HttpURLConnection)url.openConnection();
                conn.setConnectTimeout(5000);
                //conn.setReadTimeout(30000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
                //OutputStream out = new DataOutputStream(conn.getOutputStream());

                StringBuffer strBuf = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    strBuf.append(line).append("\n");
                }
                res = strBuf.toString();
                JSONObject json_test = JSONObject.fromObject(res);
                JSONObject json = JSONObject.fromObject(json_test.get("data"));
                System.out.println(("第" + i + "集" + (String) json.get("videoUrl")));
                songurlmap.put("第"+i+"集",(String)json.get("videoUrl"));

                    reader.close();
                    reader = null;


            }catch(Exception e){
                System.out.println("GET链接问题,查看原因!"+e.getMessage());
                throw new RuntimeException(e.getMessage());
            } finally {
                if (conn != null) {
                    conn.disconnect();
                    conn = null;
                }
            }
        }
        geHTML(httpresponse,songurlmap);

    }

    private HttpServletResponse geHTML(HttpServletResponse httpresponse, LinkedHashMap<String,String> songurlmap) throws IOException {

        /**
         *  前端渲染或者生成html,结合IDM下载
         */
        String libody = "仅用于学习用途，切勿用作违法行为！-------------- Design By CM大都督（快乐小草）</br>";
        for (Map.Entry<String,String> entry : songurlmap.entrySet()) {
            String sn = entry.getKey();
            String su = entry.getValue();
            libody = libody + "</br><li style = \"font-size:14px\">\n" + sn+"</br>"+
                    "\t  <a style = \"text-decoration:none;color:#01A9DB\" title=\n" + su+
                    "\t  href=" + su+
                    "\t  >" + su +
                    "</a></li>";
        }
        String result = "<div id=\"songs\" class=\"list1\">\n" +
                "\t<ol>                           \n" +
                libody+
                "\t</ol>\n" +
                "</div>";

        httpresponse.setContentType("text/html;charset=UTF-8");
        PrintWriter out = httpresponse.getWriter();
        out.write(result);
        httpresponse.setContentType("audio/mpeg");
        return httpresponse;
    }
}

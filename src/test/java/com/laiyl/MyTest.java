package com.laiyl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.laiyl.http.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyTest {

    private static HttpClient httpClient = HttpUtils.getHttpClient2();
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());


    @Test
    public void test1() {
        int i = Integer.parseInt("");
    }

    @Test
    public void test2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        Date date = sdf.parse("20170519 16:59:59");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        System.out.println(sdf.format(calendar.getTime()));

        calendar.add(Calendar.DATE, 2);
        System.out.println(sdf.format(calendar.getTime()));

        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        System.out.println(sdf.format(calendar.getTime()));
        
        String json = "{\"name\":\"haha\"}";

    }

    @Test
    public void test3() throws ExecutionException {

        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
        String test = cache.get("test", () -> {
            String str = "test-value";
            return str;
        });

        System.out.println(test);



    }


    @Test
    public void test4() {
        //key enlogistics-biz:lg_ports:level:3:has_no_db_result_flag
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("level", "3");
        LgPortsPO lgPortsPO = JSON.parseObject(jsonObject.toJSONString(), LgPortsPO.class);
        System.out.println(lgPortsPO);
        List<LgPortsPO> list = new ArrayList<>();
        list.add(lgPortsPO);
        //language=JSON
        String json = "[{\"id\":1,\"level\":\"3\",\"areaCode\":\"101\"},{\"id\":2,\"level\":\"3\",\"areaCode\":\"102\"}]";
        System.out.println(json);
        List<LgPortsPO> lgPortsPOS = JSONArray.parseArray(json, LgPortsPO.class);
        System.out.println(lgPortsPOS);

        String ss = "[\"name:\"sss]";
    }

    @Test
    public void test5() {

        scheduledExecutorService.schedule(new MyTask(scheduledExecutorService),0, TimeUnit.SECONDS);
//

        try {
            Thread.sleep(1000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class MyTask implements Runnable {
        private int count = 0;
        private ScheduledExecutorService service;

        public MyTask(ScheduledExecutorService service) {
            this.service = service;
        }

        @Override
        public void run() {
            HttpPost httpPost=new HttpPost("http://127.0.0.1:8080/testjobs1");
            HttpResponse rsp = null;
            try {
                System.out.println("retry :" + count);
                rsp = httpClient.execute(httpPost);
                System.out.println(rsp.getStatusLine().getStatusCode());
            } catch (Exception e) {
                System.out.println(e.getMessage());
                if (count < 3) {
                    count++;
                    switch (count) {
                        case 1:
                            this.service.schedule(this,2 , TimeUnit.SECONDS);
                            break;
                        case 2:
                            this.service.schedule(this, 4, TimeUnit.SECONDS);
                            break;
                        case 3:
                            this.service.schedule(this, 6, TimeUnit.SECONDS);
                    }
                }
            }finally{
                if (rsp != null) {
                    HttpClientUtils.closeQuietly(rsp);
                }
            }

        }
    }


    @Test
    public void test6(){
        try {
            JSONObject result = JSONObject.parseObject(new FileInputStream("D:/data.json"), null);
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testJson() {
        Address address = new Address();
        address.setCode(22);
        address.setGrade(1);

        PersonPo po = new PersonPo();
        po.setUsername("lisi");
        po.setPassword("123456");
        po.setAge(23);
        po.setAddress(JSON.toJSONString(address));
        String s = JSON.toJSONString(po);
        System.out.println(s.replace("\\","\\\\"));

        System.out.println(URLEncoder.encode(JSON.toJSONString(po)));
    }


}

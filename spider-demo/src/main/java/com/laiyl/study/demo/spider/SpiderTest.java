package com.laiyl.study.demo.spider;

import com.laiyl.study.demo.spider.vo.Chapter;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpiderTest {

    public static final String URL = "https://www.xs222.tw/html/16/16593/";

    public static void main(String[] args) throws IOException {

        List<String> category = getCategory(URL);

//        for (String item :  category) {
//            Chapter chapter = getChapter(item);
//        }

//        getChapter("https://www.xs222.tw/html/16/16593/9465303.html");



    }

    public static Chapter getChapter(String url) throws IOException {
        Chapter chapter = new Chapter();

        Document document = Jsoup.connect(url).get();

        String title = document.getElementsByTag("h1").text();

        String content = document.getElementById("content").text();

        chapter.setTitle(title);
        chapter.setContent(content);

        System.out.println(title);

        return chapter;
    }

    public static List<String> getCategory(String url) throws IOException {
        List<String> list = new ArrayList<>();

//        Connection connect = Jsoup.connect("https://www.xs222.tw/html/16/16593/");
        Connection connect = Jsoup.connect(url);
        Document document = connect.get();
        Elements els = document.getElementById("list").getElementsByTag("a");
        for (Element e : els) {
            //获取绝对路径
            String link = e.attr("abs:href");
            System.out.println(link);
            list.add(link);
        }
        return list;
    }
}

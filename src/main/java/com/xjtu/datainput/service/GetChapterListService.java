package com.xjtu.datainput.service;

import com.jayway.jsonpath.JsonPath;
import com.xjtu.common.Config;
import com.xjtu.datainput.domain.CatalogListLevel1;
import com.xjtu.datainput.domain.CatalogListLevel2;
import com.xjtu.datainput.domain.CatalogListLevel3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shilei on 2017/3/14.
 */

public class GetChapterListService {


    public static void main(String[] args) {
        new GetChapterListService().get("4800FD2B-C9DA-4994-AF88-95DE7C2EF980");
    }

    public ArrayList<CatalogListLevel1> get(String CourseID) {

        /**
         * 从mooc2u获取课程json
         */

        Map<String, Object> map = new HashMap<>();
        map.put("CourseID", CourseID);
        String json = sendGet(Config.MOOC2U_API_GET_COURSE + "?CourseID=" + CourseID);
        //                System.out.println(json);


        /**
         * 解析json
         */
        List<Map<String, Object>> Catalogs = JsonPath.read(json, "$.Children[*]");

        //        System.out.println(Catalogs);
        ArrayList<CatalogListLevel1> result = new ArrayList<>();

        for (Map<String, Object> string : Catalogs) {
            String ParentChapterName = JsonPath.read(string, "$.Name").toString();
            String ParentChapterID = JsonPath.read(string, "$.ID").toString();
            if (ParentChapterName.equals("期末测试")) continue;
            //			System.out.println(ParentChapterID+" : "+ParentChapterName);

            CatalogListLevel1 level1 = new CatalogListLevel1(ParentChapterID, null, ParentChapterName);
            ArrayList<CatalogListLevel2> level2list = new ArrayList<>();

            List<Map<String, Object>> chapter = JsonPath.read(string, "$.Children[*]");
            for (Map<String, Object> string2 : chapter) {

                String ChapterName = JsonPath.read(string2, "$.Name").toString();
                String ChapterID = JsonPath.read(string2, "$.ID").toString();
                //                				System.out.println(ChapterID+" : "+ChapterName);

                ArrayList<CatalogListLevel3> level3list = new ArrayList<>();
                List<Map<String, Object>> ChildrenChapter = JsonPath.read(string2, "$.Children[*]");
                for (Map<String, Object> string3 : ChildrenChapter) {
                    String ChildrenChapterName = JsonPath.read(string3, "$.Name").toString();
                    String ChildrenChapterID = JsonPath.read(string3, "$.ID").toString();
                    //					System.out.println(ChildrenChapterID+" : "+ChildrenChapterName);
                    if (isChapter(ChildrenChapterName)) {
                        level3list.add(new CatalogListLevel3(ChildrenChapterID, ChildrenChapterName));
                    }
                }

                if (isChapter(ChapterName)) {
                    level2list.add(new CatalogListLevel2(level3list, ChapterID, ChapterName));
                }


            }
            level1.setChapterList(level2list);
            result.add(level1);
        }


        //		ObjectMapper mapper = new ObjectMapper();
        //		try {
        //			System.out.println(mapper.writeValueAsString(result));
        //		} catch (JsonProcessingException e) {
        //			e.printStackTrace();
        //		}

        return result;
    }


    public static boolean isChapter(String string) {
        if (!(string.indexOf("课件") != -1 || string.indexOf("PPT") != -1
                || string.indexOf("导学视频") != -1 || string.indexOf("授课视频") != -1 || string.indexOf("文字材料") != -1
                || string.indexOf("课后练习") != -1 || string.indexOf("案例学习") != -1 || string.indexOf("试题") != -1
                || string.indexOf("文字教材") != -1 || string.indexOf("课后作业") != -1 || string.indexOf("导学") != -1
                || string.indexOf("期末测试") != -1 || string.indexOf("综合测试") != -1 || string.indexOf("习题") != -1))
            return true;
        return false;
    }

    public static String sendGet(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            //            for (String key : map.keySet()) {
            //                System.out.println(key + "--->" + map.get(key));
            //            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}

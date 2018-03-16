package com.xjtu.datainput.service;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.xjtu.common.Config;
import com.xjtu.datainput.domain.CatalogListLevel1;
import com.xjtu.datainput.domain.CatalogListLevel2;
import com.xjtu.datainput.domain.CatalogListLevel3;
import com.xjtu.utils.Http;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shilei on 2017/3/14.
 */

public class GetChapterListService {


    public static void main(String[] args) {
        new GetChapterListService().get("a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5");
    }

    public ArrayList<CatalogListLevel1> get(String CourseID) {

        /**
         * 从mooc2u获取课程json
         */

        Map<String, Object> map = new HashMap<>();
        map.put("CourseID", CourseID);
        String json = null;
        String temp = Http.sendGet(Config.MOOC2U_API_GET_ONE_COURSE_INFO + "?CourseID=" + CourseID);
//            System.out.println(temp);
        json = temp;
//      json = new String(temp.getBytes( "UTF-8"));
//        System.out.println(json);


        /**
         * 解析json
         */
        List<Map<String, Object>> Catalogs = JsonPath.read(json, "$.children[*]");

        //        System.out.println(Catalogs);
        ArrayList<CatalogListLevel1> result = new ArrayList<>();

        for (Map<String, Object> string : Catalogs) {
            String ParentChapterName = JsonPath.read(string, "$.name").toString();
            String ParentChapterID = JsonPath.read(string, "$.id").toString();
            if (ParentChapterName.equals("期末测试")) continue;
//            			System.out.println(ParentChapterID+" ParentChapterID:ParentChapterName "+ParentChapterName);

            CatalogListLevel1 level1 = new CatalogListLevel1(ParentChapterID, null, ParentChapterName);
            ArrayList<CatalogListLevel2> level2list = new ArrayList<>();
            try {
                List<Map<String, Object>> chapter = JsonPath.read(string, "$.children[*]");
                for (Map<String, Object> string2 : chapter) {

                    String ChapterName = JsonPath.read(string2, "$.name").toString();
                    String ChapterID = JsonPath.read(string2, "$.id").toString();
//                                				System.out.println(ChapterID+"ChapterID :ChapterName "+ChapterName);

                    ArrayList<CatalogListLevel3> level3list = new ArrayList<>();
                    try {
                        List<Map<String, Object>> ChildrenChapter = JsonPath.read(string2, "$.children[*]");
                        for (Map<String, Object> string3 : ChildrenChapter) {
                            String ChildrenChapterName = JsonPath.read(string3, "$.name").toString();
                            String ChildrenChapterID = JsonPath.read(string3, "$.id").toString();
//                        System.out.println(ChildrenChapterID+"ChildrenChapterID :ChildrenChapterName "+ChildrenChapterName);
                            if (isChapter(ChildrenChapterName)) {
                                level3list.add(new CatalogListLevel3(ChildrenChapterID, ChildrenChapterName));
                            }
                        }

                        if (isChapter(ChapterName)) {
                            level2list.add(new CatalogListLevel2(level3list, ChapterID, ChapterName));
                        }
                    } catch (PathNotFoundException e) {
                        // json中不存在这个字段，自动跳过。有些字段下面没有children(2018年3月16日15:03:08发现的)
                    }

                }
            } catch (PathNotFoundException e) {
                // json中不存在这个字段，自动跳过。有些字段下面没有children(2018年3月16日15:03:08发现的)
            }
            level1.setChapterList(level2list);
            result.add(level1);
        }

//
//        		ObjectMapper mapper = new ObjectMapper();
//        		try {
//        			System.out.println(mapper.writeValueAsString(result));
//        		} catch (JsonProcessingException e) {
//        			e.printStackTrace();
//        		}

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


}

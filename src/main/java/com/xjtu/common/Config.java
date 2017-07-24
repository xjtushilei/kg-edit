package com.xjtu.common;

/**
 * Created by shilei on 2017/3/14.
 */
public class Config {


    /**
     * 奥鹏的API网址，获取课程的章节等信息
     *
     * @author 石磊
     */
    public static String MOOC2U_API_GET_ONE_COURSE_INFO = "http://www.mooc2u.com/API/Open/CourseOpen/GetCourseData";
    public static String MOOC2U_API_GET_ALL_COURSE = "http://www.mooc2u.com/API/Open/CourseOpen/GetAllCourseData";


    /**
     * Spider 爬虫参数设置
     *
     * @author 郑元浩
     */

    public static String project = "";  //工程的名字，设计到api的路径.有域名则不设置
    public static String imageAPICata = "spider";
    public static int imageCount = 3; // 设置每个分面下的图片数量
}

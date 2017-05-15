package com.xjtu.dashboard.web;

import com.jayway.jsonpath.JsonPath;
import com.xjtu.common.domain.Error;
import com.xjtu.dashboard.domain.ClassStatus;
import com.xjtu.dashboard.repository.ClassStatusRepository;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xjtu.common.Config.MOOC2U_API_GET_ALL_COURSE;
import static com.xjtu.utils.Http.sendGet;

/**
 * Created by shilei on 2017/3/13.
 */

@RestController
@RequestMapping("/Dashboard")
public class DashboardController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ClassStatusRepository classStatusRepository;

    @RequestMapping(value = "/RecordCourse", method = RequestMethod.GET)
    @ApiOperation(value = "记录该课程,返回课程的id和name", notes = "记录该课程,返回课程的id和name")
    public ResponseEntity createClass(@RequestParam(value = "classID", defaultValue = "a7a6e4b7-e5d1-42a4-b7d5-0f7c6a7ff9e5") String classID) {

        classID = classID.toLowerCase();
        ClassStatus course = classStatusRepository.findByClassid(classID);
        //首先从本地查找，没有的话再从mooc2u的api获取，并加入到本地
        if (course == null) {
            Map<String, String> courseID2CourseName = new HashMap<>();
            try {
                String allCourseJson = sendGet(MOOC2U_API_GET_ALL_COURSE);

                List<Map<String, Object>> classList = JsonPath.read(allCourseJson, "$.Data[*]");

                classList.forEach(map -> courseID2CourseName.put(String.valueOf(map.get("CourseID")).toLowerCase(), String.valueOf(map.get("CourseName"))));
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error
                        ("从<" + MOOC2U_API_GET_ALL_COURSE + ">获取json失败！请重试或者联系负责人！"));
            }
            try {
                String className = courseID2CourseName.get(classID);
                course = new ClassStatus(classID, className);
                System.out.println(course);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error
                        ("在<" + MOOC2U_API_GET_ALL_COURSE + ">中没有找到该门课，请联系负责人！"));
            }

            try {
                course = classStatusRepository.save(course);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error
                        ("写入到本工程的库中失败！"));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(course);
    }

    //    @RequestMapping(value = "/getstatus", method = RequestMethod.GET)
    //    @ApiOperation(value = "获取课程的目前的状态", notes = "获取课程的目前的状态")
    //    public ResponseEntity get() {
    //
    //        List<ClassStatus> result = new ArrayList<>();
    //        try {
    //            result = classStatusRepository.findAll();
    //        } catch (Exception e) {
    //            logger.error("获取课程 failed, ", e);
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e);
    //        }
    //        return ResponseEntity.status(HttpStatus.OK).body(result);
    //    }
    //
    //
    //    @RequestMapping(value = "/createClass", method = RequestMethod.GET)
    //    @ApiOperation(value = "图谱API总接口，创建一门课程任务", notes = "将一门课程加入图谱后台系统里")
    //    public ResponseEntity createClass(@RequestParam(value = "classID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String classID,
    //                                      @RequestParam(value = "className", defaultValue = "测试课程") String className) {
    //
    //        ClassStatus result = new ClassStatus(classID, className);
    //        try {
    //            result = classStatusRepository.save(result);
    //        } catch (Exception e) {
    //            logger.error("创建课程 failed, ", e);
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.toString()));
    //        }
    //        return ResponseEntity.status(HttpStatus.OK).body(new Success("创建成功！"));
    //    }
    //
    //    @RequestMapping(value = "/updateClass", method = RequestMethod.GET)
    //    @ApiOperation(value = "图谱API总接口，重置一门课程的状态", notes = "重置一门课程的状态，这样老师可以进行上一步的修改")
    //    public ResponseEntity updateClass(@RequestParam(value = "classID", defaultValue = "4800FD2B-C9DA-4994-AF88-95DE7C2EF980") String classID,
    //                                      @RequestParam(value = "className", defaultValue = "测试课程") String className) {
    //
    //        ClassStatus result = new ClassStatus(classID, className);
    //        try {
    //            result = classStatusRepository.save(result);
    //        } catch (Exception e) {
    //            logger.error("重置课程失败, ", e);
    //            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Error(e.toString()));
    //        }
    //        return ResponseEntity.status(HttpStatus.OK).body(new Success("成功更新！"));
    //    }

}

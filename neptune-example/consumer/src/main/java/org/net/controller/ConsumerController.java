package org.net.controller;

import lombok.extern.slf4j.Slf4j;
import org.net.dto.Student;
import org.net.dto.Teacher;
import org.net.service.StudentService;
import org.net.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-07 18:02
 */
@Slf4j
@RestController
@RequestMapping(value = "/neptuneRpc")
public class ConsumerController {

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private StudentService studentService;

    @GetMapping(value = "/getTeacher")
    public Teacher invokerDirectory() {
        return teacherService.getTeacher(12);
    }

    @GetMapping(value = "/selectStudentList")
    public List<Student> selectStudentList() {

        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        return studentService.selectStudentList(idList);
    }

    @GetMapping(value = "/selectStudentMap")
    public Map<Integer, Student> selectStudentMap() {
        Map<Integer, Student> studentMap1 = new HashMap<>();
        studentMap1.put(12, new Student(12, "xxoo"));
        return  studentService.selectStudentMap(studentMap1);
    }


}

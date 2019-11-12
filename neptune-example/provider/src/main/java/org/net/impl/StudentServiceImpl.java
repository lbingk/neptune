package org.net.impl;

import org.net.dto.Student;
import org.net.service.StudentService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 18:26
 */
public class StudentServiceImpl implements StudentService {
    @Override
    public Student getStudent(Integer id) {
        return new Student(1, "xxooxxoo");
    }

    @Override
    public List<Student> selectStudentList(List<Integer> idList) {
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(12, "xxoo"));
        return studentList;
    }

    @Override
    public Map<Integer, Student> selectStudentMap(Map<Integer, Student> studentMap) {
        Map<Integer, Student> studentMap1 = new HashMap<>();
        studentMap1.put(12, new Student(12, "xxoo"));
        return studentMap1;
    }
}

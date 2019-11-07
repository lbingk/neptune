package org.net.impl;

import org.net.dto.Student;
import org.net.service.StudentService;

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
}

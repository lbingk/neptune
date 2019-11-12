package org.net.impl;

import org.net.dto.Teacher;
import org.net.service.TeacherService;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 18:26
 */
public class TeacherServiceImpl implements TeacherService {
    @Override
    public Teacher getTeacher(Integer id) {
        return new Teacher(1, "xxooxxoo");
    }
}

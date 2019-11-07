package org.net.controller;

import lombok.extern.slf4j.Slf4j;
import org.net.dto.Teacher;
import org.net.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping(value = "/getTeacher")
    public Teacher invokerDirectory() {
        return teacherService.getTeacher(12);
    }
}

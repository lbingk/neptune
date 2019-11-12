package org.net.service;

        import org.net.dto.Student;

        import java.util.List;
        import java.util.Map;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 18:19
 */
public interface StudentService {
    Student getStudent(Integer id);

    List<Student> selectStudentList(List<Integer> idList);

    Map<Integer, Student> selectStudentMap(Map<Integer, Student> studentMap);
}

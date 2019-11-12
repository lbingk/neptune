package org.net.dto;

/**
 * @Classname Student
 * @author: LUOBINGKAI
 * @Description TODO
 * @Date 2019/11/2 23:26
 */

public class Student {
    private Integer id;

    public Student(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

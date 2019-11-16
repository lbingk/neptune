package org.net.dto;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 18:20
 */

public class Teacher {
    private Integer id;

    public Teacher(Integer id, String name) {
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

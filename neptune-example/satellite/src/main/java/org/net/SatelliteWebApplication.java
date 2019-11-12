package org.net;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-02 11:57
 */
@SpringBootApplication()
@ImportResource(locations = {"classpath:satellite.xml"})
public class SatelliteWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(SatelliteWebApplication.class,args);
    }
}

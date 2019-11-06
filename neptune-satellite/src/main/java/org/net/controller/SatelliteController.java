package org.net.controller;

import lombok.extern.slf4j.Slf4j;
import org.net.manager.RegistrationDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @program: neptune
 * @description:
 * @author: LUOBINGKAI
 * @create: 2019-11-05 14:37
 */
@Slf4j
@RestController
@RequestMapping(value = "/satellite")
public class SatelliteController {

    @GetMapping(value = "/provider")
    public Map<String, Map<String, String>> provider() {
        return RegistrationDirectory.serviceBeanInfoMap;
    }

    @GetMapping(value = "/consumer")
    public Map<String, Set<String>> consumer() {
        return RegistrationDirectory.referenceBeanInfoMap;
    }

    @GetMapping(value = "/refreshBeanInfo")
    public void refreshBeanInfo() {
        RegistrationDirectory.refreshBeanInfo();
    }
}

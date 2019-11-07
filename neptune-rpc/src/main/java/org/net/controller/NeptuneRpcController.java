package org.net.controller;

import lombok.extern.slf4j.Slf4j;
import org.net.io.reference.InvokerDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @program: neptune
 * @description:
 * @author: luobingkai
 * @create: 2019-11-07 16:18
 */
@Slf4j
@RestController
@RequestMapping(value = "/neptuneRpc")
public class NeptuneRpcController {

    @GetMapping(value = "/invokerDirectory")
    public Map<String, Set<String>> invokerDirectory() {
        return InvokerDirectory.getInvokerDirectoryMap();
    }

}

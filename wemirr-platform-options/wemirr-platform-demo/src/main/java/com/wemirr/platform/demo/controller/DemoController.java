package com.wemirr.platform.demo.controller;

import com.wemirr.framework.commons.entity.Result;
import com.wemirr.framework.db.dynamic.event.body.TenantDynamicDatasource;
import com.wemirr.framework.db.dynamic.feign.TenantFeignClient;
import com.wemirr.framework.security.configuration.client.annotation.IgnoreAuthorize;
import com.wemirr.platform.demo.service.DemoService;
import com.wemirr.platform.demo.service.client.DemoTestFeignClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Levin
 */
@Slf4j
@RestController
@RequestMapping("/demos")
@RequiredArgsConstructor
@Tag(name = "Demo测试", description = "Demo测试")
public class DemoController {

    private final DemoService demoService;
    private final DemoTestFeignClient demoTestFeignClient;
    private final TenantFeignClient tenantFeignClient;

    @IgnoreAuthorize
    @GetMapping("/ignore")
    @Operation(summary = "忽略权限查询")
    public Result<?> ignore() {
        log.info("输出内容 - {}", demoService.sayHello());
        return Result.success();
    }

    @GetMapping("/say")
    @Operation(summary = "带权限查询")
    public String say() {
        log.info("输出内容 - {}", demoService.sayHello());
        return "say...";
    }


    @IgnoreAuthorize
    @GetMapping("/feign")
    @Operation(summary = "自动生成Token查询", description = "需要配置登录信息才可以")
    public List<?> feign() {
        final Result<List<TenantDynamicDatasource>> result = tenantFeignClient.selectAll();
        return demoTestFeignClient.query();
    }
}

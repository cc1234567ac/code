package cn.acbcl.code.controller;

import cn.acbcl.code.service.GenerateService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("generate")
public class GenerateController {

    @Resource
    private GenerateService generateService;

    @GetMapping("byTableName")
    public Object byTableName(@RequestParam("tableName") String tableName){
        generateService.byTableName(tableName);
        return null;
    }

    @GetMapping("byDBName")
    public Object byDBName(){
        return null;
    }
}

package com.mars.ecsheet.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mars.ecsheet.entity.WorkSheetEntity;
import com.mars.ecsheet.repository.WorkSheetRepository;
import com.mars.ecsheet.utils.SheetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mars
 * @date 2020/10/28
 * @description 表格数据加载相关请求
 */
@RestController
public class IndexController {

    @Autowired
    private WorkSheetRepository workSheetRepository;

    /**
     * 根据表格id加载表格信息
     * @param wbId 表格id
     * @return 表格信息
     */
    @PostMapping("/load/{wbId}")
    public String load(@PathVariable(value = "wbId") String wbId) {

        List<WorkSheetEntity> wsList = workSheetRepository.findAllBywbId(wbId);
        List<JSONObject> list = new ArrayList<>();
        wsList.forEach(ws -> {
            list.add(ws.getData());
        });


        return JSONUtil.toJsonStr(list);
    }


    /**
     * 根据表格id加载表格sheet页信息
     * @param wbId 表格id
     * @return sheet页信息
     */
    @PostMapping("/loadSheet/{wbId}")
    public String loadSheet(@PathVariable(value = "wbId") String wbId) {
        List<WorkSheetEntity> wsList = workSheetRepository.findAllBywbId(wbId);
        List<JSONObject> list = new ArrayList<>();
        wsList.forEach(ws -> {
            list.add(ws.getData());
        });
        if (!list.isEmpty()) {
            return SheetUtil.buildSheetData(list).toString();
        }
        return SheetUtil.getDefaultAllSheetData().toString();
    }


}

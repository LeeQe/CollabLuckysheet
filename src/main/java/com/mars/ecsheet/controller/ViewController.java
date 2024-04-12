package com.mars.ecsheet.controller;

import com.mars.ecsheet.entity.WorkBookEntity;
import com.mars.ecsheet.entity.WorkSheetEntity;
import com.mars.ecsheet.repository.WorkBookRepository;
import com.mars.ecsheet.repository.WorkSheetRepository;
import com.mars.ecsheet.utils.SheetUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 在普通Controller中，如果不在响应方法上加上@ResponseBody，那么默认返回的是一个页面
 */
@Controller
public class ViewController {

    @Autowired
    private WorkBookRepository workBookRepository;

    @Autowired
    private WorkSheetRepository workSheetRepository;


    /**
     * 返回应用的主页面
     * @return 主页面
     */
    @GetMapping(path = {"/", "/index"})
    public ModelAndView index() {
        List<WorkBookEntity> all = workBookRepository.findAll();

        // 返回静态资源的根页面。对于返回页面的请求，比较规范的做法是单独写一个controller
        return new ModelAndView("index", "all", all);
    }


    /**
     * 创建一个新的表格
     * @param request 请求体
     * @param response 返回新表格页面
     * @throws IOException IO异常
     */
    @GetMapping("index/create")
    public void create(HttpServletRequest request, HttpServletResponse response) throws IOException {
        WorkBookEntity wb = new WorkBookEntity();
        wb.setName("default");
        wb.setOption(SheetUtil.getDefautOption());
        WorkBookEntity saveWb = workBookRepository.save(wb);
        //生成sheet数据
        generateSheet(saveWb.getId());
        response.sendRedirect("/index/" + saveWb.getId());
    }


    /**
     * 根据id查询表格，并进入协同编辑页
     * @param wbId 工作表id
     * @return 表格协同页面
     */
    @GetMapping("/index/{wbId}")
    public ModelAndView index(@PathVariable(value = "wbId") String wbId) {
        Optional<WorkBookEntity> Owb = workBookRepository.findById(wbId);
        WorkBookEntity wb = new WorkBookEntity();
        if (!Owb.isPresent()) {
            wb.setId(wbId);
            wb.setName("default");
            wb.setOption(SheetUtil.getDefautOption());
            WorkBookEntity result = workBookRepository.save(wb);
            generateSheet(wbId);
        } else {
            wb = Owb.get();
        }

        return new ModelAndView("websocket", "wb", wb);
    }


    /**
     * 创建一个新的工作表，并保存到mongoDB数据库中
     * @param wbId 工作表id
     */
    private void generateSheet(String wbId) {
        SheetUtil.getDefaultSheetData().forEach(jsonObject -> {
            WorkSheetEntity ws = new WorkSheetEntity();
            ws.setWbId(wbId);
            ws.setData(jsonObject);
            ws.setDeleteStatus(0);
            workSheetRepository.save(ws);
        });
    }
}

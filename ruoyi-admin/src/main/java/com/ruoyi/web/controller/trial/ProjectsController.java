package com.ruoyi.web.controller.trial;

import java.io.*;
import java.util.*;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.trial.domain.Projects;
import com.ruoyi.trial.domain.TrialTaskDetailProd;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.service.IProjectsService;
import com.ruoyi.trial.service.ITrialTaskDetailProdService;
import com.ruoyi.trial.service.ITrialTaskProdService;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * 项目管理Controller
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@RestController
@RequestMapping("/trial/projects")
public class ProjectsController extends BaseController {
    @Autowired
    private IProjectsService projectsService;

    @Autowired
    private ITrialTaskProdService trialTaskProdService;

    @Autowired
    private ITrialTaskDetailProdService trialTaskDetailProdService;

    // 日志
    private static final Logger log = LoggerFactory.getLogger(ProjectsController.class);
    // 注入你的项目服务

    /**
     * 查询项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:list')")
    @GetMapping("/list")
    public TableDataInfo list(Projects projects) {
        startPage();
        List<Projects> list = projectsService.selectProjectsList(projects);
        return getDataTable(list);
    }

    /**
     * 导出项目管理列表
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:export')")
    @Log(title = "项目管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Projects projects) {
        List<Projects> list = projectsService.selectProjectsList(projects);
        ExcelUtil<Projects> util = new ExcelUtil<Projects>(Projects.class);
        util.exportExcel(response, list, "项目管理数据");
    }

    /**
     * 获取项目管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(projectsService.selectProjectsById(id));
    }

    /**
     * 新增项目管理
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:add')")
    @Log(title = "项目管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Projects projects) {
        return toAjax(projectsService.insertProjects(projects));
    }

    /**
     * 修改项目管理
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:edit')")
    @Log(title = "项目管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Projects projects) {
        return toAjax(projectsService.updateProjects(projects));
    }

    /**
     * 删除项目管理
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:remove')")
    @Log(title = "项目管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(projectsService.deleteProjectsByIds(ids));
    }

    /**
     * 打印项目基本信息
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:print')")
    @Log(title = "项目管理", businessType = BusinessType.OTHER)
    @GetMapping("/print/{id}")
    public void print(@PathVariable Long id, HttpServletResponse response) throws Exception {
        Projects project = projectsService.selectProjectsById(id);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "未找到项目");
            return;
        }
        // 设置响应参数
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("projectName", project.getProjectName());
        parameters.put("assemblyName", project.getAssemblyName());
        parameters.put("projectLeader", project.getProjectLeader());
        parameters.put("projectStatus", project.getProjectStatus());


        response.setContentType("application/pdf");
        String encodedFileName = URLEncoder.encode(project.getProjectName() + "_项目信息.pdf", "utf-8");
        response.setHeader("content-disposition", "inline;filename=" + encodedFileName);

        try (InputStream inputStream = this.getClass().getResourceAsStream("/jasper/project_report.jasper")) {
            // 填充报表
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, parameters, new JREmptyDataSource());
            // 导出为PDF并输出到响应
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    /**
     * 导出项目为PDF
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:export')")
    @Log(title = "项目管理", businessType = BusinessType.EXPORT)
    @GetMapping(value = "/exportPdf/{id}", produces = "application/pdf")
    public void exportPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
        // 1. 查询项目数据
        Projects project = projectsService.selectProjectsById(id);
        if (project == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "未找到项目");
            return;
        }

        // 2. 响应内容类型设置为pdf
        response.setContentType("application/pdf");

        // 3. 设置响应文件的文件名
        String fileName = System.currentTimeMillis() + project.getProjectName() + "_项目信息报表.pdf";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("content-disposition", "attachment;filename=" + encodedFileName);

        // 4. 填充 Jasper 参数并生成 PDF
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("projectName", project.getProjectName());
        params.put("assemblyName", project.getAssemblyName());
        params.put("projectLeader", project.getProjectLeader());
        params.put("projectStatus", project.getProjectStatus());

        try (InputStream inputStream = this.getClass().getResourceAsStream("/jasper/project_report.jasper")) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    /**
     * 打印项目所有零件的电子流转卡
     */
    @PreAuthorize("@ss.hasPermi('trial:projects:print')")
    @Log(title = "项目管理", businessType = BusinessType.OTHER)
    @GetMapping("/printAllCards/{id}")
    public void printAllCards(@PathVariable Long id, HttpServletResponse response) throws Exception {
        // ========== 【提前设置响应编码，无论什么情况都输出HTML，从根源杜绝空白页】 ==========
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            // 先输出完整HTML基础结构，确保页面永远有合法内容，不会出现空白
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>项目所有零件电子流转卡</title>");
            out.println("</head>");
            out.println("<body>");

            // 查询项目基础信息
            Projects project = projectsService.selectProjectsById(id);

            // ========== 【核心修改2：项目不存在时，输出友好提示，不返回空内容】 ==========
            if (project == null) {
                out.println("<div style='text-align: center; margin-top: 80px; font-size: 18px; color: #f56c6c;'>");
                out.println("<h2>未找到对应项目</h2>");
                out.println("<p>请检查项目ID是否正确，或联系管理员</p>");
                out.println("</div>");
                // 必须闭合HTML标签，保证页面结构完整
                out.println("</body></html>");
                out.flush();
                return;
            }

            // 项目存在，先输出页面标题和项目基础信息
            out.println("<h1>零件电子流转卡信息</h1>");
            out.println("<h2>项目名称：" + project.getProjectName() + "</h2>");

            // 查询项目下的所有任务
            TrialTaskProd trialTaskProd = new TrialTaskProd();
            trialTaskProd.setProjectId(id);
            List<TrialTaskProd> taskList = trialTaskProdService.selectTrialTaskProdList(trialTaskProd);

            // ========== 【核心修改3：无任务/无流转卡时，输出友好提示，替代空白页】 ==========
            if (taskList == null || taskList.isEmpty()) {
                out.println("<div style='text-align: center; margin-top: 60px; font-size: 16px; color: #909399;'>");
                out.println("<h3>该项目暂无任务</h3>");
                out.println("<p>当前项目下没有可打印的零件流转卡，请先创建任务后再试</p>");
                out.println("</div>");
                // 闭合HTML标签，保证结构完整
                out.println("</body></html>");
                out.flush();
                return;
            }

            // ========== 原有正常业务逻辑：有任务时输出流转卡表格 ==========
            for (TrialTaskProd task : taskList) {
                out.println("<h3>任务名称：" + task.getTitle() + "</h3>");
                out.println("<table border='1' cellpadding='5' cellspacing='0'>");
                out.println("<tr><td>任务编号</td><td>" + task.getTaskId() + "</td></tr>");
                out.println("<tr><td>总成名称</td><td>" + task.getAssemblyName() + "</td></tr>");
                out.println("<tr><td>总成图号</td><td>" + task.getAssemblyFigure() + "</td></tr>");
                out.println("<tr><td>试制管理员</td><td>" + task.getPm() + "</td></tr>");
                out.println("<tr><td>PE姓名</td><td>" + task.getPe() + "</td></tr>");
                out.println("<tr><td>状态</td><td>" + task.getStatus() + "</td></tr>");
                out.println("</table>");

                // 查询该任务的所有流转程序
                TrialTaskDetailProd detailProd = new TrialTaskDetailProd();
                detailProd.setTaskId(task.getTaskId());
                List<TrialTaskDetailProd> detailList = trialTaskDetailProdService.selectTrialTaskDetailProdList(detailProd);

                if (detailList != null && !detailList.isEmpty()) {
                    out.println("<h4>试制详情：</h4>");
                    out.println("<table border='1' cellpadding='5' cellspacing='0'>");
                    out.println("<tr><th>序号</th><th>流转程序</th><th>名称</th><th>图号</th><th>试制数量</th><th>送检数量</th><th>制造质量确认</th><th>工艺质量确认</th></tr>");

                    for (int i = 0; i < detailList.size(); i++) {
                        TrialTaskDetailProd detail = detailList.get(i);
                        out.println("<tr>");
                        out.println("<td>" + (i + 1) + "</td>");
                        out.println("<td>" + detail.getProgram() + "</td>");
                        out.println("<td>" + detail.getName() + "</td>");
                        out.println("<td>" + detail.getFigure() + "</td>");
                        out.println("<td>" + detail.getTrialQuantity() + "</td>");
                        out.println("<td>" + detail.getInspectionQuantity() + "</td>");
                        out.println("<td>" + detail.getManufacturingQualityStatus() + "</td>");
                        out.println("<td>" + detail.getProcessQualityStatus() + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                }
                out.println("<hr />");
            }

            // 页面完全加载后再触发自动打印，避免打印空白内容
            out.println("<script>window.onload = function() { window.print(); }</script>");
            // 闭合HTML标签，保证页面结构完整
            out.println("</body></html>");
            out.flush();
        }
    }
}

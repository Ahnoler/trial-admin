package com.ruoyi.trial.service.impl;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONArray;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.QRCodeUtil;
import com.ruoyi.common.utils.uuid.UUID;
import com.ruoyi.common.entity.ActionLog;
import org.springframework.beans.BeanUtils;
import com.ruoyi.trial.domain.CardModelDetailProd;
import com.ruoyi.trial.domain.DTO.TrialTaskDetailProdPdfDTO;
import com.ruoyi.trial.domain.TrialTaskDetailProd;
import com.ruoyi.trial.mapper.ActionLogMapper;
import com.ruoyi.trial.mapper.TrialTaskDetailProdMapper;
import com.ruoyi.trial.service.ICardModelDetailProdService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.trial.mapper.TrialTaskProdMapper;
import com.ruoyi.trial.domain.TrialTaskProd;
import com.ruoyi.trial.service.ITrialTaskProdService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;

/**
 * 试制任务信息Service业务层处理
 *
 * @author ruoyi
 * @date 2023-07-11
 */
@Service
public class TrialTaskProdServiceImpl implements ITrialTaskProdService {
    @Autowired
    private TrialTaskProdMapper trialTaskProdMapper;

    @Autowired
    private TrialTaskDetailProdMapper trialTaskDetailProdMapper;

    @Autowired
    private ICardModelDetailProdService cardModelDetailProdService;

    @Autowired
    private ActionLogMapper actionLogMapper;

    @Autowired
    private com.ruoyi.trial.service.IPrintLogService printLogService;


    /**
     * 查询试制任务信息
     *
     * @param taskId 试制任务信息主键
     * @return 试制任务信息
     */
    @Override
    public TrialTaskProd selectTrialTaskProdByTaskId(Long taskId) {
        return trialTaskProdMapper.selectTrialTaskProdByTaskId(taskId);
    }

    /**
     * 查询试制任务信息列表
     *
     * @param trialTaskProd 试制任务信息
     * @return 试制任务信息
     */
    @Override
    public List<TrialTaskProd> selectTrialTaskProdList(TrialTaskProd trialTaskProd) {
        return trialTaskProdMapper.selectTrialTaskProdList(trialTaskProd);
    }

    /**
     * 新增试制任务信息
     *
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    @Override
    public int insertTrialTaskProd(TrialTaskProd trialTaskProd) {
        trialTaskProd.setCurrentSerialNo(101);
        trialTaskProd.setCurrentSerialName("物料1");
        trialTaskProd.setCardType("PROD");
        trialTaskProd.setCreateTime(DateUtils.getNowDate());
        trialTaskProdMapper.insertTrialTaskProd(trialTaskProd);
        
        String qrCode = "TASK_" + trialTaskProd.getTaskId() + "_" + UUID.randomUUID().toString().substring(0, 8);
        String qrCodeBase64 = QRCodeUtil.generateQRCodeBase64(qrCode, 200, 200);
        trialTaskProd.setQrCode(qrCode);
        trialTaskProd.setQrCodeUrl(qrCodeBase64);
        trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);

        return insertTrialTaskProdDetail(trialTaskProd);
    }

    /**
     * 新增试制任务信息
     *
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    @Override
    public int copyTrialTaskProd(TrialTaskProd trialTaskProd) {
        trialTaskProdMapper.insertTrialTaskProd(trialTaskProd);
        
        String qrCode = "TASK_" + trialTaskProd.getTaskId() + "_" + UUID.randomUUID().toString().substring(0, 8);
        String qrCodeBase64 = QRCodeUtil.generateQRCodeBase64(qrCode, 200, 200);
        trialTaskProd.setQrCode(qrCode);
        trialTaskProd.setQrCodeUrl(qrCodeBase64);
        trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);
        
        return 1;
    }

    /**
     * 新增试制卡片信息
     *
     * @param trialTaskProd 试制任务信息
     */
    public int insertTrialTaskProdDetail(TrialTaskProd trialTaskProd) {
        int rows = 1;
        //查询卡片模板
        String cardType = trialTaskProd.getCardType();
        CardModelDetailProd cardModelDetailProd = new CardModelDetailProd();
        cardModelDetailProd.setCardType(cardType);
        List<CardModelDetailProd> cardModelDetailProdList =
                cardModelDetailProdService.selectCardModelDetailProdList(cardModelDetailProd);

        // 新增试制卡片
        List<TrialTaskDetailProd> list = new ArrayList<>();
        long taskId = trialTaskProd.getTaskId();

        for (CardModelDetailProd node : cardModelDetailProdList) {
            TrialTaskDetailProd newNode = new TrialTaskDetailProd();
            newNode.setTaskId(taskId);
            newNode.setCardType(node.getCardType());
            newNode.setDirector(node.getDirector());
            newNode.setFigure(node.getFigure());
            newNode.setColumnCode(node.getColumnCode());
            newNode.setDirectorTel(node.getDirectorTel());
            newNode.setInspectionQuantity(node.getInspectionQuantity());
            newNode.setName(node.getName());
            newNode.setManufacturingArea(node.getManufacturingArea());
            newNode.setManufacturingQualityStatus(node.getManufacturingQualityStatus());
            newNode.setMeDirector(node.getMeDirector());
            newNode.setMeDirectorTel(node.getMeDirectorTel());
            newNode.setProgram(node.getProgram());
            newNode.setNotes(node.getNotes());
            newNode.setSerialNo(node.getSerialNo());
            newNode.setStatus(node.getStatus());
            newNode.setTrialQuantity(node.getTrialQuantity());
            newNode.setProcessQualityStatus(node.getProcessQualityStatus());

            list.add(newNode);
        }
        if (list.size() > 0) {
            rows = trialTaskDetailProdMapper.batchTrialTaskDetailProd(list);
        }
        return rows;
    }

    /**
     * 修改试制任务信息
     *
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    @Override
    public int updateTrialTaskProd(TrialTaskProd trialTaskProd) {

        trialTaskProd.setUpdateTime(DateUtils.getNowDate());
        return trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);
    }

    /**
     * 批量删除试制任务-零件流转卡
     *
     * @param taskIds 需要删除的试制任务信息主键
     * @return 结果
     */
    @Override
    public int deleteTrialTaskProdByTaskIds(Long[] taskIds) {
        trialTaskDetailProdMapper.deleteTrialtaskDetailProdByTaskId(taskIds);
        return trialTaskProdMapper.deleteTrialTaskProdByTaskIds(taskIds);
    }

    /**
     * 变更试制任务信息
     *
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    @Override
    public int changeTrialTaskProd(TrialTaskProd trialTaskProd) {
        TrialTaskDetailProd node = new TrialTaskDetailProd();
        node.setTaskId(trialTaskProd.getTaskId());
        //需要把当前环节为正在填报，前面环节为已审核，后面的程序环节变更为未填报
        List<TrialTaskDetailProd> list = trialTaskDetailProdMapper.selectTrialTaskDetailProdList(node);
        for (int i = 0; i < list.size(); i++) {
            TrialTaskDetailProd currentNode = list.get(i);
            if (currentNode.getSerialNo() < trialTaskProd.getCurrentSerialNo()) {
                currentNode.setStatus("3");//已审核
                trialTaskDetailProdMapper.updateTrialTaskDetailProd(currentNode);
            } else if (currentNode.getSerialNo().intValue() == trialTaskProd.getCurrentSerialNo().intValue()) {
                currentNode.setStatus("1");//正在填报
                trialTaskDetailProdMapper.updateTrialTaskDetailProd(currentNode);
            } else if (currentNode.getSerialNo() > trialTaskProd.getCurrentSerialNo()) {
                currentNode.setStatus("0");//未填报
                trialTaskDetailProdMapper.updateTrialTaskDetailProd(currentNode);
            }
        }
        return 0;
    }

    /**
     * 分流试制任务信息
     *
     * @param trialTaskProd 试制任务信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int forkTrialTaskProd(TrialTaskProd trialTaskProd, long oldTaskId) {
        TrialTaskDetailProd node = new TrialTaskDetailProd();
        node.setTaskId(oldTaskId);
        List<TrialTaskDetailProd> list = trialTaskDetailProdMapper.selectTrialTaskDetailProdList(node);
        List<TrialTaskDetailProd> listInfo = JSONArray.parseArray(trialTaskProd.getListStr(), TrialTaskDetailProd.class);
        if (!listInfo.isEmpty()) {
            //设置关联任务ID
            trialTaskProd.setRelatedTaskId(oldTaskId);
            trialTaskProdMapper.updateTrialTaskProd(trialTaskProd);
            
            // 完全复制父母卡片的所有流转程序
            List<TrialTaskDetailProd> allProcesses = new ArrayList<>();
            list.forEach(process -> {
                TrialTaskDetailProd newProcess = new TrialTaskDetailProd();
                // 使用 BeanUtils 复制所有属性
                BeanUtils.copyProperties(process, newProcess);
                // 设置需要修改的属性
                newProcess.setTaskId(trialTaskProd.getTaskId());
                newProcess.setInspectionQuantity("0"); // 重置检验数量
                newProcess.setId(null); // 确保是新记录
                newProcess.setCreateTime(DateUtils.getNowDate());
                newProcess.setUpdateTime(DateUtils.getNowDate());
                allProcesses.add(newProcess);
            });
            
            // 更新分流的工序数量
            listInfo.forEach(item -> {
                for (TrialTaskDetailProd process : allProcesses) {
                    if (process.getSerialNo().equals(item.getSerialNo())) {
                        process.setTrialQuantity(item.getShuntQty());
                        break;
                    }
                }
            });
            
            // 批量插入所有流转程序
            trialTaskDetailProdMapper.batchTrialTaskDetailProd(allProcesses);
            
            // 处理分流卡片的状态：当前环节为正在填报，前面环节为已审核，后面的程序环节变更为未填报
            TrialTaskProd parentTask = selectTrialTaskProdByTaskId(oldTaskId);
            Integer currentSerialNo = parentTask.getCurrentSerialNo();
            for (TrialTaskDetailProd process : allProcesses) {
                if (process.getSerialNo() < currentSerialNo) {
                    process.setStatus("3"); // 已审核
                } else if (process.getSerialNo().intValue() == currentSerialNo.intValue()) {
                    process.setStatus("1"); // 正在填报
                } else if (process.getSerialNo() > currentSerialNo) {
                    process.setStatus("0"); // 未填报
                }
                trialTaskDetailProdMapper.updateTrialTaskDetailProd(process);
            }
            
            // 复用已解析的listInfo，不再重复解析
            listInfo.forEach(item -> {
                int qty;
                int shuntQty = 0;

                if (StringUtils.isNotEmpty(item.getTrialQuantity())) {
                    shuntQty = Integer.parseInt(item.getShuntQty());
                }
                int trialQty = 0;
                if (StringUtils.isNotEmpty(item.getTrialQuantity())) {
                    trialQty = Integer.parseInt(item.getTrialQuantity());
                }
                qty = trialQty - shuntQty;
                if (qty > 0) {
                    item.setTrialQuantity(qty + "");
                } else {
                    item.setTrialQuantity("0");
                }

            });
            for (TrialTaskDetailProd trialTaskDetailProd : listInfo) {
                TrialTaskDetailProd currentNode = new TrialTaskDetailProd();
                currentNode.setId(trialTaskDetailProd.getId());
                if (StringUtils.isNotEmpty(trialTaskDetailProd.getTrialQuantity())) {
                    currentNode.setTrialQuantity(trialTaskDetailProd.getTrialQuantity());
                }

                trialTaskDetailProdMapper.updateTrialTaskDetailProd(currentNode);
            }
        }
        return 1;
    }

    /**
     * 导出试制任务-零件流转卡信息
     *
     * @param id 试制任务信息主键
     */
    @Override
    public void exportPdf(Long id, HttpServletResponse response) throws Exception {
        TrialTaskProd trialTaskProd = trialTaskProdMapper.selectTrialTaskProdByTaskId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("carType", trialTaskProd.getCarType());
        map.put("assemblyName", trialTaskProd.getAssemblyName());
        map.put("assemblyFigure", trialTaskProd.getAssemblyFigure());
        map.put("pm", trialTaskProd.getPm());
        map.put("pe", trialTaskProd.getPe());
        map.put("qrCode", trialTaskProd.getQrCode());
        
        if (trialTaskProd.getQrCodeUrl() != null && trialTaskProd.getQrCodeUrl().contains(",")) {
            String base64Data = trialTaskProd.getQrCodeUrl().substring(trialTaskProd.getQrCodeUrl().indexOf(",") + 1);
            byte[] qrCodeImage = java.util.Base64.getDecoder().decode(base64Data);
            map.put("qrCodeImage", qrCodeImage);
        } else {
            map.put("qrCodeImage", null);
        }

        // 1、设置请求头类型
        response.setContentType("application/pdf");
        // 2、设置返回文件名字
        String fileName = System.currentTimeMillis() + trialTaskProd.getProjectName()  + "_试制任务信息报表.pdf";
        String encodedFileName = URLEncoder.encode(fileName, "utf-8");
        response.setHeader("content-disposition", "attachment;filename=" + encodedFileName);

        List<TrialTaskDetailProdPdfDTO> trialTaskDetailProdPdfDTOS = trialTaskDetailProdMapper.selectTrialTaskDetailProdPdfDTOByTaskId(id);

        try(InputStream inputStream = this.getClass().getResourceAsStream("/jasper/prod.jasper")){
            //填充数据---使用JavaBean数据源方式填充
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, map, new JRBeanCollectionDataSource(trialTaskDetailProdPdfDTOS));
            JasperExportManager.exportReportToPdfStream(jasperPrint, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    @Override
    public List<TrialTaskProd> selectTrialTaskProdByRelatedTaskId(Long relatedTaskId) {
        return trialTaskProdMapper.selectTrialTaskProdByRelatedTaskId(relatedTaskId);
    }

    @Override
    public int flowTrialTaskProd(TrialTaskProd trialTaskProd, String userId) {
        TrialTaskProd oldNode = selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());

        if ("2".equals(oldNode.getStatus())) {
            throw new RuntimeException("任务已结束，无法变更程序");
        }

        //先查询任务信息，复制一个新任务
        TrialTaskProd newNode = selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());
        newNode.setCurrentSerialNo(trialTaskProd.getCurrentSerialNo());
        newNode.setCurrentSerialName(trialTaskProd.getCurrentSerialName());

        //需要把当前环节为正在填报，前面环节为已审核，后面的程序环节变更为未填报
        changeTrialTaskProd(newNode);
        updateTrialTaskProd(trialTaskProd);
        com.ruoyi.common.entity.ActionLog actionLog = new com.ruoyi.common.entity.ActionLog();
        actionLog.setTaskId(trialTaskProd.getTaskId());
        actionLog.setActionType(2L);
        actionLog.setCreateBy(userId);
        actionLog.setCreateTime(com.ruoyi.common.utils.DateUtils.getTime());
        actionLogMapper.insert(actionLog);
        return 1;
    }

    @Override
    public void printTrialTaskProd(Long id, Long printType, String userId, HttpServletResponse response) throws Exception {
        TrialTaskProd task = selectTrialTaskProdByTaskId(id);
        if (task == null) {
            response.setContentType("text/html; charset=UTF-8");
            java.io.PrintWriter out = response.getWriter();
            out.println("<html>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>纸质流转卡打印</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div style='text-align: center; margin-top: 80px; font-size: 18px; color: #f56c6c;'>");
            out.println("<h2>未找到对应任务</h2>");
            out.println("<p>请检查任务ID是否正确，或联系管理员</p>");
            out.println("</div>");
            out.println("</body></html>");
            return;
        }

        // 记录打印日志
        com.ruoyi.trial.domain.PrintLog printLog = new com.ruoyi.trial.domain.PrintLog();
        printLog.setTaskId(id);
        printLog.setPrinttype(printType);
        printLog.setCreateBy(userId);
        printLog.setCreateTime(new java.util.Date());
        printLog.setStatus("1");
        printLogService.insertPrintLog(printLog);

        response.setContentType("text/html; charset=UTF-8");
        java.io.PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>纸质流转卡打印</title>");
        out.println("<style>");
        out.println("table { width: 100%; border-collapse: collapse; margin: 20px 0; }");
        out.println("th, td { border: 1px solid #000; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("h1, h2, h3 { text-align: center; }");
        out.println(".page-break { page-break-after: always; }");
        out.println(".print-type { text-align: right; margin-bottom: 10px; font-weight: bold; }");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");

        // 打印类型显示
        String printTypeDesc = "正常打印";
        if (printType == 2) {
            printTypeDesc = "丢失补打";
        } else if (printType == 3) {
            printTypeDesc = "分流打印";
        }
        out.println("<div class='print-type'>打印类型：" + printTypeDesc + "</div>");

        out.println("<h1>零件电子流转卡</h1>");
        out.println("<h2>任务名称：" + task.getTitle() + "</h2>");
        out.println("<table border='1' cellpadding='5' cellspacing='0'>");
        out.println("<tr><td>任务编号</td><td>" + task.getTaskId() + "</td></tr>");
        out.println("<tr><td>所属项目</td><td>" + task.getProjectName() + "</td></tr>");
        out.println("<tr><td>车型</td><td>" + task.getCarType() + "</td></tr>");
        out.println("<tr><td>总成名称</td><td>" + task.getAssemblyName() + "</td></tr>");
        out.println("<tr><td>总成图号</td><td>" + task.getAssemblyFigure() + "</td></tr>");
        out.println("<tr><td>试制管理员</td><td>" + task.getPm() + "</td></tr>");
        out.println("<tr><td>PE姓名</td><td>" + task.getPe() + "</td></tr>");
        out.println("<tr><td>当前程序</td><td>" + task.getCurrentSerialName() + "</td></tr>");
        out.println("<tr><td>状态</td><td>" + ("0".equals(task.getStatus()) ? "正常" : "停用") + "</td></tr>");
        out.println("<tr><td>二维码</td><td><img src='" + task.getQrCodeUrl() + "' width='100' height='100' style='vertical-align: middle;'/></td></tr>");
        out.println("</table>");

        // 打印流转程序详情

        out.println("<div style='page-break-before: always;'></div>");
        out.println("<h3>流转程序详情：</h3>");
        out.println("<table border='1' cellpadding='5' cellspacing='0'>");
        out.println("<tr><th>序号</th><th>程序<br>名称</th><th>名称</th><th>图号</th><th>试制<br>数量</th><th>送检<br>数量</th><th>制造<br>质量<br>确认</th><th>工艺<br>质量<br>确认</th><th>制造<br>区域</th><th>负责人</th><th>备注</th></tr>");

        // 查询任务详情
        com.ruoyi.trial.domain.TrialTaskDetailProd detail = new com.ruoyi.trial.domain.TrialTaskDetailProd();
        detail.setTaskId(id);
        List<com.ruoyi.trial.domain.TrialTaskDetailProd> details = trialTaskDetailProdMapper.selectTrialTaskDetailProdList(detail);
        for (int i = 0; i < details.size(); i++) {
            com.ruoyi.trial.domain.TrialTaskDetailProd d = details.get(i);
            out.println("<tr>");
            out.println("<td>" + (i + 1) + "</td>");
            out.println("<td>" + d.getProgram() + "</td>");
            out.println("<td>" + d.getName() + "</td>");
            out.println("<td>" + d.getFigure() + "</td>");
            out.println("<td>" + d.getTrialQuantity() + "</td>");
            out.println("<td>" + d.getInspectionQuantity() + "</td>");
            out.println("<td>" + d.getManufacturingQualityStatus() + "</td>");
            out.println("<td>" + d.getProcessQualityStatus() + "</td>");
            out.println("<td>" + d.getManufacturingArea() + "</td>");
            out.println("<td>" + d.getDirector() + "</td>");
            out.println("<td>" + d.getNotes() + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");

        out.println("<script>window.onload = function() { window.print(); }</script>");
        out.println("</body></html>");
    }

    @Override
    public int forkTrialTaskProdWithLog(TrialTaskProd trialTaskProd, String userId) {
        TrialTaskProd oldNode = selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());
        if ("2".equals(oldNode.getStatus())) {
            throw new RuntimeException("任务已结束，无法分流");
        }

        //先查询任务信息，复制一个新任务
        TrialTaskProd newNode = selectTrialTaskProdByTaskId(trialTaskProd.getTaskId());

        //需要复制一个任务
        newNode.setTaskId(null);
        copyTrialTaskProd(newNode);
        newNode.setListStr(trialTaskProd.getListStr());
        forkTrialTaskProd(newNode, trialTaskProd.getTaskId());
        ActionLog actionLog = new ActionLog();
        actionLog.setTaskId(trialTaskProd.getTaskId());
        actionLog.setActionType(1L);
        actionLog.setCreateBy(userId);
        actionLog.setCreateTime(DateUtils.getTime());
        actionLogMapper.insert(actionLog);
        return 1;
    }

    @Override
    public TrialTaskProd selectTrialTaskProdByQrCode(String qrCode) {
        return trialTaskProdMapper.selectTrialTaskProdByQrCode(qrCode);
    }

    @Override
    public int batchGenerateQrCode() {
        List<TrialTaskProd> taskList = trialTaskProdMapper.selectTrialTaskProdWithoutQrCode();
        int count = 0;
        for (TrialTaskProd task : taskList) {
            String qrCode = "TASK_" + task.getTaskId() + "_" + UUID.randomUUID().toString().substring(0, 8);
            String qrCodeBase64 = QRCodeUtil.generateQRCodeBase64(qrCode, 200, 200);
            task.setQrCode(qrCode);
            task.setQrCodeUrl(qrCodeBase64);
            trialTaskProdMapper.updateTrialTaskProd(task);
            count++;
        }
        return count;
    }
}

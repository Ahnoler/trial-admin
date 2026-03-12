package com.ruoyi.common.utils;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.export.HtmlExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleHtmlReportConfiguration;
import org.springframework.core.io.ClassPathResource;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Author: xyyz150
 * Date: 2020/4/9 19:40
 * Description:
 */
@Slf4j
public class JasperReportUtil {
    final static String jasperDir = "jaspers";

    public static String getJasperFileDir(String fileName) {
        return jasperDir + File.separator + fileName + ".jasper";
    }

    private static String getContentType(ReportType type) {
        String contentType;
        switch (type) {
            case HTML:
                contentType = "text/html;charset=utf-8";
                break;
            case PDF:
                contentType = "application/pdf";
                break;
            case XLS:
                contentType = "application/vnd.ms-excel";
                break;
            case XLSX:
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
                break;
            case XML:
                contentType = "text/xml";
                break;
            case RTF:
                contentType = "application/rtf";
                break;
            case CSV:
                contentType = "text/plain";
                break;
            case DOC:
                contentType = "application/msword";
                break;
            default:
                contentType = "text/html;charset=utf-8";
        }
        return contentType;
    }

    static JasperPrint getJasperPrint(InputStream jasperStream, Map parameters, List<?> list) throws JRException {
        JRBeanCollectionDataSource dataSource = null;
        if (null == list || list.size() == 0) {
            dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
        } else {
            dataSource = new JRBeanCollectionDataSource(list);
        }

        JasperPrint jasperPrint = null;
        DefaultJasperReportsContext context = DefaultJasperReportsContext.getInstance();
        JRPropertiesUtil.getInstance(context).setProperty("net.sf.jasperreports.xpath.executer.factory", "net.sf.jasperreports.engine.util.xml.JaxenXPathExecuterFactory");
        jasperPrint = JasperFillManager.fillReport(jasperStream, parameters, dataSource);
        return jasperPrint;
    }

    public static void exportToPdf(String jasperPath, Map parameters, List<?> list, HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            response.setContentType(getContentType(ReportType.PDF));
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);
            JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
        } catch (Exception e) {
            log.error("读取报表异常", e);
            outputStream.write("读取报表异常".getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }


    public static void exportToXml(String jasperPath, Map parameters, List<?> list, HttpServletResponse response) throws Exception {
        OutputStream outputStream = response.getOutputStream();
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            response.setContentType(getContentType(ReportType.XML));
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);
            JasperExportManager.exportReportToXmlStream(jasperPrint, outputStream);
        } catch (Exception e) {
            outputStream.write("读取报表异常".getBytes());
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }

    public static void exportToHtml(String jasperPath, Map parameters, List<?> list, HttpServletResponse response) throws Exception {
        response.setHeader("Content-type", "text/html;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType(getContentType(ReportType.HTML));
        PrintWriter out = response.getWriter();
        HtmlExporter exporter = new HtmlExporter();
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);


            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            SimpleHtmlReportConfiguration configuration = new SimpleHtmlReportConfiguration();
            exporter.setConfiguration(configuration);

            HtmlExporterOutput outPut = new SimpleHtmlExporterOutput(out);
            exporter.setExporterOutput(outPut);

            exporter.exportReport();
        } catch (Exception e) {
            log.error("读取报表异常", e);
            out.write("读取报表异常");
        } finally {
            out.flush();
            out.close();
        }
    }


    /**
     * 发送打印任务至打印机
     *
     * @param jasperPath
     * @param parameters
     * @param list
     * @throws Exception
     */
    public static void exportToPdfByPrinter(String jasperPath, Map parameters, List<?> list) throws Exception {
        try {
            ClassPathResource resource = new ClassPathResource(jasperPath);
            InputStream jasperStream = resource.getInputStream();
            JasperPrint jasperPrint = getJasperPrint(jasperStream, parameters, list);


            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            DocFlavor flavor = DocFlavor.BYTE_ARRAY.PNG;
            PrintService[] printServices = PrintServiceLookup.lookupPrintServices(flavor, pras);
            for (PrintService printService : printServices) {
                System.out.println(printService.getName());
            }
            //demo默认获取第一个打印机进行打印，实际业务中可以选择所需要的打印机，
            PrintService printService = printServices[0];

            JRAbstractExporter je = new JRPrintServiceExporter();
            je.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);

            //设置指定打印机
            je.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE, printService);
            je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, false);
            je.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, false);

            //打印
            je.exportReport();
            jasperStream.close();
        } catch (Exception e) {
            log.error("读取报表异常", e);
        }
    }


    public enum ReportType {
        HTML,
        PDF,
        XLS,
        XLSX,
        XML,
        RTF,
        CSV,
        DOC
    }
}


package com.ruoyi.common.utils.poi;

import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.ClassUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class PdfUtil {
    /**
     * 导出PDF 文件
     * @param temPath //模板路径
     * @param pdfName 导出文件名称
     * @param response 响应
     * @param map 数据
     */
    public  void  exportPdf(String temPath, String pdfName, ResourceLoader resourceLoader,
                                  HttpServletResponse response, HttpServletRequest request, Map<String, Object> map){
        try {

            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                //IE浏览器处理
                pdfName = java.net.URLEncoder.encode(pdfName, "UTF-8");
            } else {
                // 非IE浏览器的处理：
                pdfName = new String(pdfName.getBytes("UTF-8"), "ISO-8859-1");
            }
            InputStream in = this.getClass().getResourceAsStream("/test_A4.jrxml");
            JasperReport jasperReport = JasperCompileManager.compileReport(in);
            JasperPrint print = JasperFillManager.fillReport(jasperReport,map,new JREmptyDataSource());
            response.setContentType("application/x-download");
            response.setHeader("Content-Disposition", String.format("attachment; filename=\""+pdfName+".pdf\""));
            OutputStream out = response.getOutputStream();
            JasperExportManager.exportReportToPdfStream(print,out);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

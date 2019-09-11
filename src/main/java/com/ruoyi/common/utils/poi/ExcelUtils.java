package com.ruoyi.common.utils.poi;

import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.config.RuoYiConfig;
import com.ruoyi.framework.web.domain.AjaxResult;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * 下载excel工具类
 * @ProjectName deviceManage
 * @PackageName com.ruoyi.common.utils.poi
 * @Author: Administrator
 * @Date: 2019/5/24 9:25
 * @Description //TODO
 * @Version: 1.0
 **/
public class ExcelUtils {

    private static final Logger log = LoggerFactory.getLogger(ExcelUtils.class);

    public static Workbook createWorkbook(){
        return new SXSSFWorkbook(800);
    }
    /**
     * 创建工作表
     */
    public static Sheet createSheet(Workbook workbook)
    {
        return workbook.createSheet();
    }

    /**
     * 编码文件名
     */
    public static String encodingFilename(String filename)
    {
        filename = System.currentTimeMillis() + "_" + filename + ".xlsx";
        return filename;
    }

    /**
     * 获取下载路径
     *
     * @param filename 文件名称
     */
    public static String getAbsoluteFile(String filename)
    {
        String downloadPath = RuoYiConfig.getDownloadPath() + filename;
        File desc = new File(downloadPath);
        if (!desc.getParentFile().exists())
        {
            desc.getParentFile().mkdirs();
        }
        return downloadPath;
    }

    public static AjaxResult getAjaxResult(Workbook wb, String fileName) {
        OutputStream out =null;
        try {
            out = new FileOutputStream(ExcelUtils.getAbsoluteFile(fileName));
            wb.write(out);
            return AjaxResult.success(fileName);
        }catch (Exception e){
            log.error("导出Excel异常{}", e.getMessage());
            throw new BusinessException("导出Excel失败，请联系网站管理员！");
        }finally {
            try {
                if(wb != null){
                    wb.close();
                }
                if(out != null){
                    out.close();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}

package com.lemon17.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon17.constants.Constants;
import com.lemon17.pojo.CaseInfo;
import com.lemon17.pojo.WriteBackData;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: YiBin
 * @Description: 读excel工具类
 * @Date: Created in 下午 01:47 20/05/15
 * @Modified By:
 */
public class ExcelUtils {
    public static void main(String[] args) {
        Object[][] read = read(1, 1, CaseInfo.class);
    }

    //存储excel回写对象的集合
    public static List<WriteBackData> wbdList = new ArrayList<>();



    /**
     * 批量回写excel（修改）方法
     */
    public static void backWrite(){
        //定义流
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            //初始化输入流对象
            fis = new FileInputStream(Constants.EXCEL_PATH);
            //获取excel对象
            Workbook excel = WorkbookFactory.create(fis);
            //循环wbdList
            for (WriteBackData wbd : wbdList){
                int sheetIndex = wbd.getSheetIndex();
                int rowNum = wbd.getRowNum();
                int cellNum = wbd.getCellNum();
                String content = wbd.getContent();
                //获取sheet
                Sheet sheet = excel.getSheetAt(sheetIndex);
                //获取row
                Row row = sheet.getRow(rowNum);
                //获取cell
                Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
                //修改cell的值
                cell.setCellValue(content);
                //回写到excel中
                fos = new FileOutputStream(Constants.EXCEL_PATH);
                excel.write(fos);
            }
        } catch (IOException e) {
            e.printStackTrace();
            //关流
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    /**
     * 读excel的方法
     * @return
     */
    public static <T> Object[][] read(int startSheetIndex, int sheetNum, Class<T> clazz){
        Object[][] datas = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(Constants.EXCEL_PATH);//读取常量类中的常量
            //excel导入配置
            ImportParams importParams = new ImportParams();
            //从startSheetIndex位置开始读sheet
            importParams.setStartSheetIndex(startSheetIndex);
            //读几个sheet
            importParams.setSheetNum(sheetNum);
            //获取CaseInfo类实例的list
            List<T> list = ExcelImportUtil.importExcel(fis, clazz, importParams);

            //把list中的对象存入到datas二维数组中
            datas = new Object[list.size()][1];
            for (int i = 0; i < list.size(); i++) {
                datas[i][0] = list.get(i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return datas;
    }

}

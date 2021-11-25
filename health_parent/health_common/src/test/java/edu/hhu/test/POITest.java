package edu.hhu.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * POI入门案例
 * POI : Java程序对Microsoft Office格式档案读和写的功能 主要操作Excel文件
 * HSSF － 提供读写Microsoft Excel XLS格式档案的功能
 * XSSF － 提供读写Microsoft Excel OOXML XLSX格式档案的功能
 */
public class POITest {
    /**
     * 测试 对excel文件内容的读取01
     */
    @Test
    public void test01() throws IOException {
        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("D:\\TestResources\\HHU_Health\\Hello.xlsx");
        //获取工作表(sheet1,sheet2)
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //获取每一行
        for (Row row : sheet) {
            //获取每一行每一列的单元格
            for (Cell cell : row) {
                System.out.println(cell.getStringCellValue());//获取每个单元格的字符串数据
            }
        }
        xssfWorkbook.close();
    }

    /**
     * 测试 对excel文件内容的读取02
     */
    @Test
    public void test02() throws IOException {
        //创建工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("D:\\TestResources\\HHU_Health\\Hello.xlsx");
        //获取工作表(sheet1,sheet2)
        XSSFSheet sheet = xssfWorkbook.getSheetAt(0);
        //获取最后一行行号
        int lastRowNum = sheet.getLastRowNum();//行号从0开始
        System.out.println(lastRowNum);
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取行对象
            XSSFRow row = sheet.getRow(i);
            //获取对应的列数
            short lastCellNum = row.getLastCellNum();//获得列数
            System.out.println(lastCellNum);
            for (int j = 0; j < lastCellNum; j++) {
                System.out.println(row.getCell(j).getStringCellValue());
            }
        }
        xssfWorkbook.close();
    }

    /**
     * 测试 向excel表格中写入数据
     */
    @Test
    public void test03() throws IOException {
        //在内存中创建一个工作簿
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //创建工作表,并指定名称
        XSSFSheet sheet = xssfWorkbook.createSheet("HHU_Health");
        //创建第一行
        XSSFRow titleRow = sheet.createRow(0);
        //创建标题单元格
        titleRow.createCell(0).setCellValue("姓名");
        titleRow.createCell(1).setCellValue("地址");
        titleRow.createCell(2).setCellValue("年龄");
        //创建第二行
        XSSFRow DataRow01 = sheet.createRow(1);
        //创建数据单元格并写入数据
        DataRow01.createCell(0).setCellValue("张三");
        DataRow01.createCell(1).setCellValue("北京");
        DataRow01.createCell(2).setCellValue(20);
        //写回磁盘
       FileOutputStream  out= new FileOutputStream("D:\\TestResources\\HHU_Health\\test01.xlsx");
       xssfWorkbook.write(out);
       out.flush();
       out.close();
       xssfWorkbook.close();
    }
}

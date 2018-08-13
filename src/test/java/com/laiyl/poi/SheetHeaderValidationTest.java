package com.laiyl.poi;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SheetHeaderValidationTest {
    public static void main(String[] args) throws IOException {

        FileInputStream fileTemp = new FileInputStream("D:\\develop\\project\\orderImportTemplate.xls");
        FileInputStream fileImport = new FileInputStream("D:\\develop\\project\\orderImport.xls");

        HSSFWorkbook workbook = new HSSFWorkbook(fileTemp);
        HSSFWorkbook workbook2 = new HSSFWorkbook(fileImport);

        HSSFSheet tempSheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> tempHeader = new HashMap<Integer,List<String>>();
        readHeader(tempSheet,tempHeader,3);



        Map<Integer, List<String>> importHeader = new HashMap<>();
        HSSFSheet importSheet = workbook2.getSheetAt(0);
        readHeader(importSheet,importHeader,3);

        boolean flag = true;
        for (int i = 0; i < 3; i++) {
            List<String> tem = tempHeader.get(i);
            List<String> imp = importHeader.get(i);
            if (tem.size() == imp.size()) {

                for (int j = 0,len = tem.size(); j < len; j++) {
                    if (!tem.get(j).equals(imp.get(j))) {
                        flag = false;
                        break;
                    }
                }
            } else {
                flag = false;
            }

            if (!flag) {
                break;
            }
        }

        if (!flag) {
            System.out.println("请使用下载的模板导入");
        } else {
            System.out.println("模板正确");
            System.out.printf("");
        }

        fileTemp.close();
        fileImport.close();


    }

    private static void readHeader(HSSFSheet sheet, Map map, int headerRows) {
        List<String> row;
        for (int i = 0; i < headerRows; i++) {
            row = new ArrayList<>();
            HSSFRow r = sheet.getRow(i);
            short lastCellNum = r.getLastCellNum();
            int t = 0;
            while (t < lastCellNum) {
                HSSFCell cell = r.getCell(t);
                if (cell == null || HSSFCell.CELL_TYPE_BLANK == cell.getCellType()) {
                    t++;
                    continue;
                } else {
                    row.add(cell.getRichStringCellValue().toString());
                }
                t++;
            }

            map.put(i,row);
        }
    }
}

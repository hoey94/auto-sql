package com.paic.util;

import cn.hutool.poi.excel.ExcelUtil;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PhoenixSqlGenerator implements SqlGenerator {

    public String generate(String dirPath, String filePath, JLabel label) {
        if(label != null){
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        java.util.List<java.util.List<Object>> read = ExcelUtil.getReader(dirPath + filePath).read();
        java.util.List<Object> tableNameList = read.get(0);
        java.util.List<Object> columnClusterList = read.get(1);
        List<Object> colList1 = new ArrayList<Object>();
        colList1.add("");
        List<Object> colList2 = new ArrayList<Object>();
        colList2.add("PK");

        for (int i = 1; i < columnClusterList.size(); i++) {
            String[] split = columnClusterList.get(i).toString().split(":");
            colList1.add(split[0]);
            colList2.add(split[1]);
        }

        java.util.List<java.util.List<Object>> dataList = read.subList(2, read.size());

        // 循环的次数
        int cycNumber = columnClusterList.size();
        StringBuffer upsertIntoSql = new StringBuffer();
        StringBuffer createTableSql = new StringBuffer();
        createTableSql.append("CREATE TABLE IF NOT EXISTS \""+tableNameList.get(0)+"\"( \r\n");
        StringBuffer createViewSql = new StringBuffer();
        createViewSql.append("CREATE VIEW \""+tableNameList.get(0)+"\"( \r\n");
        StringBuffer result = new StringBuffer();
        for (List<Object> objects : dataList) {
            String colName = "(";
            String colValue = "(";

            for (int i = 0; i < cycNumber; i++) {
                if(i == 0 && createTableSql.indexOf(";") == -1){
                    createTableSql.append(colList2.get(i) + " VARCHAR NOT NULL PRIMARY KEY");
                }
                if(i != 0 && createTableSql.indexOf(";") == -1){
                    createTableSql.append(colList1.get(i) +"."+ colList2.get(i) +" VARCHAR");
                }

                if(i == 0 && createViewSql.indexOf(";") == -1){
                    createViewSql.append(colList2.get(i) + " VARCHAR NOT NULL PRIMARY KEY");
                }
                if(i != 0 && createViewSql.indexOf(";") == -1){
                    createViewSql.append(colList1.get(i) +"."+ colList2.get(i) +" VARCHAR");
                }


                colName += colList2.get(i);
                colValue += "'"+objects.get(i)+"'";
                if(i != cycNumber-1){
                    colName += ",";
                    colValue += ",";
                    if(createTableSql.indexOf(";") == -1){
                        createTableSql.append(",\r\n");
                    }
                    if(createViewSql.indexOf(";") == -1){
                        createViewSql.append(",\r\n");
                    }

                }else{
                    colName += ")";
                    colValue += ")";
                    if(createTableSql.indexOf(";") == -1){
                        createTableSql.append("\r\n);");
                    }
                    if(createViewSql.indexOf(";") == -1){
                        createViewSql.append("\r\n) AS SELECT * FROM \""+tableNameList.get(0)+"\";");
                    }
                }

            }
            upsertIntoSql.append("UPSERT INTO \""+tableNameList.get(0)+"\"" + colName + " VALUES" + colValue + ";\r\n");
        }
        //System.out.println(createTableSql.toString());
        //System.out.println(createViewSql.toString());
        result.append("-- 创建表\r\n");
        result.append(createTableSql.toString() + "\r\n\r\n");
        result.append("-- 创建视图\r\n");
        result.append(createViewSql.toString() + "\r\n\r\n");
        result.append("-- 插入数据\r\n");
        result.append(upsertIntoSql.toString());
        if(label != null){
            label.setText("解析成功");
        }
        return result.toString();

    }

    public static void main(String[] args) {
        System.out.println(new PhoenixSqlGenerator().generate("/Users/zyh/Documents/","hbase.xlsx",null));
    }
}

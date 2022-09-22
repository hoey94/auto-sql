package com.paic.core;

import cn.hutool.poi.excel.ExcelUtil;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * @author zyh
 * @Description:
 * @date 2021/9/29:22 上午
 */
public class HiveSqlGenerator implements Generator{


    @Override
    public String generate(String dirPath, String filePath, JLabel label) {

        if(label != null){
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        List<List<Object>> read = ExcelUtil.getReader(dirPath + filePath).read();
        List<Object> tableNameList = read.get(0);
        List<List<Object>> dataList = read.subList(1, read.size()).stream().filter(o-> !o.isEmpty()).collect(Collectors.toList());


        StringBuffer stringBuffer = new StringBuffer();
        // 拼接创建语句
        // 拼接文件名
        stringBuffer.append("DDL_airqecode_" + tableNameList.get(0) + ".sql");
        stringBuffer.append("\n");
        stringBuffer.append("CREATE TABLE  IF NOT EXISTS ods.`airqrcode_"+tableNameList.get(0)+"`(");
        stringBuffer.append("\n");
        for (int i = 0; i < dataList.size(); i++) {
            String colName = dataList.get(i).get(0).toString().trim();
            String type = dataList.get(i).get(1).toString().trim();
            String line = colName + " " + type;
            if(i != 0){
                line = "," +line;
            }
            stringBuffer.append(line);
            stringBuffer.append("\n");
        }
        stringBuffer.append(")PARTITIONED BY (`year` string,`month` string,`day` string) stored as PARQUET;");
        // 生成建表语句
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");
        stringBuffer.append("\n");

        // 拼接insert语句
        stringBuffer.append("airqrcode_"+tableNameList.get(0) + ".sql");
        stringBuffer.append("\n");
        stringBuffer.append("set hive.exec.dynamic.partition=true;");
        stringBuffer.append("\n");
        stringBuffer.append("set hive.exec.dynamic.partition.mode=nonstrict;");
        stringBuffer.append("\n");
        stringBuffer.append("set hive.exec.max.dynamic.partitions=50000;");
        stringBuffer.append("\n");
        stringBuffer.append("set hive.exec.max.dynamic.partitions.pernode=10000;");
        stringBuffer.append("\n");
        stringBuffer.append("insert overwrite table ods.airqrcode_"+tableNameList.get(0)+" partition(year, month , day)");
        stringBuffer.append("\n");
        stringBuffer.append("select");
        stringBuffer.append("\n");

        for (int i = 0; i < dataList.size(); i++) {
            String colName = dataList.get(i).get(0).toString().trim();
            if(i != 0){
                colName = "," +colName;
            }

            stringBuffer.append(colName);
            stringBuffer.append("\n");
        }
        stringBuffer.append(",year");
        stringBuffer.append("\n");
        stringBuffer.append(",month");
        stringBuffer.append("\n");
        stringBuffer.append(",day");
        stringBuffer.append("\n");
        stringBuffer.append("from airqrcode."+tableNameList.get(0)+" t ;");
        stringBuffer.append("\n");


        if(label != null){
            label.setText("解析成功");
        }
        return stringBuffer.toString();

    }

    public static void main(String[] args) {
        HiveSqlGenerator generator = new HiveSqlGenerator();
        System.out.println(generator.generate("/Users/zyh/workspace/autoTool/", "hiveCols.xlsx", null));

    }
}

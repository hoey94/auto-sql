package com.paic.util;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HbaseSqlGenerator implements SqlGenerator {

    public String generate(String dirPath, String filePath, JLabel label) {
        if(label != null){
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        List<List<Object>> read = ExcelUtil.getReader(dirPath + filePath).read();
        List<Object> tableNameList = read.get(0);
        List<Object> columnClusterList = read.get(1);
        List<List<Object>> dataList = read.subList(2, read.size());

        // 循环的次数
        int cycNumber = columnClusterList.size()-1;

        StringBuffer stringBuffer = new StringBuffer();
        for (List<Object> objects : dataList) {
            Object rowKeyValue = objects.get(0);
            for (int i = 1; i < cycNumber + 1; i++) {
                // 跳过第一个值，直接从第二个值开始取
                if(label != null){
                    label.setText("生成值:" + "put '" + tableNameList.get(0) + "','" + rowKeyValue + "','" + columnClusterList.get(i) + "','" + objects.get(i) + "'");
                }
                stringBuffer.append("put '" + tableNameList.get(0) + "','" + rowKeyValue + "','" + columnClusterList.get(i) + "','" + objects.get(i) + "'\r\n");
            }
        }
        if(label != null){
            label.setText("解析成功");
        }
        return stringBuffer.toString();

    }

    public static void main(String[] args) {
        new HbaseSqlGenerator().generate("/Users/zyh/Documents/","hbase.xlsx",null);
    }
}

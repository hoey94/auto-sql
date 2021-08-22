package com.paic.core;

import cn.hutool.poi.excel.ExcelUtil;

import javax.swing.*;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class HbaseGenerator implements Generator {

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
        Set<String> set = new TreeSet<>();
        for (List<Object> objects : dataList) {
            Object rowKeyValue = objects.get(0);
            for (int i = 1; i < cycNumber + 1; i++) {
                String ccl = columnClusterList.get(i).toString();
                if(!ccl.equals("PK")){
                    set.add(ccl.split(":")[0]);
                }

                // 跳过第一个值，直接从第二个值开始取
                if(label != null){
                    label.setText("生成值:" + "put '" + tableNameList.get(0) + "','" + rowKeyValue + "','" + columnClusterList.get(i) + "','" + objects.get(i) + "'");
                }
                stringBuffer.append("put '" + tableNameList.get(0) + "','" + rowKeyValue + "','" + columnClusterList.get(i) + "','" + objects.get(i) + "'\r\n");
            }
        }

        stringBuffer.append("create '"+tableNameList.get(0)+"',");
        for(int i = 0 ; i < set.size() ; i++){
            String s = "";
            if(i == set.size() - 1){
                s = "{NAME => '"+set.toArray()[i]+"', VERSIONS => 1}";
            }else{
                s = "{NAME => '"+set.toArray()[i]+"', VERSIONS => 1},";
            }
            stringBuffer.append(s);
        }

        // 生成建表语句
        stringBuffer.append("\n");

        if(label != null){
            label.setText("解析成功");
        }
        return stringBuffer.toString();

    }

    public static void main(String[] args) {
        new HbaseGenerator().generate("/Users/zyh/Documents/","hbase.xlsx",null);
    }
}

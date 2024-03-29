package com.paic.core;

import cn.hutool.poi.excel.ExcelUtil;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zyh
 * @Description:
 * @date 2021/8/226:44 下午
 */
public class KafkaCommaProducter implements Generator {

    @Override
    public String generate(String dirPath, String filePath, JLabel label) {

        if(label != null){
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        List<List<Object>> read = ExcelUtil.getReader(dirPath + filePath).read();
        List<List<Object>> dataList = read.subList(1, read.size());

        StringBuffer sb = new StringBuffer();

        for (List<Object> row : dataList) {
            sb.append(row.stream().map(o->o.toString()).collect(Collectors.joining(",")) + "\n");
        }

        if(label != null){
            label.setText("解析成功");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new KafkaCommaProducter().generate("/Users/zyh/workspace/autoTool/", "kafka.xlsx", null));
    }


}
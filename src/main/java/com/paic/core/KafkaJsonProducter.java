package com.paic.core;

import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.fastjson.JSONObject;

import javax.swing.*;
import java.util.List;

/**
 * @author zyh
 * @Description:
 * @date 2021/8/226:44 下午
 */
public class KafkaJsonProducter implements Generator {

    @Override
    public String generate(String dirPath, String filePath, JLabel label) {

        if(label != null){
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        List<List<Object>> read = ExcelUtil.getReader(dirPath + filePath).read();
        List<Object> cols = read.get(0);
        List<List<Object>> dataList = read.subList(1, read.size());

        StringBuffer sb = new StringBuffer();

        for (List<Object> row : dataList) {
            JSONObject object = new JSONObject();
            for (int i = 0; i < cols.size(); i++) {
                Object col = cols.get(i);
                Object data = row.get(i);
                object.put(col.toString(),data);
            }
            sb.append(object.toJSONString());
            sb.append("\n");
        }


        if(label != null){
            label.setText("解析成功");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new KafkaJsonProducter().generate("/Users/zyh/workspace/autoTool/", "kafka.xlsx", null));
    }


}
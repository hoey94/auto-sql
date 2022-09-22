package com.paic.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.ExcelUtil;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zyh
 * @Description:
 * @date 2021/9/63:25 下午
 */
public class HiveArchiveGenerator implements Generator {

    private static List<String> lists = new ArrayList<>();


    /**
     * 拼接 ALTER TABLE test.airqrcode_test_hive_archive ARCHIVE PARTITION(year='2021',month='',);
     * @return
     */
    @Override
    public String generate(String dirPath, String filePath, JLabel label) {

        if(label != null){
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        List<List<Object>> read = ExcelUtil.getReader(dirPath + filePath).read();
        String dbAndTableName = ((String) read.get(0).get(0)).trim();
        List<List<Object>> dataList = read.subList(1, read.size()).stream().filter(o-> !o.isEmpty()).collect(Collectors.toList());

        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("set hive.archive.enabled=true;\n" +
                "set hive.archive.har.parentdir.settable=true;\n" +
                "set har.partfile.size=1099511627776;\n");

        // 拼接创建语句
        for (int i = 0; i < dataList.size(); i++) {
            String partitionStr = getPartitionStr(dataList.get(i));
            if(lists.contains(partitionStr)){
                continue;
            }else{
                lists.add(partitionStr);

                stringBuffer.append("ALTER TABLE "+dbAndTableName+" ARCHIVE PARTITION("+partitionStr+");");
                stringBuffer.append("\n");
            }
        }

        if(label != null){
            label.setText("解析成功");
        }
        FileUtil.writeBytes(stringBuffer.toString().getBytes(),new File("/Users/zyh/workspace/Archive/airqrcode/script/archive"));
        return stringBuffer.toString();
    }

    private String getPartitionStr(List<Object> objects) {
        String str = (String) objects.get(0);
        String[] arr = str.split("/");
        String year=arr[0];
        String month=arr[1];
        String day=arr[2];
        return year + "," + month + "," + day;
    }

    public static void main(String[] args) {
        String rootPath = "/Users/zyh/Downloads";
        List<String> strings = FileUtil.listFileNames(rootPath);
        for (String string : strings) {
            System.out.println(string);
            if(string.startsWith("query-hive")){
                run(rootPath,string);
            }
        }
    }

    public static void run(String rootPath, String fileName){
        HiveArchiveGenerator generator = new HiveArchiveGenerator();
        System.out.println(generator.generate(rootPath + "/", fileName, null));

    }

}

package com.paic.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.paic.util.FileUtils;
import com.paic.util.ParseUtil;
import com.paic.util.Tuple2;
import com.paic.util.VelocityUtils;

import javax.swing.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zyh
 * @Description:
 * @date 2022/9/2209:58
 */
public class CK2MysqlGenerator implements Generator{

    @Override
    public String generateCK2Mysql(String[] params, JLabel label) {
        String dirPath = params[0];
        String filePath = params[1];
        String sourceyu = params[2];
        String targetyu = params[3];
        String pks = params[4];

        if(label != null){
            System.out.println(dirPath + filePath);
            label.setText("开始加载文件:" + dirPath + filePath);
        }
        String result = null;
        try {
            List<String> ckSQL = FileUtil.readLines(dirPath + filePath, Charset.defaultCharset());
            //  顶格写
            String firstLine = getFirstLine(ckSQL);

            // ck表名
            Tuple2<String, String> ckTableName = ParseUtil.getCKTableName(firstLine, sourceyu, targetyu);
            // mysql表名
            String tableMysqlName = ParseUtil.getMysqlTableName(firstLine);
            // ck源表名
            String tableName = ParseUtil.getTableName(firstLine);
            // ck源列
            List<Tuple2<String, String>> columns = ParseUtil.getColums(ckSQL);

            if(firstLine.toUpperCase().contains("VIEW")){
                result = dealView(columns, ckTableName, tableMysqlName, tableName, pks, targetyu, sourceyu);
            }else if(firstLine.toUpperCase().contains("TABLE")){
                result = dealTable(columns, ckTableName, tableMysqlName, tableName, pks, targetyu, sourceyu);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String dealTable(List<Tuple2<String, String>> columns, Tuple2<String, String> tableCKName, String tableMysqlName, String tableName, String pks, String targetyu, String sourceyu) {

        StringBuffer sb = new StringBuffer();
        String ckDDL = getCKDDL(tableCKName.getB(), columns, targetyu, tableMysqlName);
        String mysqlDDL = getMYSQLDDL(tableMysqlName, columns, pks);
        String execShell = getExecShell(tableCKName,tableName, columns);
        sb.append(ckDDL + "\n");
        sb.append(mysqlDDL + "\n");
        String writeDestTableName = null;
        if(tableCKName.getB().contains(".")){
            writeDestTableName = "export_" + tableCKName.getB().replaceAll("\\.","_") + ".sh";
            sb.append("-- export_" + tableCKName.getB().replaceAll("\\.","_") + ".sh\n");
        }else{
            writeDestTableName = "export_ads_" + tableCKName + ".sh";
            sb.append("-- export_ads_" + tableCKName + ".sh\n");
        }
        sb.append(execShell + "\n");

        FileUtils.writeCKDDL(ckDDL, sourceyu, writeDestTableName);
        FileUtils.writeMysqlDDL(mysqlDDL, sourceyu, writeDestTableName);
        FileUtils.writeExportShell(execShell, sourceyu, writeDestTableName);

        return sb.toString();
    }


    /**
     * 解析视图sql
     * @param columns
     */
    private String dealView(List<Tuple2<String, String>> columns, Tuple2<String, String> tableCKName, String tableMysqlName, String tableName, String pks, String targetyu, String sourceyu) {
        StringBuffer sb = new StringBuffer();
        String ckDDL = getCKDDL(tableCKName.getB(), columns, targetyu, tableMysqlName);
        String mysqlDDL = getMYSQLDDL(tableMysqlName, columns, pks);
        String execShell = getExecShell(tableCKName, tableName, columns);
        sb.append(ckDDL + "\n");
        sb.append(mysqlDDL + "\n");
        String writeDestTableName = null;
        if(tableCKName.getB().contains(".")){
            writeDestTableName = "export_" + tableCKName.getB().replaceAll("\\.","_") + ".sh";
            sb.append("-- export_" + tableCKName.getB().replaceAll("\\.","_") + ".sh\n");
        }else{
            writeDestTableName = "export_ads_" + tableCKName + ".sh";
            sb.append("-- export_ads_" + tableCKName + ".sh\n");
        }
        sb.append(execShell + "\n");

        FileUtils.writeCKDDL(ckDDL, sourceyu, writeDestTableName);
        FileUtils.writeMysqlDDL(mysqlDDL, sourceyu, writeDestTableName);
        FileUtils.writeExportShell(execShell, sourceyu, writeDestTableName);

        return sb.toString();
    }

    /**
     * 处理视图中的 execshell
     * @param tableCKName
     * @param colums
     * @return
     */
    private String getExecShell(Tuple2<String, String> tableCKName, String tableName, List<Tuple2<String, String>> colums) {
        StringBuffer sb = new StringBuffer();
        sb.append("#!/bin/bash\n" +
                "\n" +
                "target_table_name=\""+tableCKName.getB()+"\"\n" +
                "source_table_name=\""+tableName+"\"\n" +
                "\n" +
                "opts=$@\n" +
                "getParam(){\n" +
                "  arg=$1\n" +
                "  echo $opts | xargs -n1 | cut -b 2- | awk -F'=' '{if($1==\"'\"$arg\"'\") print $2}'\n" +
                "}\n" +
                "\n" +
                "#统一调度平台参数获取\n" +
                "azkaban_flow_start_timestamp=`getParam azkaban_flow_start_timestamp`\n" +
                "run_date=`date -d \"${azkaban_flow_start_timestamp}\" '+%Y-%m-%d %H:%M:%S'`\n" +
                "echo \"[INFO] run_date: \"${run_date}\n" +
                "\n" +
                "run_year=`getParam azkaban_flow_start_year`\n" +
                "echo \"[INFO] run_year: \"${run_year}\n" +
                "\n" +
                "run_month=`getParam azkaban_flow_start_month`\n" +
                "echo \"[INFO] run_month: \"${run_month}\n" +
                "\n" +
                "run_day=`getParam azkaban_flow_start_day`\n" +
                "echo \"[INFO] run_day: \"${run_day}\n" +
                "\n" +
                "mysql_host=`getParam mysql_host`\n" +
                "echo \"[INFO] mysql_host: \"${mysql_host}\n" +
                "\n" +
                "mysql_username=`getParam mysql_username`\n" +
                "echo \"[INFO] mysql_username: \"${mysql_username}\n" +
                "\n" +
                "mysql_password=`getParam mysql_password`\n" +
                "echo \"[INFO] mysql_password: ***\"\n" +
                "\n" +
                "ck_host=`getParam ck_host`\n" +
                "echo \"[INFO] ck_host: \"${ck_host}\n" +
                "\n" +
                "ck_username=`getParam ck_username`\n" +
                "echo \"[INFO] ck_username: \"${ck_username}\n" +
                "\n" +
                "ck_password=`getParam ck_password`\n" +
                "echo \"[INFO] ck_password: ***\"\n" +
                "\n" +
                "\n" +
                "ct=$(clickhouse-client -u ${ck_username} --password ${ck_password} -h ${ck_host} -m --multiquery -q\"select count(*) as ct from ${source_table_name} --where toDate(UPDATE_TIME) >= toDate('${run_date}');\")\n" +
                "echo \"获得数据量: ${ct}\"\n" +
                "\n" +
                "for((i=1;i<=${ct};i+=10000))\n" +
                "do\n" +
                "  start=${i}\n" +
                "  end=$[i+10000]\n" +
                "  echo \"当前条数${start},10000\"\n" +
                "\n" +
                "  ck_sql1=\"\n" +
                "    insert into ${target_table_name}\n" +
                "    select\n" );

        for (int i = 0; i < colums.size(); i++) {
            if(colums.size()-1 == i){
                sb.append(colums.get(i).getA() +"\n");
            }else{
                sb.append(colums.get(i).getA() + ",\n");
            }
        }
        if(!tableCKName.getA().toUpperCase().equals("DWD") && !tableCKName.getA().toUpperCase().equals("ADS")){
            sb.append(
                    "    from ${source_table_name}\n" +
                            "    --where toDate(GMT_MODIFIED) >= toDate('${run_date}')\n" +
                            "    limit ${start},10000\n" +
                            ";\n" +
                            "\"\n" +
                            "\n" +
                            "echo ${ck_sql1}\n" +
                            "\n" +
                            "clickhouse-client -u ${ck_username} --password ${ck_password} -h ${ck_host} -m --multiquery -q\"${ck_sql1}\"\n" +
                            "\n" +
                            "done\n" +
                            "\n" +
                            "#done\n" +
                            "\n" +
                            "echo \"[INFO] SUCCESS\"");
        }else{
            sb.append(
                    "    from ${source_table_name}\n" +
                            "    --where toDate(UPDATE_TIME) >= toDate('${run_date}')\n" +
                            "    limit ${start},10000\n" +
                            ";\n" +
                            "\"\n" +
                            "\n" +
                            "echo ${ck_sql1}\n" +
                            "\n" +
                            "clickhouse-client -u ${ck_username} --password ${ck_password} -h ${ck_host} -m --multiquery -q\"${ck_sql1}\"\n" +
                            "\n" +
                            "done\n" +
                            "\n" +
                            "#done\n" +
                            "\n" +
                            "echo \"[INFO] SUCCESS\"");
        }

        return sb.toString();
    }

    private String getMYSQLDDL(String tableMysqlName, List<Tuple2<String, String>> colums, String pks) {
        StringBuffer sb = new StringBuffer();
        sb.append("DROP TABLE IF EXISTS "+tableMysqlName+";\n");
        sb.append("CREATE TABLE "+tableMysqlName+"\n");
        sb.append("(\n");
        for (int i = 0; i < colums.size(); i++) {
            sb.append(colums.get(i).getA().toLowerCase() +  " "+ParseUtil.mapping(colums.get(i), pks)+",\n");
        }
        sb.append("PRIMARY KEY " + pks.toLowerCase() + "\n");
        sb.append(")\n");
        return sb.toString();
    }

    private String getCKDDL(String tableCKName, List<Tuple2<String, String>> colums, String targetyu, String tableMysqlName) {
        StringBuffer sb = new StringBuffer();
        sb.append("DROP TABLE IF EXISTS " + tableCKName + ";\n");
        sb.append("CREATE TABLE " + tableCKName + "\n");
        sb.append("(\n");
        for (int i = 0; i < colums.size(); i++) {
            if (colums.size() - 1 == i) {
                sb.append(colums.get(i).getA() + " " + colums.get(i).getB() + "\n");
            } else {
                sb.append(colums.get(i).getA() + " " + colums.get(i).getB() + ",\n");
            }
        }
        String engineConfig = getMysqlEngine(targetyu, tableMysqlName);
        sb.append(")" + engineConfig + "\n");
        return sb.toString();
    }

    private String getMysqlEngine(String targetyu, String tableMysqlName) {

        String vm = ParseUtil.getYuMapping().get(targetyu);
        String result = null;
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("tableMysqlName", tableMysqlName);
        try {
            result = VelocityUtils.process(vm, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private String getFirstLine(List<String> sql) {

        return sql.get(0);
    }
}

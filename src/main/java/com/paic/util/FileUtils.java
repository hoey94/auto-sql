package com.paic.util;

import cn.hutool.core.io.FileUtil;

/**
 * @author zyh
 * @Description:
 * @date 2022/9/3010:19
 */
public class FileUtils {

//    /**
//     * 只涉及代码，不涉及调度
//     */
//    private static final String basePath = "/Users/zyh/workspace/travel-service-pipeline/apps";
    private static final String basePath = "D:/work/Java/IdeaProjects/travel-service-pipeline/apps";

    public static String getFullPath(String sourceyu, SqlTypeEnum type, String fileName){
        StringBuffer sb = new StringBuffer();
        return sb.append(basePath).append("/")
                .append(sourceyu).append("/")
                .append(type.toString().toLowerCase()).append("/")
                .append(fileName).toString();
    }

    /**
     *
     * @param ddl
     * @param sourceyu
     */
    public static void writeCKDDL(String ddl, String sourceyu, String fileName){
        // 在文件名后面拼接ck
        String ckFileName = getDDLFileName(fileName, "ck");
        // type 0是DDL, 1是DML
        FileUtil.writeBytes(ddl.getBytes(), getFullPath(sourceyu, SqlTypeEnum.getType("0"), ckFileName));
    }

    public static void writeMysqlDDL(String ddl, String sourceyu, String fileName){
        // 在文件名后面拼接ck
        String mysqlFileName = getDDLFileName(fileName, "mysql");
        // type 0是DDL, 1是DML
        FileUtil.writeBytes(ddl.getBytes(), getFullPath(sourceyu, SqlTypeEnum.getType("0"), mysqlFileName));
    }

    public static void writeExportShell(String exportShell, String sourceyu, String fileName){

        // 在文件名后面拼接ck
        String fullPath = getFullPath(sourceyu, SqlTypeEnum.getType("1"), fileName);

        // type 0是DDL, 1是DML
        FileUtil.writeBytes(exportShell.getBytes(), fullPath);
    }


    private static String getDDLFileName(String fileName, String type) {
        String reuslt = fileName;
        String preFileName = fileName.split("\\.")[0];
        String sufFileName = fileName.split("\\.")[1];

        if(type.equalsIgnoreCase("ck")){
            reuslt = preFileName + "_ck.sql";
        }

        if(type.equalsIgnoreCase("mysql")){
            reuslt = preFileName + "_mysql.sql";
        }
        return reuslt;
    }


    public static void main(String[] args) {
        String ckddl = "ddl test";
        String mysqlddl = "mysql ddl test";
        String shell = "shell test";
        String sourceyu = "ap";
        String fileName = "export_ads_ap_zyh_mysql_flt.sh";



        writeCKDDL(ckddl, sourceyu, fileName);
        writeMysqlDDL(mysqlddl, sourceyu, fileName);
        writeExportShell(shell, sourceyu, fileName);
//        writeMysqlDDL();
    }
}

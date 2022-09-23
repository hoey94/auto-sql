package com.paic.util;

import cn.hutool.core.util.ObjectUtil;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author zyh
 * @Description:
 * @date 2022/9/2211:16
 */
public class ParseUtil {

    public static Map<String, String> typeMapping = new HashMap();

    public static Map<String, String> yuMapping = new HashMap<>();

    public static Map<String, String> getTypeMapping() {
        typeMapping.put("STRING", "VARCHAR(255)");
        typeMapping.put("INT", "INT");
        typeMapping.put("DATE", "DATETIME");
        return typeMapping;
    }

    public static Map<String, String> getYuMapping() {
        yuMapping.put("fin","ENGINE = MySQL('172.22.17.38:3306', 'finance', '${tableMysqlName}', 'u_finance', 'H7jtcY_8PetE', 1);");
        yuMapping.put("flt","ENGINE = MySQL('172.22.17.38:3306', 'flight', '${tableMysqlName}', 'u_flight', 'w6jtcY_3PetE', 1);");
        yuMapping.put("loc","ENGINE = MySQL('172.22.17.38:3306', 'loc', '${tableMysqlName}', 'u_loc', 'n6jtcY_5PetE', 1);");
        yuMapping.put("ap","ENGINE = MySQL('172.22.17.38:3306', 'test', '${tableMysqlName}', 'u_airplan', 'T8jtcn_5Pepn', 1);");
        yuMapping.put("tic","ENGINE = MySQL('172.22.17.38:3306', 'orderdb', '${tableMysqlName}', 'u_order', 'W3jtcY_8BetE', 1);");
        yuMapping.put("cst","ENGINE = MySQL('172.22.17.38:3306', 'customer', '${tableMysqlName}', 'u_customer', 'N5bbcY_3wetm', 1);");
        yuMapping.put("emp","ENGINE = MySQL('172.22.17.38:3306', 'employee', '${tableMysqlName}', 'u_employee', 'N5bbcY_4wetm', 1);");
        yuMapping.put("al","ENGINE = MySQL('172.22.17.38:3306', 'airline', '${tableMysqlName}', 'u_airline', 'N5bbcY_9wetm', 1);");
        yuMapping.put("ser","ENGINE = MySQL('172.22.17.38:3306', 'service', '${tableMysqlName}', 'u_service', 'N1bbcY_3wetm', 1);");
        yuMapping.put("org","ENGINE = MySQL('172.22.17.38:3306', 'organization', '${tableMysqlName}', 'u_organization', 'N2bbcY_3wetm', 1);");
        yuMapping.put("ptn","ENGINE = MySQL('172.22.17.38:3306', 'partner', '${tableMysqlName}', 'u_partner', 'N3bbcY_3wetm', 1);");
        yuMapping.put("egp","ENGINE = MySQL('172.22.17.38:3306', 'equipment', '${tableMysqlName}', 'u_equipment', 'N4bbcY_3wetm', 1);");
        yuMapping.put("prod","ENGINE = MySQL('172.22.17.38:3306', 'product', '${tableMysqlName}', 'u_product', 'N7bbcY_3wetm', 1);");
        yuMapping.put("mkt","ENGINE = MySQL('172.22.17.38:3306', 'marketing', '${tableMysqlName}', 'u_marketing', 'N8bbcY_3wetm', 1);");
        yuMapping.put("rev","ENGINE = MySQL('172.22.17.38:3306', 'revenue', '${tableMysqlName}', 'u_revenue', 'N9bbcY_3wetm', 1);");
        yuMapping.put("cgo","ENGINE = MySQL('172.22.17.38:3306', 'cargo', '${tableMysqlName}', 'u_cargo', 'N0bbcY_3wetm', 1);");
        return yuMapping;
    }


    public static List<Tuple2<String, String>> getColums(List<String> sql){

        LinkedList<Tuple2<String, String>> tuple2s = new LinkedList<>();
        for (String line : sql) {
            line = line.trim();
            if(line.contains("TABLE") || line.contains("VIEW") || ObjectUtil.isEmpty(line)){
                continue;
            }
            if(line.length() == 1 || line.toUpperCase().contains("ENGINE")){
                continue;
            }
            line = line.replaceAll(",", "");
            line = line.replaceAll("`", "");
            line = line.replaceAll("\\s{1,}", " ");

            if(line.startsWith("(")){
                line = line.substring(1,line.length());
            }
            if(line.endsWith("))")){
                line = line.substring(0,line.length()-1);
            }
            Tuple2<String, String> tuple2 = getTuple2(line);
            // 去掉SIGN和VERSION
            if(tuple2.getA().toUpperCase().equals("SIGN") || tuple2.getA().toUpperCase().equals("VERSION")){
                continue;
            }
            tuple2s.add(tuple2);
        }
        return tuple2s;
    }

    private static Tuple2<String, String> getTuple2(String line) {
        String[] s = line.split(" ");
        return Tuple2.of(s[0], s[1]);
    }


    public static String mapping(String type){
        String result = type;
        Map<String, String> mappings = getTypeMapping();
        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if(type.toUpperCase().contains(key)){
                result = value;
            }
        }
        return result;
    }


    /**
     * 获取ck表名
     * @param firstLine
     * @param sourceyu
     * @param targetyu
     * @return
     */
    public static String getCKTableName(String firstLine,String sourceyu, String targetyu) {
        String otn = getTableName(firstLine);

        // 只要名字
        String ttn = otn;
        if(otn.contains(".")){
            ttn = otn.split("\\.")[1];
        }
        // 拼接表名
        ttn = "ads."+ sourceyu +"_" + ttn + "_mysql_" + targetyu;
        ttn = ttn.replaceAll("_vcm", "");
        return ttn;
    }

    public static String getMysqlTableName(String firstLine) {
        String otn = getTableName(firstLine);

        // 拼接表名
        String ttn = otn;
        if(otn.contains(".")){
            ttn = otn.split("\\.")[1];
        }
        ttn = ttn.replaceAll("_vcm", "");

        return ttn;
    }


    /**
     * 获取CK表名，如果没有dwd，自动拼上
     * @param firstLine
     * @return
     */
    public static String getTableName(String firstLine) {
        String otn = null;
        String[] lines = firstLine.split(" ");
        for (String line : lines) {
            // 如果表名中包含下划线
            if(line.contains("_")){
                otn = line;
            }
        }
        if(ObjectUtil.isEmpty(otn)){
            throw new RuntimeException("getMysqlTableName未解析到表名");
        }

        // 拼接表名
        String ttn = otn;
        if(!otn.contains(".")){
            ttn = "dwd."+ttn;
        }

        return ttn;
    }
}

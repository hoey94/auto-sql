package com.paic.core;

/**
 * @author zyh
 * @Description:
 * @date 2021/9/84:26 下午
 */
public class TianjiaCastGenerator {



    public static void main(String[] args) {
        String str = "`ACID` STRING,\n" +
                "\t`MSN` STRING,\n" +
                "\t`MP_ACTYPE` STRING,\n" +
                "\t`PLAN_CENTER` STRING,\n" +
                "\t`PL_ACTYPE` STRING,\n" +
                "\t`PB_ACTYPE` STRING,\n" +
                "\t`CONF_ACTYPE` STRING,\n" +
                "\t`HANDLE_BASE_NO` STRING,\n" +
                "\t`CEC` STRING,\n" +
                "\t`ACNO` STRING,\n" +
                "\t`VALID_STATUS` STRING,\n" +
                "\t`FACTORY_DATE` TIMESTAMP,\n" +
                "\t`MANUFACTURE` STRING,\n" +
                "\t`SERVE_DATE_PLAN` STRING,\n" +
                "\t`SERVE_DATE_ACTUAL` TIMESTAMP,\n" +
                "\t`EXIT_DATE_PLAN` TIMESTAMP,\n" +
                "\t`EXIT_DATE_ACTUAL` TIMESTAMP,\n" +
                "\t`LEASING_COMPANY` STRING,\n" +
                "\t`IPC_NO` STRING,\n" +
                "\t`ENG_TYPE` STRING,\n" +
                "\t`APU_TYPE` STRING,\n" +
                "\t`LINE_NO` STRING,\n" +
                "\t`OLDPLANE_MARK` STRING,\n" +
                "\t`WIDETPLANE_MARK` STRING,\n" +
                "\t`YJXYTAG` STRING,\n" +
                "\t`AC_ADDRESS_CODE` STRING,\n" +
                "\t`MAX_ELEVATION_TAKEOFF_LANDING` STRING,\n" +
                "\t`MAX_WEIGHT_SLIDE` DECIMAL ( 12, 4 ),\n" +
                "\t`MAX_WEIGHT_TAKEOFF` DECIMAL ( 12, 4 ),\n" +
                "\t`MAX_WEIGHT_LANDING` DECIMAL ( 12, 4 ),\n" +
                "\t`MAX_WEIGHT_OILLESS` DECIMAL ( 12, 4 ),\n" +
                "\t`SEAT_AMOUNT` INT,\n" +
                "\t`FIRSTCLASS_SEAT_AMOUNT` INT,\n" +
                "\t`BUSCLASS_SEAT_AMOUNT` INT,\n" +
                "\t`TOURISTCLASS_SEAT_AMOUNT` INT,\n" +
                "\t`XHHM` STRING,\n" +
                "\t`MODES` STRING,\n" +
                "\t`EN_SERIAL_NO` STRING,\n" +
                "\t`APUER` STRING,\n" +
                "\t`TRANSPORT_TYPE` STRING,\n" +
                "\t`AC_LENGTH` DECIMAL ( 6, 2 ),\n" +
                "\t`AC_WIDTH` DECIMAL ( 6, 2 ),\n" +
                "\t`AC_HEGHT` DECIMAL ( 6, 2 ),\n" +
                "\t`INPUTER_ID` STRING,\n" +
                "\t`INPUT_DATE` TIMESTAMP,\n" +
                "\t`MODIFY_BY_ID` STRING,\n" +
                "\t`MODIFY_DATE` TIMESTAMP,\n" +
                "\t`OPERATOR_CODE` STRING,\n" +
                "\t`ALREADY_TO_EM` STRING,\n" +
                "\t`FSN` STRING,\n" +
                "\t`WV` STRING,\n" +
                "\t`SHARKLETS` STRING,\n" +
                "\t`WTF` STRING,\n" +
                "\t`PRODUCE_DATE` TIMESTAMP,\n" +
                "\t`IMPORT_DATE` TIMESTAMP,\n" +
                "\t`FIRST_FLY_DATE` TIMESTAMP,\n" +
                "\t`LAND_DATE` TIMESTAMP,\n" +
                "\t`DELIVER_DATE` TIMESTAMP,\n" +
                "\t`CONTRACT_CANCEL_DATE` TIMESTAMP,\n" +
                "\t`INTRODUCTION_WAY` STRING,\n" +
                "\t`AC_INTERACTIVELY` STRING,\n" +
                "\t`PIC_TYPE` STRING,\n" +
                "\t`MANDT` STRING,\n" +
                "\t`ZENG_QUNT` STRING,\n" +
                "\t`ZSIDT` STRING,\n" +
                "\t`ZSHDT` STRING,\n" +
                "\t`ZYSTYP` STRING,\n" +
                "\t`ZRET_FJ` STRING,\n" +
                "\t`ZFJ_STS` STRING,\n" +
                "\t`ZIATIM` STRING,\n" +
                "\t`ZISTIM` STRING,\n" +
                "\t`ZIUPDOWN` STRING,\n" +
                "\t`ZICUPDOWN` STRING,\n" +
                "\t`OVERWATER` STRING,\n" +
                "\t`ZTOMESST` STRING,\n" +
                "\t`TPLNR` STRING,\n" +
                "\t`OBJCT` STRING,\n" +
                "\t`ZJOBNAME` STRING,\n" +
                "\t`ZTRU_LV` STRING,\n" +
                "\t`ZYYR` STRING,\n" +
                "\t`ZENGX` STRING,\n" +
                "\t`ZAPUPN` STRING,\n" +
                "\t`ZENXXGX` STRING,\n" +
                "\t`ZPBDJ` STRING,\n" +
                "\t`ZRVSM` STRING,\n" +
                "\t`ZRNAV1` STRING,\n" +
                "\t`ZRNAV2` STRING,\n" +
                "\t`ZRNAV5` STRING,\n" +
                "\t`ZRNAV10` STRING,\n" +
                "\t`ZGLS` STRING,\n" +
                "\t`ZGGY` STRING,\n" +
                "\t`ZYX2` STRING,\n" +
                "\t`ZYX3` STRING,\n" +
                "\t`ZSBAS` STRING,\n" +
                "\t`ZGBAS` STRING,\n" +
                "\t`ZRNP4` STRING,\n" +
                "\t`ZJDBS` STRING,\n" +
                "\t`ZRNPAR` STRING,\n" +
                "\t`ZADS_B` STRING,\n" +
                "\t`ZRNP1` STRING,\n" +
                "\t`ZHUD` STRING,\n" +
                "\t`ZRNPAPCH` STRING,\n" +
                "\t`ZCPDLC` STRING,\n" +
                "\t`ZADS_C` STRING,\n" +
                "\t`ZADS_B_DO` STRING,\n" +
                "\t`ZETOPSTIME` STRING,\n" +
                "\t`ZRCPTIME` STRING,\n" +
                "\t`ZRSPTIME` STRING,\n" +
                "\t`ZZWBJ` STRING,\n" +
                "\t`ZTSN` STRING,\n" +
                "\t`ZCSN` STRING,\n" +
                "\t`ZJFDD` STRING,\n" +
                "\t`ZBZ` STRING,\n" +
                "\t`GROUPS` STRING,\n" +
                "\t`ZJFDT` TIMESTAMP,";
        str = castDecimalToFloat(str);
        String[] lines = str.split(",");
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            String newLine = addCast(line);
            if(i != lines.length -1){
                newLine += ",";
            }
            System.out.println(newLine);
        }

    }

    private static String castDecimalToFloat(String line) {
        return line.replaceAll("(?i)DECIMAL.+", "FLOAT,");
    }

    private static String addCast(String line) {
        StringBuffer newLine = new StringBuffer();
        String[] s = line.split(" ");
        String colName = s[0].trim();
        String type = s[1];
        if(colName.equals("`op_type`") || colName.equals("`commit_time`") || colName.equals("`ogg_time`")){
            colName = colName;
        }else{
            colName = "data_row." + colName;
        }
        newLine.append(colName);
        if(!type.toUpperCase().equals("STRING")){
            wapperLine(newLine, type.toUpperCase());
        }
        return newLine.toString();
    }

    private static void wapperLine(StringBuffer line, String type) {
        line.insert(0,"CAST(");
        line.append(" as ").append(type).append(")");
    }
}

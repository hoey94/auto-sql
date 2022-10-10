package com.paic.util;

import java.util.List;

/**
 * @author zyh
 * @Description:
 * @date 2022/10/1014:27
 */
public class ColumnUtils {


    public static boolean checkGMTFlag(List<Tuple2<String, String>> colums){
        int GMT_CREATE_Flag = 0;
        int GMT_MODIFIED_Flag = 0;
        int OP_TYPE_Flag = 0;

        for (int i = 0; i < colums.size(); i++) {

            if(colums.get(i).getA().toUpperCase().contains("GMT_CREATE")){
                GMT_CREATE_Flag = 1;
            }
            if(colums.get(i).getA().toUpperCase().contains("GMT_MODIFIED")){
                GMT_MODIFIED_Flag = 1;
            }
            if(colums.get(i).getA().toUpperCase().contains("OP_TYPE")){
                OP_TYPE_Flag = 1;
            }
        }

        if(GMT_CREATE_Flag == 1 && GMT_MODIFIED_Flag == 1 && OP_TYPE_Flag ==1){
            return true;
        }else{
            return false;
        }
    }
}

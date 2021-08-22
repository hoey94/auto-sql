package com.paic.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author zyh
 * @Description:
 * @date 2021/8/225:19 下午
 */
public interface Item {

    /**
     * 导入文档，解析输出
     */
    @Getter
    enum Import implements Item{
        HBASE("importHBaseItem", "HBase"),PHOENIX("importPhoenixItem","Phoenix"),KAFKA_COMMA("importKafkaCommaItem","Kafka(comma)"),KAFKA_JSON("importKafkaJsonItem","Kafka(json)");
        private String key;
        private String name;
        private Import(String key, String name){
            this.key = key;
            this.name = name;
        }
    }

    // 导出文档到磁盘
    @Getter
    enum Export implements Item{
        HBASE("exportHBaseItem", "HBase"),PHOENIX("exportPhoenixItem","Phoenix"),KAFKA("exportKafkaItem","Kafka");
        private String key;
        private String name;
        private Export(String key, String name){
            this.key = key;
            this.name = name;
        }
    }


}

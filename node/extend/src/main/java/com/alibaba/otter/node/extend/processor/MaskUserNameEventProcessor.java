package com.alibaba.otter.node.extend.processor;

import com.alibaba.otter.shared.etl.model.EventColumn;
import com.alibaba.otter.shared.etl.model.EventData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MaskUserNameEventProcessor extends AbstractEventProcessor {
    private static final Logger logger = LoggerFactory.getLogger(MaskUserNameEventProcessor.class);

    public boolean process(EventData eventData) {
        logger.info("do mask user name process start");
        String tableName = eventData.getTableName();

        if (tableName != null && tableName.equalsIgnoreCase("user")) {
            for (EventColumn column : eventData.getColumns()) {
                if (column.isUpdate() && "name".equalsIgnoreCase(column.getColumnName())) {
                    String maskName = mask(column.getColumnValue());
                    column.setColumnValue(maskName);
                }
            }
        }
        logger.info("do mask user name process success");

        return true;
    }

    private static String mask(String str) {
        int len = str.length();

        switch (len) {
            case 0:
                return str;
            case 1:
                return "*";
            case 2:
                return str.substring(0, 1) + "*";
            default:
                StringBuilder builder = new StringBuilder();
                builder.append(str.substring(0, 1));

                for (int i = 0; i < len - 2; i++) {
                    builder.append("*");
                }

                builder.append(str.substring(len - 1, len));
                return builder.toString();
        }
    }
}

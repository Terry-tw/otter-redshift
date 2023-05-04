package com.alibaba.otter.node.etl.common.db.dialect.redshift;

import com.alibaba.otter.node.etl.common.db.dialect.AbstractSqlTemplate;

public class RedshiftSqlTemplate extends AbstractSqlTemplate {

    private static final String ESCAPE = "\"";

    public String getMergeSql(String schemaName, String tableName, String[] pkNames, String[] columnNames,
                              String[] viewColumnNames, boolean includePks, String shardColumn) {
        StringBuilder sql = new StringBuilder("insert into " + getFullName(schemaName, tableName) + "(");
        int size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(columnNames[i])).append(" , ");
        }
        size = pkNames.length;
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(pkNames[i])).append((i + 1 < size) ? " , " : "");
        }

        sql.append(") values (");
        size = columnNames.length;
        for (int i = 0; i < size; i++) {
            sql.append("?").append(" , ");
        }
        size = pkNames.length;
        for (int i = 0; i < size; i++) {
            sql.append("?").append((i + 1 < size) ? " , " : "");
        }
        sql.append(")");
        sql.append(" ON CONFLICT ( ");
        for (int i = 0; i < size; i++) {
            sql.append(appendEscape(pkNames[i])).append((i + 1 < size) ? " , " : "");
        }
        sql.append(" ) DO UPDATE SET ");

        size = columnNames.length;
        for (int i = 0; i < size; i++) {

            sql.append(appendEscape(columnNames[i]))
                    .append("=excluded.")
                    .append(appendEscape(columnNames[i]))
                    .append((i + 1 < size) ? " , " : "");
        }

        return sql.toString().intern();// intern优化，避免出现大量相同的字符串
    }

    protected String appendEscape(String columnName) {
        return ESCAPE + columnName + ESCAPE;
    }

}

package com.alibaba.otter.node.etl.common.db.dialect.postgresql;

import com.alibaba.otter.node.etl.common.db.dialect.AbstractSqlTemplate;

public class PostgresqlSqlTemplate extends AbstractSqlTemplate {

    private static final String ESCAPE = "`";

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
        sql.append(" on duplicate key update ");

        size = columnNames.length;
        for (int i = 0; i < size; i++) {
            // 如果是DRDS数据库, 并且存在拆分键 且 等于当前循环列, 跳过
            if(!includePks && shardColumn != null && columnNames[i].equals(shardColumn)){
                continue;
            }

            sql.append(appendEscape(columnNames[i]))
                    .append("=values(")
                    .append(appendEscape(columnNames[i]))
                    .append(")");
            if (includePks) {
                sql.append(" , ");
            } else {
                sql.append((i + 1 < size) ? " , " : "");
            }
        }

        if (includePks) {
            size = pkNames.length;
            for (int i = 0; i < size; i++) {
                sql.append(appendEscape(pkNames[i])).append("=values(").append(appendEscape(pkNames[i])).append(")");
                sql.append((i + 1 < size) ? " , " : "");
            }
        }

        return sql.toString().intern();// intern优化，避免出现大量相同的字符串
    }

    protected String appendEscape(String columnName) {
        return ESCAPE + columnName + ESCAPE;
    }

}

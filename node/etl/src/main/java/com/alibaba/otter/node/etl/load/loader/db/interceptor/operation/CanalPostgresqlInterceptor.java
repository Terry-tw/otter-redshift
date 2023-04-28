package com.alibaba.otter.node.etl.load.loader.db.interceptor.operation;

public class CanalPostgresqlInterceptor extends AbstractOperationInterceptor {

    public static final String mergeofPostgresqlSql     = "INSERT INTO {0} (id, {1}) VALUES (?, ?) ON DUPLICATE KEY UPDATE {1} = VALUES({1})";

    public static final String mergeofPostgresqlInfoSql = "INSERT INTO {0} (id, {1}, {2}) VALUES (?, ? ,?) ON DUPLICATE KEY UPDATE {1} = VALUES({1}) , {2} = VALUES({2})";

    public CanalPostgresqlInterceptor(){
        super(mergeofPostgresqlSql, mergeofPostgresqlInfoSql);
    }

}

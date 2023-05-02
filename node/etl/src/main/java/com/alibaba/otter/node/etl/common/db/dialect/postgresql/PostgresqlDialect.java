package com.alibaba.otter.node.etl.common.db.dialect.postgresql;

import com.alibaba.otter.node.etl.common.db.dialect.AbstractDbDialect;
import com.alibaba.otter.shared.common.utils.meta.DdlUtils;
import com.google.common.base.Function;
import com.google.common.collect.OtterMigrateMap;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.NestableRuntimeException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PostgresqlDialect extends AbstractDbDialect {

    private boolean                   isDRDS = false;
    private Map<List<String>, String> shardColumns;

    public PostgresqlDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        super(jdbcTemplate, lobHandler);
        sqlTemplate = new PostgresqlSqlTemplate();
    }

    public PostgresqlDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, String databaseVersion,
                             int majorVersion, int minorVersion){
        super(jdbcTemplate, lobHandler, name, majorVersion, minorVersion);
        sqlTemplate = new PostgresqlSqlTemplate();

        if (StringUtils.contains(databaseVersion, "-TDDL-")) {
            isDRDS = true;
            initShardColumns();
        }
    }

    private void initShardColumns() {
        this.shardColumns = OtterMigrateMap.makeSoftValueComputingMap(new Function<List<String>, String>() {

            public String apply(List<String> names) {
                Assert.isTrue(names.size() == 2);
                try {
                    String result = DdlUtils.getShardKeyByDRDS(jdbcTemplate, names.get(0), names.get(0), names.get(1));
                    if (StringUtils.isEmpty(result)) {
                        return "";
                    } else {
                        return result;
                    }
                } catch (Exception e) {
                    throw new NestableRuntimeException("find table [" + names.get(0) + "." + names.get(1) + "] error",
                            e);
                }
            }
        });
    }

    public boolean isCharSpacePadded() {
        return false;
    }

    public boolean isCharSpaceTrimmed() {
        return true;
    }

    public boolean isEmptyStringNulled() {
        return false;
    }

    public boolean isSupportMergeSql() {
        return true;
    }

    public String getDefaultSchema() {
        return null;
    }

    public boolean isDRDS() {
        return isDRDS;
    }

    public String getShardColumns(String schema, String table) {
        if (isDRDS()) {
            return shardColumns.get(Arrays.asList(schema, table));
        } else {
            return null;
        }
    }

    public String getDefaultCatalog() {
        return (String) jdbcTemplate.queryForObject("select database()", String.class);
    }
}
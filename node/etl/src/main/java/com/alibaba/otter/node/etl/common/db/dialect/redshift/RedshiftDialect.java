package com.alibaba.otter.node.etl.common.db.dialect.redshift;

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

public class RedshiftDialect extends AbstractDbDialect {

    private boolean                   isDRDS = false;
    private Map<List<String>, String> shardColumns;

    public RedshiftDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler){
        super(jdbcTemplate, lobHandler);
        sqlTemplate = new RedshiftSqlTemplate();
    }

    public RedshiftDialect(JdbcTemplate jdbcTemplate, LobHandler lobHandler, String name, String databaseVersion,
                             int majorVersion, int minorVersion){
        super(jdbcTemplate, lobHandler, name, majorVersion, minorVersion);
        sqlTemplate = new RedshiftSqlTemplate();
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
        return (String) jdbcTemplate.queryForObject("select current_database()", String.class);
    }
}

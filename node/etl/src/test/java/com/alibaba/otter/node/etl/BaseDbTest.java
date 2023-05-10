/*
 * Copyright (C) 2010-2101 Alibaba Group Holding Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alibaba.otter.node.etl;

import com.alibaba.otter.shared.common.model.config.data.DataMediaType;
import com.alibaba.otter.shared.common.model.config.data.db.DbDataMedia;
import com.alibaba.otter.shared.common.model.config.data.db.DbMediaSource;

public class BaseDbTest extends BaseOtterTest {

    public DbDataMedia getMysqlMedia() {
        DbMediaSource dbMediaSource = new DbMediaSource();
        dbMediaSource.setId(10L);
        dbMediaSource.setDriver("com.mysql.jdbc.Driver");
        dbMediaSource.setUsername("xxxxx");
        dbMediaSource.setPassword("xxxxx");
        dbMediaSource.setUrl("jdbc:mysql://127.0.0.1:3306/srf");
        dbMediaSource.setEncode("UTF-8");
        dbMediaSource.setType(DataMediaType.MYSQL);

        DbDataMedia dataMedia = new DbDataMedia();
        dataMedia.setSource(dbMediaSource);
        dataMedia.setId(1L);
        dataMedia.setName("columns");
        dataMedia.setNamespace("srf");
        return dataMedia;
    }

    public DbDataMedia getOracleMedia() {
        DbMediaSource dbMediaSource = new DbMediaSource();
        dbMediaSource.setId(11L);
        dbMediaSource.setDriver("oracle.jdbc.OracleDriver");
        dbMediaSource.setUsername("xxxxx");
        dbMediaSource.setPassword("xxxxx");
        dbMediaSource.setUrl("jdbc:oracle:thin:@127.0.0.1:1521:crmgsb");
        dbMediaSource.setEncode("UTF-8");
        dbMediaSource.setType(DataMediaType.ORACLE);

        DbDataMedia dataMedia = new DbDataMedia();
        dataMedia.setSource(dbMediaSource);
        dataMedia.setId(2L);
        dataMedia.setName("columns");
        dataMedia.setNamespace("srf");
        return dataMedia;
    }

    public DbDataMedia getPostgresqlMedia() {
        DbMediaSource dbMediaSource = new DbMediaSource();
        dbMediaSource.setId(12L);
        dbMediaSource.setDriver("org.postgresql.Driver");
        dbMediaSource.setUsername("xxxxx");
        dbMediaSource.setPassword("xxxxx");
        dbMediaSource.setUrl("jdbc:postgresql://127.0.0.1:5432/postgres");
        dbMediaSource.setEncode("UTF-8");
        dbMediaSource.setType(DataMediaType.POSTGRESQL);

        DbDataMedia dataMedia = new DbDataMedia();
        dataMedia.setSource(dbMediaSource);
        dataMedia.setId(3L);
        dataMedia.setName("columns");
        dataMedia.setNamespace("srf");
        return dataMedia;
    }

    public DbDataMedia getRedshiftMedia() {
        DbMediaSource dbMediaSource = new DbMediaSource();
        dbMediaSource.setId(13L);
        dbMediaSource.setDriver("com.amazon.redshift.jdbc42.Driver");
        dbMediaSource.setUsername("xxxxx");
        dbMediaSource.setPassword("xxxxx");
        dbMediaSource.setUrl("jdbc:redshift://cluster.abcd1234.us-west-4.redshift.amazonaws.com:5439/test");
        dbMediaSource.setEncode("UTF-8");
        dbMediaSource.setType(DataMediaType.REDSHIFT);

        DbDataMedia dataMedia = new DbDataMedia();
        dataMedia.setSource(dbMediaSource);
        dataMedia.setId(4L);
        dataMedia.setName("columns");
        dataMedia.setNamespace("srf");
        return dataMedia;
    }
}

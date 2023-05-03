package com.alibaba.otter.node.extend.processor.demo;

import com.alibaba.otter.shared.etl.extend.processor.support.DataSourceFetcher;

public class MysqlDemoEventProcessor extends AbstractDemoEventProcessor {

    @Override
    public void setDataSourceFetcher(DataSourceFetcher dataSourceFetcher) {
        this.sourceDataSource = dataSourceFetcher.fetch(new Long(3));
        this.targetDataSource = dataSourceFetcher.fetch(new Long(4));
    }
}

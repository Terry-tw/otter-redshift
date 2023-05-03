package com.alibaba.otter.node.extend.processor.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.otter.node.extend.processor.AbstractEventProcessor;
import com.alibaba.otter.shared.etl.extend.processor.support.DataSourceFetcherAware;
import com.alibaba.otter.shared.etl.model.EventColumn;
import com.alibaba.otter.shared.etl.model.EventData;

abstract class AbstractDemoEventProcessor extends AbstractEventProcessor implements DataSourceFetcherAware {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDemoEventProcessor.class);

    protected DataSource sourceDataSource;
    protected DataSource targetDataSource;

    public boolean process(EventData eventData) {
        logger.info("do process start");
        String tableName = eventData.getTableName();

        if (tableName != null && tableName.startsWith("wallet_")) {
            logger.debug("process for wallet");
            Integer userId = -1;
            boolean isMoneyChange = false;

            for (EventColumn column : eventData.getColumns()) {
                if ("user_id".equals(column.getColumnName())) {
                    String userIdStr = column.getColumnValue();
                    userId = Integer.parseInt(userIdStr);
                }

                if ("money".equals(column.getColumnName())) {
                    isMoneyChange = column.isUpdate();
                }
            }

            logger.debug("process for {}: isMoneyChange = {}, userId = {}", tableName, isMoneyChange, userId);

            if (isMoneyChange && userId > -1) {
                Integer totalMoney = queryWallet(1, userId) + queryWallet(2, userId) + queryWallet(3, userId);
                updateMoney(userId, totalMoney);
            }
        }

        logger.info("do process success");

        return true;
    }

    private Integer queryWallet(Integer walletId, Integer userId) {
        int money = 0;

        try {
            Connection conn = this.sourceDataSource.getConnection();
            PreparedStatement stat = conn.prepareStatement(queryWalletSql(walletId));
            stat.setInt(1, userId);

            ResultSet set = stat.executeQuery();
            if (set.next()) {
                money = set.getInt(1);
            }

            set.close();
            conn.close();
        } catch (Exception e) {
            logger.error("error in query wallet_0{}: {}", walletId, e.getMessage());
        }

        return money;
    }

    private void updateMoney(Integer userId, Integer totalMoney) {
        logger.info("process for updateMoney: userId = {}, totalMoney = {}", userId, totalMoney);

        try {
            Connection conn = this.targetDataSource.getConnection();;
            PreparedStatement stat = conn.prepareStatement(updateMoneySql());
            stat.setInt(1, totalMoney);
            stat.setInt(2, userId);
            stat.executeUpdate();
            conn.close();
        } catch (Exception e) {
            logger.error("error in updateMoney: {}", e.getMessage());
        }
    }

    protected String updateMoneySql() {
        return "UPDATE `user` SET `total_money` = ? WHERE `id` = ?";
    }

    protected String queryWalletSql(Integer walletId) {
        return String.format("SELECT `money` FROM `wallet_%02d` WHERE `user_id` = ?", walletId);
    }
}

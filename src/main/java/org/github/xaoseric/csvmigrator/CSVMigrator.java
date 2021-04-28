package org.github.xaoseric.csvmigrator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVMigrator
{
    private static CSVMigrator instance;

    private static Config config;
    private static HikariDataSource dataSource;
    private static Logger logger = Logger.getLogger(CSVMigrator.class.getName());

    public static void main(String[] args) {

        // load config
        loadConfig();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase());
        hikariConfig.setUsername(config.getUser());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.setConnectionTimeout(10 * 1000);
        hikariConfig.setIdleTimeout(30 * 1000);
        hikariConfig.setMaxLifetime(300 * 1000);
        hikariConfig.setMinimumIdle(config.getSqlMinConnections());
        hikariConfig.setMaximumPoolSize(config.getSqlMaxConnections());
        hikariConfig.setLeakDetectionThreshold(2000);

        try {
            dataSource = new HikariDataSource(hikariConfig);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to start Hikari Data Source!", e);
            shutdown();
        }

        try {
            Exporter.ExportTitanicData();
        } catch (IOException|SQLException e) {
            logger.log(Level.SEVERE, "Failed to migrate titanic csv data", e);
            shutdown();
        }
    }

    /**
     * Loads the config for Dragon and creates any needed directories
     */
    private static void loadConfig() {
        //load our config
        config = new Config();

        if (!config.getConfigFile().exists()) {
            try {
                config.getConfigFile().createNewFile();
                config.save();
                logger.info("New config generated! Please enter the settings and try again");
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "Failed to create new settings.json file", ex);
                shutdown();
            }
        }

        try {
            logger.info("Loading config...");
            config.load();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to load config", ex);
            shutdown();
        }
    }

    public static void shutdown() {
        System.exit(0);
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

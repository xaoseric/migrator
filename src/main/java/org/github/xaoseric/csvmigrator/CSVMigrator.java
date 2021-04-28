package org.github.xaoseric.csvmigrator;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.ibatis.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;


public class CSVMigrator
{

    public static File configFile = new File("config.json");
    private static Config config;
    private static HikariDataSource dataSource;
    public static final Logger logger = LogManager.getLogger("CSVMigrator");

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
            logger.error("Failed to start Hikari Data Source!", e);
            shutdown();
        }

        try {
            // Run sql scripts because less work
            logger.info("Running SQL Migrations");

            // Initialize the script runner
            ScriptRunner sr = new ScriptRunner(dataSource.getConnection());

            // Create teh reader objects
            Reader titanicReader = new BufferedReader(new FileReader("./sql/titanic.sql"));

            // Run the scripts
            sr.runScript(titanicReader);
        } catch (SQLException|IOException e) {
            logger.error("Failed to run sql migrations", e);
            shutdown();
        }

        try {
            logger.info("Exporting Titanic CSV Data");
            Exporter.ExportTitanicData();
        } catch (IOException|SQLException e) {
            logger.error("Failed to migrate titanic csv data", e);
            shutdown();
        }

        logger.info("Completed Titanic CSV Data Export To MySQL!");
        shutdown();
    }

    /**
     * Loads the config for Dragon and creates any needed directories
     */
    private static void loadConfig() {
        //load our config
        config = new Config();

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
                config.save();
                logger.info("New config generated! Please enter the settings and try again");
            } catch (IOException ex) {
                logger.error("Failed to create new config.json file", ex);
                shutdown();
            }
        }

        try {
            logger.info("Loading config...");
            config.load();
        } catch (IOException ex) {
            logger.error("Failed to load config", ex);
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

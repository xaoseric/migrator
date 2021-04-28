package org.github.xaoseric.csvmigrator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Config
{
    private int port, sqlMinConnections, sqlMaxConnections;
    private String host, database, user, password;

    public Config() {
        host = "";
        port = 3306;
        database = "";
        user = "";
        password = "";
        sqlMinConnections = 5;
        sqlMaxConnections = 100;
    }

    public void save() throws IOException {
        BufferedWriter fout;
        fout = new BufferedWriter(new FileWriter(CSVMigrator.configFile));
        fout.write(new GsonBuilder().setPrettyPrinting().create().toJson(this));
        fout.close();
    }

    public void load() throws IOException {
        RandomAccessFile fin;
        byte[] buffer;

        fin = new RandomAccessFile(CSVMigrator.configFile, "r");
        buffer = new byte[(int) fin.length()];
        fin.readFully(buffer);
        fin.close();

        String json = new String(buffer);
        Config file = new Gson().fromJson(json, Config.class);
        host = file.host;
        port = file.port;
        database = file.database;
        user = file.user;
        password = file.password;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getSqlMinConnections() {
        return sqlMinConnections;
    }

    public int getSqlMaxConnections() {
        return sqlMaxConnections;
    }
}

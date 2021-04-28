package org.github.xaoseric.csvmigrator;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.github.xaoseric.csvmigrator.data.TitanicAnalytic;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

public class Exporter {

    public static void ExportTitanicData() throws IOException, SQLException
    {
        Reader reader = Files.newBufferedReader(Paths.get("./data/titanic_analytics.csv"));
        CsvToBean<TitanicAnalytic> csvToBean = new CsvToBeanBuilder<TitanicAnalytic>(reader)
                .withType(TitanicAnalytic.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        List<TitanicAnalytic> csvList = csvToBean.parse();
        saveTitanicData(csvList);

    }

    private static void saveTitanicData(List<TitanicAnalytic> entities) throws SQLException {
        try (
                Connection conn = CSVMigrator.getConnection();
                PreparedStatement stmt = conn.prepareStatement("INSERT INTO `titanic_analytics` (`passenger_id`, `survived`, `pclass`, `name`, `sex`, `age`, " +
                        "`sibsp`, `parch`, `ticket`, `fare`, `cabin`, `embarked`) VALUES " +
                        "(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
        ) {
            int i = 0;

            for (TitanicAnalytic entity : entities) {
                stmt.setInt(1, entity.getPassengerId());
                stmt.setInt(2, entity.getSurvived());
                stmt.setInt(3, entity.getPclass());
                stmt.setString(4, entity.getName());
                stmt.setString(5, entity.getSex());
                stmt.setDouble(6, entity.getAge());
                stmt.setInt(7, entity.getSibsp());
                stmt.setInt(8, entity.getParch());
                stmt.setString(9, entity.getTicket());
                stmt.setDouble(10, entity.getFare());
                stmt.setString(11, entity.getCabin());
                stmt.setString(12, entity.getEmbarked());

                stmt.addBatch();
                i++;

                if (i % 1000 == 0 || i == entities.size()) {
                    stmt.executeBatch(); // Execute every 1000 items.
                }
            }
        }
    }

}

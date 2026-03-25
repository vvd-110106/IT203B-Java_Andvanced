package Session14.FLASHSALE.FLASHSALE.src.com.flashsale.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    
    public static void initialize() {
        Connection connection = DatabaseConnectionManager.getInstance().getConnection();
        String schemaPath = "src/database/schema.sql"; 
        
        try (BufferedReader reader = new BufferedReader(new FileReader(schemaPath));
             Statement statement = connection.createStatement()) {
            
            StringBuilder sql = new StringBuilder();
            String line;
            String delimiter = ";";
            
            while ((line = reader.readLine()) != null) {
                String trimmedLine = line.trim();
                
                if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                    continue;
                }
                
                if (trimmedLine.startsWith("DELIMITER")) {
                    delimiter = trimmedLine.split(" ")[1];
                    continue;
                }
                
                sql.append(line).append("\n");
                
                if (trimmedLine.endsWith(delimiter)) {
                    String command = sql.toString().replace(delimiter, ""); // Remove delimiter
                    try {
                        statement.execute(command);
                    } catch (SQLException e) {
                        System.out.println("Error executing: " + command);
                        e.printStackTrace();
                    }
                    sql.setLength(0);
                }
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}

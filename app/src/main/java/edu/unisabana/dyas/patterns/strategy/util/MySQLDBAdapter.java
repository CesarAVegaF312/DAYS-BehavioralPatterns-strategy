package edu.unisabana.dyas.patterns.strategy.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class MySQLDBAdapter  {

    private static final String DB_PROPERTIES = "META-INF/DBMySQL.properties";

    //Propiedades de los archivos properties
    private static final String DB_NAME_PROP = "dbname";
    private static final String DB_HOST_PROP = "host";
    private static final String DB_PASSWORD_PROP = "password";
    private static final String DB_PORT_PROP = "port";
    private static final String DB_USER_PROP = "user";

    static {
        //Bloque para registrar el Driver de MySQL
        try {
            new com.mysql.jdbc.Driver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            String connectionString = createConnectionString();
            Connection connection = DriverManager.getConnection(connectionString);
            System.out.println("Connection class ==> " + connection.getClass().getName());
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private String createConnectionString() {
        Properties prop = PropertiesUtil.loadProperty(DB_PROPERTIES);
        // Las variables de entorno (si existen) tienen prioridad sobre el archivo
        // properties, para no depender de credenciales hardcodeadas en el repo.
        String host = envOrProperty("DB_HOST", prop, DB_HOST_PROP);
        String port = envOrProperty("DB_PORT", prop, DB_PORT_PROP);
        String db = envOrProperty("DB_NAME", prop, DB_NAME_PROP);
        String user = envOrProperty("DB_USER", prop, DB_USER_PROP);
        String password = envOrProperty("DB_PASSWORD", prop, DB_PASSWORD_PROP);

        String connectionString = "jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + password;
        System.out.println("ConnectionString ==> jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=****");
        return connectionString;
    }

    private String envOrProperty(String envVar, Properties prop, String propertyKey) {
        String envValue = System.getenv(envVar);
        return envValue != null ? envValue : prop.getProperty(propertyKey);
    }
}
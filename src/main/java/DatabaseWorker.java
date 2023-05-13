import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DatabaseWorker {

    //  Доступы
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5500/checker";
    static final String USER = "admin";
    static final String PASS = "1H1d3e9g";

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private void createNewsTable() throws SQLException {
        Statement statement = null;

        String createTableSQL = "CREATE TABLE IF NOT EXISTS news("
                + "id SERIAL NOT NULL, "
                + "title VARCHAR(255) NOT NULL, "
                + "url VARCHAR(255) NOT NULL, "
                + "text Text NOT NULL, "
                + "markers VARCHAR(255) NOT NULL, "
                + "date VARCHAR(20), " + "PRIMARY KEY (id) "
                + ")";

        try {
            statement = this.connection.createStatement();

            // выполнить SQL запрос
            statement.execute(createTableSQL);
            System.out.println("Table is created!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public void saveNews(List<News> news) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO news(title,url,text,date,markers) VALUES ");
        for(int i=0; i<news.size(); i++) {
            sb.append("(");
            sb.append("'");
            sb.append(news.get(i).getTitle());
            sb.append("'");
            sb.append(",");
            sb.append("'");
            sb.append(news.get(i).getUrl());
            sb.append("'");
            sb.append(",");
            sb.append("'");
            sb.append(news.get(i).getText());
            sb.append("'");
            sb.append(",");
            sb.append("'");
            sb.append(news.get(i).getDate());
            sb.append("'");
            sb.append(",");
            sb.append("'");
            sb.append(news.get(i).getMarkersString());
            sb.append("'");
            sb.append(")");
            if (i!=news.size()-1) {
                sb.append(",");
            }
            sb.append("\n");
        }
        sb.append(";");
        System.out.println(sb.toString());

        Statement statement = null;

        try {
            statement = this.connection.createStatement();

            statement.execute(sb.toString());
            System.out.println("Insert is done!");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    public DatabaseWorker() {
        System.out.println("Testing connection to PostgreSQL JDBC");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        System.out.println("PostgreSQL JDBC Driver successfully connected");

        try {
            this.connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            this.createNewsTable();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
            return;
        }

        if (this.connection != null) {
            System.out.println("You successfully connected to database now");
        } else {
            System.out.println("Failed to make connection to database");
        }
    }

    public void closeConnection() throws SQLException {
        if (this.connection!= null) {
            this.connection.close();
        }
    }
}
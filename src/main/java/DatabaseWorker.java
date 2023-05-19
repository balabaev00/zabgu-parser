import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Класс для работы с базой данных
 */
public class DatabaseWorker {

    //  Доступы
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5500/checker";
    static final String USER = "admin";
    static final String PASS = "1H1d3e9g";

    private Connection connection;

    /**
     * Getter для connection
     * @return соединение типа Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Создание таблицы News
     * Если таблица создана, то ничего не делать
     */
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
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Принимает массив новостей, сохраняет его в базу дааных
     * @param news List<News> - массив новостей
     */
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

        Statement statement = null;

        try {
            statement = this.connection.createStatement();

            statement.execute(sb.toString());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
    }

    /**
     * Инициализация класса
     * Устанавливает соедение к серверу PostgreSQL
     */
    public DatabaseWorker() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("PostgreSQL JDBC Driver is not found. Include it in your library path ");
            e.printStackTrace();
            return;
        }

        try {
            this.connection = DriverManager
                    .getConnection(DB_URL, USER, PASS);
            this.createNewsTable();

        } catch (SQLException e) {
            System.out.println("Connection Failed");
            e.printStackTrace();
        }
    }

    /**
     * Закрытие соединения с базой данных
     */
    public void closeConnection() throws SQLException {
        if (this.connection!= null) {
            this.connection.close();
        }
    }
}
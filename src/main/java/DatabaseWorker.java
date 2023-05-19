import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс для работы с базой данных
 */
public class DatabaseWorker {

    //  Доступы
    static final String DB_URL = "jdbc:postgresql://127.0.0.1:5500/checker";
    static final String USER = "admin";
    static final String PASS = "1H1d3e9g";
    private static final String INSERT_USERS_SQL = "INSERT INTO news" +
            "  (text, title, date, markers, url) VALUES " +
            " (?, ?, ?, ?, ?);";

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
        for (int i = 0; i < news.size(); i++) {

            Statement statement = null;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL);
                preparedStatement.setString(1, news.get(i).getText());
                preparedStatement.setString(2, news.get(i).getTitle());
                preparedStatement.setString(3, news.get(i).getDate());
                preparedStatement.setString(4, news.get(i).getMarkersString());
                preparedStatement.setString(5, news.get(i).getUrl());

                System.out.println(preparedStatement);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {
                if (statement != null) {
                    statement.close();
                }
            }
        }
    }

    /**
     * Получает из базы данных список текстов и формирует одну строку
     * @return String - текст новостей
     * @throws SQLException
     */
    public String getNews() throws SQLException {
        Statement statement = null;

        String SQL = "SELECT text FROM news";
        StringBuilder fullText = new StringBuilder();

        try {
            statement = this.connection.createStatement();

            ResultSet rs = statement.executeQuery(SQL.toString());

            while (rs.next()) {
               fullText.append(rs.getString("text") + "\n");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return fullText.toString();
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
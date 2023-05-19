import org.jsoup.select.Elements;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final int DEFAULT_PAGE_COUNT=5;
    private static final String DEFAULT_SUBSTRING= "ЗабГУ";
    private static final FileWorker fileWorker= new FileWorker();
    private static final RegexWorker regexWorker = new RegexWorker();
    private static final int THREAD_COUNT=4;
    static List<News> news = new ArrayList<>();
    public static void main(String[] args) throws SQLException, IOException, InterruptedException {
        if (args.length > 0) {
            if (args[0].equals("/parse")) {
                int pageCount = Integer.parseInt(args[1]);

                Parser parser = new Parser();
                List<Elements> newsElement = parser.getNewsLine(pageCount);
                for (int i=0;i<newsElement.size(); i++) {
                    news.addAll(parser.getNewsFromLine(newsElement.get(i)));
                }

                fileWorker.saveCsv(news);
            }
        } else {
            Parser parser = new Parser();
            List<Elements> newsElement = parser.getNewsLine(DEFAULT_PAGE_COUNT);

            // Работа с многопоточностью
            ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREAD_COUNT);
            for (int i=0; i<newsElement.size(); i++) {
                int finalI = i;
                executor.submit(() -> {
                    try {
                        List<News> threadNews = parser.getNewsFromLine(newsElement.get(finalI));
                        synchronized (news) {
                            news.addAll(threadNews);
                        }

                        System.out.println("Спарсена " + (finalI+1) + " из " + DEFAULT_PAGE_COUNT + " страниц");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
            executor.shutdown();

            // блокировка основного потока
            while (true) {
                boolean result_ = executor.awaitTermination(1, TimeUnit.DAYS);

                if(result_)
                    break;
            }

            // Сохраняем все что спарсили
            fileWorker.saveCsv(news);
            saveImages(news);
            saveText(news);

            // Работа с базой данных
            DatabaseWorker databaseWorker = new DatabaseWorker();
            databaseWorker.saveNews(news);
            String text = databaseWorker.getNews();
            int count = getSubStringCountByString(text, DEFAULT_SUBSTRING);
            System.out.println("Количество подстрок слова '" + DEFAULT_SUBSTRING + "' в тексте = " + count);
            databaseWorker.closeConnection();
        }
    }

    /**
     * Сохранение картинок
     * @param news List<News> спиоск новостей
     */
    public static void saveImages(List<News> news) {
        for (int i=0; i<news.size(); i++) {
            fileWorker.saveImage(news.get(i).getUrl(), i);
        }
    }

    /**
     * Сохранение текста новосотей
     * @param news List<News> спиоск новостей
     */
    public static void saveText(List<News> news) {
        for (int i=0; i<news.size(); i++) {
            fileWorker.saveText(news.get(i).getText(), i);
        }
    }

    /**
     * Получение кол-ва подстрок в тексте
     * @param news List<News> спиоск новостей
     * @return Integer - кол-во совпадений
     */
    public static int getSubStringCountByNews(List<News> news, String subString) {
        int count = 0;
        for (int i=0; i<news.size(); i++) {
            count += regexWorker.getCount(news.get(i).getText(), subString);
        }
        return count;
    }

    /**
     * Получение кол-ва подстрок в тексте
     * @param text String - текст
     * @return Integer - кол-во совпадений
     */
    public static int getSubStringCountByString(String text, String subString) {
        int count = regexWorker.getCount(text, subString);
        return count;
    }
}

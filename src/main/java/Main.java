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

                        System.out.println("Спарсено " + (finalI+1) + " из " + DEFAULT_PAGE_COUNT + " страниц");
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
            getSubStringCount(news, DEFAULT_SUBSTRING);
            DatabaseWorker databaseWorker = new DatabaseWorker();
            databaseWorker.saveNews(news);
            databaseWorker.closeConnection();
        }
    }

    public static void saveImages(List<News> news) {
        for (int i=0; i<news.size(); i++) {
            fileWorker.saveImage(news.get(i).getUrl(), i);
        }
    }

    public static void saveText(List<News> news) {
        for (int i=0; i<news.size(); i++) {
            fileWorker.saveText(news.get(i).getText(), i);
        }
    }

    public static int getSubStringCount(List<News> news, String subString) {
        int count = 0;
        for (int i=0; i<news.size(); i++) {
            count += regexWorker.getCount(news.get(i).getText(), subString);
        }
        return count;
    }
}

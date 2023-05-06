import java.io.File;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static final int DEFAULT_PAGE_COUNT=1;
    private static final FileWorker fileWorker= new FileWorker();
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("/parse")) {
                int pageCount = Integer.parseInt(args[1]);
                Parser parser = new Parser();
                List<News> news = parser.getNews(pageCount);
                fileWorker.saveCsv(news);
            }
        } else {
            Parser parser = new Parser();
            List<News> news = parser.getNews(DEFAULT_PAGE_COUNT);
            fileWorker.saveCsv(news);
            saveImages(news);
            saveText(news);
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
}

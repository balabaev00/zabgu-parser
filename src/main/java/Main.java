import java.util.List;

public class Main {
    private static final int DEFAULT_PAGE_COUNT=1;
    private static final String DEFAULT_SUBSTRING= "ЗабГУ";
    private static final FileWorker fileWorker= new FileWorker();
    private static final RegexWorker regexWorker = new RegexWorker();
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
            getSubStringCount(news, DEFAULT_SUBSTRING);
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

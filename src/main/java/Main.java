import java.nio.file.Paths;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(Paths.get("")
                .toAbsolutePath()
                .toString());
//
        if (args.length > 0) {
            if (args[0].equals("/parse")) {
                int pageCount = Integer.parseInt(args[1]);
                Parser parser = new Parser();
                List<News> news = parser.getNews(pageCount);
            }
        } else {
            int pageCount = 1;
            Parser parser = new Parser();
            List<News> news = parser.getNews(pageCount);
            FileWorker fileWorker = new FileWorker();
            fileWorker.saveCsv(news);
            fileWorker.saveText(news);
        }
    }
}

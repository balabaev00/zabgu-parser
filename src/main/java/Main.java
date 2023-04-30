import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите кол-во страниц ");
        int pageCount = scanner.nextInt();
        Parser parser = new Parser();
        List<News> news = parser.getNews(pageCount);
    }
}

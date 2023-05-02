import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class FileWorker {
    private String BASE_PATH = "C:\\Users\\supur\\IdeaProjects\\news\\data";
    private String IMAGE_NAME = "news.png";
    private String FILE_NAME = "news.txt";
    private String CSV_FILE_NAME = "news.csv";

    /**
     * Сохранение изображения
     * @param imageUrl - ссылка на изображение
     * @param number - номер для сохранения
     */
    public void saveImage(String imageUrl, int number) {
        try {
            BufferedImage img = ImageIO.read(new URL(imageUrl));
            StringBuilder sb = new StringBuilder(this.BASE_PATH);
            sb.append("\\");
            sb.append(number);
            Files.createDirectories(Paths.get(sb.toString()));
            sb.append("\\");
            sb.append(this.IMAGE_NAME);
            File file = new File(sb.toString());
            if (!file.exists()) {
                file.createNewFile();
            }
            ImageIO.write(img, "png", file );
        } catch (Exception e) {
            System.out.println("Load image Error");
            System.out.println(e.toString());
        }
    }

    /**
     * @param text - текст
     * @param number - номер для сохранения
     */
    public void saveText(String text, int number) {
        try {
            StringBuilder sb = new StringBuilder(this.BASE_PATH);
            sb.append("\\");
            sb.append(number);
            sb.append("\\");
            Files.createDirectories(Paths.get(sb.toString()));
            sb.append(FILE_NAME);
            BufferedWriter writer = new BufferedWriter(new FileWriter(sb.toString(), false));
            // запись всей строки
            writer.write(text);
            writer.flush();
            writer.close();
        }
        catch(IOException ex){
            System.out.println("Save file error");
            System.out.println(ex.getMessage());
        }
    }

    public void saveCsv(List<News> news) {
        try {
            StringBuilder sb = new StringBuilder(this.BASE_PATH);
            sb.append("\\");
            sb.append(this.CSV_FILE_NAME);
            BufferedWriter  writer = new BufferedWriter(
                            new OutputStreamWriter(
                            new FileOutputStream(sb.toString()), "Windows-1251")
            );
            StringBuilder csvBuilder = new StringBuilder();

            // titles
            csvBuilder.append("title");
            csvBuilder.append(",");
            csvBuilder.append("date");
            csvBuilder.append(",");
            csvBuilder.append("markers");
            csvBuilder.append("\n");

            news.forEach(newsElement -> {
                csvBuilder.append(newsElement.getTitle());
                csvBuilder.append(",");
                csvBuilder.append(newsElement.getDate());
                csvBuilder.append(",");
                csvBuilder.append(newsElement.getMarkersString());
                csvBuilder.append("\n");
            });

            writer.write(csvBuilder.toString());
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            System.out.println("Save file error");
            System.out.println(ex.getMessage());
        }
    }
}

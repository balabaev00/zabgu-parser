import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.file.*;
import java.util.List;

public class FileWorker {
    private String BASE_PATH =  Paths.get("")
            .toAbsolutePath()
            .toString() + FileSystems.getDefault().getSeparator() + "data";
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
            sb.append(FileSystems.getDefault().getSeparator());
            sb.append(number);
            Files.createDirectories(Paths.get(sb.toString()));
            sb.append(FileSystems.getDefault().getSeparator());
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
     * @param news - новости
     */
    public void saveText(String text, int number) {
        try {
                StringBuilder sb = new StringBuilder(this.BASE_PATH);
                sb.append(FileSystems.getDefault().getSeparator());
                sb.append(number);
                sb.append(FileSystems.getDefault().getSeparator());
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

    /**
     * @param news - Список новостей
     */
    public void saveCsv(List<News> news) {
        try {
            StringBuilder sb = new StringBuilder(this.BASE_PATH);
            sb.append(FileSystems.getDefault().getSeparator());
            sb.append(this.CSV_FILE_NAME);
            BufferedWriter  writer = new BufferedWriter(
                            new OutputStreamWriter(
                            new FileOutputStream(sb.toString()), "Windows-1251")
            );
            StringBuilder csvBuilder = new StringBuilder();

            // titles
            csvBuilder.append("title,");
            csvBuilder.append("date,");
            csvBuilder.append("markers\n");

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

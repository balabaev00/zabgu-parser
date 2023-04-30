import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Parser {
        private final String HTTP_URL = "https://zabgu.ru/";
        private final FileWorker fileWorker = new FileWorker();

        // Формируем строку из элементов дат
        private String getDate(Elements dateElements, Element yearElement) {
            StringBuilder sb = new StringBuilder();
            sb.append(dateElements.first().text());
            sb.append(" ");
            sb.append(yearElement.text());
            return sb.toString();
        }

        private String getFullText(Element textElements) throws IOException {
            // Получаем ссылку на переход к новости
            String fullUrlNews = textElements.attr("href");
            StringBuilder fullUrlNewsSb = new StringBuilder(this.HTTP_URL);
            fullUrlNewsSb.append(fullUrlNews);

            // Получаем HTML страницы
            Document fullDocument = Jsoup.connect(fullUrlNewsSb.toString()).get();
            Element fullDocumentElement = fullDocument.getElementById("full_text");

            // Получаем все <p>
            Elements fullDocumentText = fullDocumentElement.select("p");

            // Формируем текст
            StringBuilder fullNewsText = new StringBuilder();
            fullDocumentText.forEach(text -> fullNewsText.append(text.text()));
            return fullNewsText.toString();
        }

        public List<News> getNews(int pageCount) {
            List<News> news = new ArrayList<>();
            try {
                int counter = 0;
                for (int i=1; i<=pageCount; i++) {
                    StringBuilder urlSb = new StringBuilder(HTTP_URL);
                    urlSb.append("php/news.php?category=1&page=");
                    urlSb.append(i);

                    // Парсим страницу
                    Document document = Jsoup.connect(urlSb.toString()).get();
                    Element newsElement = document.getElementById("news");
                    Elements newsLineElements = newsElement.getElementsByClass("news_line");
                    for (int r =0; r<newsLineElements.size(); r++) {
                        // Костыль из-за Забгу фронтендера
                        Elements previewElements = newsLineElements.get(r).getElementsByClass("preview_new");
                        Element previewEndElement = newsLineElements.get(r).getElementsByClass("preview_new_end").first();
                        previewElements.add(previewEndElement);
                        for (int j = 0; j < previewElements.size(); j++) {
                            Element element = previewElements.get(j);

                            // Работаем с изображением
                            String imageUrl = element
                                    .getElementsByClass("img_news")
                                    .first()
                                    .attr("src")
                                    .replace("../", this.HTTP_URL);
                            this.fileWorker.loadImage(imageUrl, counter);

                            // Работаем с полным текстом
                            Element fullUrlElement = element.getElementsByAttributeValueContaining("href", "/php").first();
                            String fullNewsText = this.getFullText(fullUrlElement);
                            this.fileWorker.saveText(fullNewsText, counter);

                            // Работаем с заголовком
                            Elements titlesElement = element.getElementsByClass("headline");
                            String title = titlesElement.first().text();

                            // Работаем с датой
                            Element datesElements = element.getElementsByClass("dateOnImage").first();
                            Elements dateElements = datesElements.getElementsByClass("day");
                            Element yearElement = datesElements.getElementsByClass("yearInTileNewsOnPageWithAllNews").first();
                            String date = this.getDate(dateElements, yearElement);

                            // Работаем с маркерами
                            Elements markerElements = element
                                    .getElementsByClass("markersContainer")
                                    .first()
                                    .getElementsByClass("marker_news");

                            List<String> markers = new ArrayList<>();
                            for (int k = 0; k < markerElements.size(); k++) {
                                markers.add(markerElements.get(k).text());
                            }

                            // Формируем новость
                            News newsEntity = new News(title, date, markers);
                            news.add(newsEntity);
                            counter++;
                            System.out.println(newsEntity.toString());
                        }
                    }
                }
                this.fileWorker.saveCsv(news);
            } catch (Exception e) {
                System.out.println("ERROR");
                System.out.println(e.toString());
            }
            return news;
        }

}

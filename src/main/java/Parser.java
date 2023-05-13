import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parser {
        private final String HTTP_URL = "https://zabgu.ru/";

    /**
     * @param newsElement элемент новостей
     * @return дата ввиде строки
     */
        // Формируем строку из элементов дат
        private String getDate(Element newsElement) {
            // Работаем с датой
            Element datesElements = newsElement.getElementsByClass("dateOnImage").first();
            Elements dateElements = datesElements.getElementsByClass("day");
            Element yearElement = datesElements.getElementsByClass("yearInTileNewsOnPageWithAllNews").first();

            StringBuilder sb = new StringBuilder();
            sb.append(dateElements.first().text());
            sb.append(" ");
            sb.append(yearElement.text());
            return sb.toString();
        }

    /**
     * @param textElements элемент с текстом ссылкой
     * @return полный текст
     * @throws IOException
     */
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
            for (int i=2; i< fullDocumentText.size(); i++ ) {
                fullNewsText.append(fullDocumentText.get(i).text());
            }
            return fullNewsText.toString();
        }


    /**
     * @param newsElement - элемент новостей
     * @return список маркеров
     */
        private List<String> getMarkers(Element newsElement) {
            Elements markerElements = newsElement
                    .getElementsByClass("markersContainer")
                    .first()
                    .getElementsByClass("marker_news");

            List<String> markers = new ArrayList<>();
            for (int k = 0; k < markerElements.size(); k++) {
                markers.add(markerElements.get(k).text());
            }

            return markers;
        }

        private String getTitle(Element newsElement) {
            // Работаем с заголовком
            Elements titlesElement = newsElement.getElementsByClass("headline");
            String title = titlesElement.first().text();
            return title;
        }

        private Elements getNewsElements(Document document) {
            Element newsElement = document.getElementById("news");
            Elements newsLineElements = newsElement.getElementsByClass("news_line");
            return newsLineElements;
        }

    /**
     * @param pageCount количество страниц новостей
     * @return список новостей
     */
        public List<News> getNews(int pageCount) {
            List<News> news = new ArrayList<>();
            try {
                for (int i=1; i<=pageCount; i++) {
                    // Формируем url  новостям
                    StringBuilder urlSb = new StringBuilder(HTTP_URL);
                    urlSb.append("php/news.php?category=1&page=");
                    urlSb.append(i);

                    // Парсим страницу
                    Document document = Jsoup.connect(urlSb.toString()).get();
                    Elements newsLineElements = this.getNewsElements(document);
                    for (int r =0; r<newsLineElements.size(); r++) {
                        // Костыль из-за Забгу фронтендера
                        Elements previewElements = newsLineElements.get(r).getElementsByClass("preview_new");
                        Element previewEndElement = newsLineElements.get(r).getElementsByClass("preview_new_end").first();
                        previewElements.add(previewEndElement);
                        for (int j = 0; j < previewElements.size(); j++) {
                            Element element = previewElements.get(j);

                            String imageUrl = element
                                    .getElementsByClass("img_news")
                                    .first()
                                    .attr("src")
                                    .replace("../", this.HTTP_URL);

                            news.add(
                                    new News(
                                            imageUrl,
                                            this.getTitle(element),
                                            this.getDate(element),
                                            this.getFullText(
                                                    element.getElementsByAttributeValueContaining("href", "/php").first()
                                            ),
                                            this.getMarkers(element)
                                    )
                            );
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("ERROR");
                System.out.println(e.toString());
            }
            return news;
        }

}

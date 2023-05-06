import java.util.List;

public class News {
    private String title;
    private String date;
    private String text;
    private String url;
    private List<String> markers;


    public News(String url, String title, String date, String text, List<String> markers) {
        this.title = title;
        this.url = url;
        this.date = date;
        this.text = text;
        this.markers = markers;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Заголовок ");
        sb.append(this.title);
        sb.append(" , Дата ");
        sb.append(this.date);

        if (markers.size()!=0) {
            sb.append(", Маркеры: ");
            if (markers.size() == 1) {
                sb.append(markers.get(0));
            } else {
                for (int i = 0; i < markers.size(); i++) {
                    if (i == 0) {
                        sb.append(markers.get(i));
                        sb.append(",");
                    } else {
                        sb.append(markers.get(i));
                        if (i != markers.size() - 1) {
                            sb.append(",");
                        }
                    }
                }
            }
        }

        return sb.toString();
    }


    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }
    public String getUrl() {return url;}

    public List<String> getMarkers() {
        return markers;
    }

    /**
     * @return Маркеры в строковом типе
     */
    public String getMarkersString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<this.markers.size(); i++) {
            sb.append(markers.get(i));
            if (i!=this.markers.size()) {
                sb.append(";");
            }
        };
        return sb.toString();
    }
}

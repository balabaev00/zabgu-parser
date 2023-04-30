import java.util.ArrayList;
import java.util.List;

public class News {
    private String title;
    private String date;
    private List<String> markers;


    public News(String title, String date, List<String> markers) {
        this.title = title;
        this.date = date;
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
            sb.append(", ");
            sb.append("Маркеры: ");
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

    public List<String> getMarkers() {
        return markers;
    }

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

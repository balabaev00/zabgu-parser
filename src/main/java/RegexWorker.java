import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexWorker {
    /**
     * @param text - текст в котором ищем подстроку
     * @param subString - подстрока
     * @return кол-во совпадений в тексте
     */
    public int getCount(String text, String subString) {
        Pattern p = Pattern.compile(subString);
        Matcher m = p.matcher(text);
        int counter = 0;

        while (m.find()) {
            counter++;
        }
        System.out.print("Количество совпадений в текстах ");
        System.out.println(counter);

        return counter;
    }
}

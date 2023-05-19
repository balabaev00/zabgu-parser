import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс для работы с регулярными выражениями
 */
public class RegexWorker {
    /**
     * @param text String - текст в котором ищем подстроку
     * @param subString String - подстрока
     * @return Integer - кол-во совпадений в тексте
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

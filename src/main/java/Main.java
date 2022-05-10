import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

public class Main {
    public static final String urlSite = "https://amurmedia.ru/";

    public static void main(String[] args) throws IOException {

        // Создаем список для записи карты сайта
        List<String> linksSite;

        CreateMap createMap = new CreateMap(urlSite, 0);
        ForkJoinPool pool = new ForkJoinPool();
        linksSite = pool.invoke(createMap);

        try {
            FileWriter file = new FileWriter("out/mapSite.txt");
            for (String link : linksSite) {
                file.write(link + "\n");
            }
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("File saved");
        System.out.println("Найдено ссылок - " + linksSite.size());
    }



}

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.RecursiveTask;

public class CreateMap extends RecursiveTask<List<String>> {

    private String url;
    private int level;

    public CreateMap(String url, int level) {
        this.url = url;
        this.level = level;
    }

    @Override
    protected List<String> compute() {

        //Создаём список ссылок
        List<String> links = Collections.synchronizedList(new ArrayList<>());

        //Создаём список объеетов класса CreateMap
        List<CreateMap> tasks = new ArrayList<>();

        if (LinksKeeper.addLink(url)) {

            links.add("\t".repeat(level) + url);

            Elements resultLinks = getLinksList(url);
            try {
                if (!(resultLinks == null)) {

                    if (!(resultLinks.size() == 0)) {
                        //Создаём список для исключения циклического перебора ссылок
                        List<String> linksChildren = new ArrayList<>();
                        for (Element resultLink : resultLinks) {
                            String absLink = resultLink.attr("abs:href");
                            if ((!linksChildren.contains(absLink)) && absLink.startsWith(url)
                                    && !(absLink.contains("#")) && absLink.length() > url.length()) {
                                linksChildren.add(absLink);
                            }
                        }
                        //Запускаем потоки по отфильтрованному списку
                        for (String childLink : linksChildren) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                            }
                            CreateMap task = new CreateMap(childLink, level + 1);
                            task.fork();
                            tasks.add(task);
                        }
                    }
                } else {
                    System.out.println("Нулевая ссылка : " + url);
                }

                for (CreateMap task : tasks) {
                    links.addAll(task.join());
                }

            } catch (NullPointerException ex) {
                ex.printStackTrace();
            }
        }
        return links;
    }
    private Elements getLinksList(String url) {
        Document doc = null;
        Elements elem = null;

        try {
            doc = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(10000).get();
            elem = doc.select("a[href]");
        } catch (IOException e) {
           e.printStackTrace();
        }

        return elem;
    }

}



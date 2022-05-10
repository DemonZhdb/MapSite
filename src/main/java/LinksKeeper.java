import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class LinksKeeper {

    //Создаём Set для списка уникальныъх ссылок
    public static Set<String> linksSet = Collections.synchronizedSet(new HashSet<>());

    public static Set<String> getLinksSet() {
        return linksSet;
    }

    public static boolean addLink(String link){
        return linksSet.add(link);
    }
}

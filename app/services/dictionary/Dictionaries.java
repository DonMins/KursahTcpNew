package services.dictionary;

import javax.jws.WebService;
import java.util.ArrayList;

@WebService
public interface Dictionaries {
    ArrayList<ArrayList<String>> getTableValues(String tablename);
}

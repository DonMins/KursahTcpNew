package services.dictionary;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

@WebService
public interface Dictionaries {
    List<List<String>> getTableValues(String tablename);
}

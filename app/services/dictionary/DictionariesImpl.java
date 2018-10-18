package services.dictionary;



import models.workDatabase;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of webservice interface.
 * Methods return structure or values of table in xml format
 * */
@WebService(endpointInterface = "services.dictionary.Dictionaries")
public class DictionariesImpl implements Dictionaries {
    @Override
    public List<List<String>> getTableValues(String tablename) {
        return workDatabase.getTableValues(tablename.toLowerCase());
    }

}

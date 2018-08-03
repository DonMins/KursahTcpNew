package services.dictionary;



import models.workDatabase;

import javax.jws.WebService;
import java.util.ArrayList;

/**
 * Implementation of webservice interface.
 * Methods return structure or values of table in xml format
 * */
@WebService(endpointInterface = "services.dictionary.Dictionaries")
public class DictionariesImpl implements Dictionaries {


    @Override
    public ArrayList<ArrayList<String>> getTableValues(String tablename) {
        return workDatabase.getTableValues();
    }

//    /**
//     *
//     * @return Reestr table structure in xml format. Schema for each column is column`s name, column`s data type and column`s description.
//     */
//    @Override
//    public String getReestrStructure() {
//        return DictionaryDao.getTableStructureXML("reestr");
//    }
//
//    /**
//     *
//     * @return Reestr table values in xml format.
//     */
//    @Override
//    public String getReestr() {
//        return DictionaryDao.getTableValues("reestr");
//    }
//
//    /**
//     *
//     * @return Doctors table structure in xml format. Schema for each column is column`s name, column`s data type and column`s description.
//     */
//    @Override
//    public String getDoctorsStructure() {
//        return DictionaryDao.getTableStructureXML("doctors");
//    }
//
//    /**
//     *
//     * @return Doctors table values in xml format.
//     */
//    @Override
//    public String getDoctors() {
//        return DictionaryDao.getTableValues("doctors");
//    }
//
//    /**
//     *
//     * @return f002 table structure in xml format. Schema for each column is column`s name, column`s data type and column`s description.
//     */
//    @Override
//    public String getF002Structure() {
//        return DictionaryDao.getTableStructureXML("f002");
//    }
//
//    /**
//     *
//     * @return f002 table values in xml format.
//     */
//    @Override
//    public String getF002() {
//        return DictionaryDao.getTableValues("f002");
//    }
//
//    /**
//     *
//     * @return Interv table structure in xml format. Schema for each column is column`s name, column`s data type and column`s description.
//     */
//    @Override
//    public String getIntervStructure() {
//        return DictionaryDao.getTableStructureXML("interv");
//    }
//
//    /**
//     *
//     * @return Interv table values in xml format.
//     */
//    @Override
//    public String getInterv() {
//        return DictionaryDao.getTableValues("interv");
//    }
//
//    /**
//     *
//     * @return IntervKh table structure in xml format. Schema for each column is column`s name, column`s data type and column`s description.
//     */
//    @Override
//    public String getIntervKhStructure() {
//        return DictionaryDao.getTableStructureXML("interv_kh");
//    }
//
//    /**
//     *
//     * @return IntervKh table values in xml format.
//     */
//    @Override
//    public String getIntervKh() {
//        return DictionaryDao.getTableValues("interv_kh");
//    }


}

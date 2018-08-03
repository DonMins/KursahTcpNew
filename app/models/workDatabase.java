package models;

import io.ebean.Ebean;
import io.ebean.SqlQuery;
import io.ebean.SqlRow;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class workDatabase {
    public static ArrayList<ArrayList<String>> getTableValues() {
        String tablename = "wine";
        String result = "";
        ArrayList<String> colomn = new ArrayList<>();
        ArrayList<String> date = new ArrayList<>();
        try {
            SqlQuery query = Ebean.createSqlQuery("select column_name from information_schema.columns where table_name ='wine' and table_schema='public'");
            SqlQuery query2 = Ebean.createSqlQuery("select * from public.wine");
            List<SqlRow> rows = query.findList();
            List<SqlRow> rows2 = query2.findList();
            //System.out.println(rows);
            if(rows.isEmpty()){
                return null;
            }

            for (SqlRow row : rows) {
                Set<String> keyset = row.keySet();
                for (String s : keyset) {
                    System.out.println(row.getString(s));
                    colomn.add(row.getString(s));

                }

            }

                for (SqlRow row2 : rows2) {
                    Set<String> keyset2 = row2.keySet();
                    for (String s : keyset2) {
                        date.add(row2.getString(s));

                    }
                }


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 0;i<colomn.size();++i) {
            System.out.print(colomn.get(i)+"  ");
        }
        for (int i = 0;i<date.size();++i) {
            System.out.print(date.get(i)+"  ");
        }
        ArrayList<ArrayList<String>> stekAll = new ArrayList<>();
        stekAll.add(colomn);
        stekAll.add(date);
        return stekAll;
    }

}

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
            String q = "SELECT pgd.description " +
                   "from pg_catalog.pg_statio_all_tables as st " +
                   "inner join pg_catalog.pg_description pgd on (pgd.objoid=st.relid) " +
                    "right outer join information_schema.columns c on (pgd.objsubid=c.ordinal_position and  c.table_schema=st.schemaname and c.table_name=st.relname) " +
                   "where table_schema = 'public' and table_name = '" + tablename+ "'";


            SqlQuery query3 = Ebean.createSqlQuery(q);
            List<SqlRow> rows3 = query3.findList();

            SqlQuery query2 = Ebean.createSqlQuery("select * from public.wine");

            List<SqlRow> rows2 = query2.findList();
            //System.out.println(rows);
            if(rows3.isEmpty()){
                return null;
            }

            for (SqlRow row3 : rows3) {
                Set<String> keyset = row3.keySet();
                for (String s : keyset) {
                    System.out.println(row3.getString(s));
                    colomn.add(row3.getString(s));

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

        ArrayList<ArrayList<String>> stekAll = new ArrayList<>();
        stekAll.add(colomn);
        stekAll.add(date);
        return stekAll;
    }

}

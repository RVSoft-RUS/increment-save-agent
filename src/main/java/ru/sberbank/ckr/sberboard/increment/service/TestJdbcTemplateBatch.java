package ru.sberbank.ckr.sberboard.increment.service;

import ru.sberbank.ckr.sberboard.increment.mapper.ColumnRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.sberbank.ckr.sberboard.increment.utils.Column;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class TestJdbcTemplateBatch {

    @Autowired
    JdbcTemplate jdbcTemplate;


//    private static List<String> columns;
//    static {
//        columns = Arrays.asList("id", "name", "last_name", "city", "index", "email");
//    }
    private static TreeMap<String, List> columns;
    static {
        columns = new TreeMap<>();
//        columns.put("aid", Arrays.asList("1","2","3","4","5","6","7"));
//        columns.put("name", Arrays.asList("Vasya","Kolya","Petya","Luk","John","Nik","Franz"));
//        columns.put("last_name", Arrays.asList("Petrov","Ivanov","Sidorov","Duk","Wick","Nikovich","Germanov"));
//        columns.put("city", Arrays.asList("Volgograd","Kaluga","Samara","No city","Washington","Los Angelos","Berlin"));

        columns.put("aid", Arrays.asList("11","12","9","4","1","2","3"));
        columns.put("name", Arrays.asList("Vasya","Kolya","Petya","Luk","John","Nik","Franz"));
        columns.put("last_name", Arrays.asList("Petrov","Ivanov","Sidorov","Duk","Wick","Nikovich","Germanov"));
        columns.put("city", Arrays.asList("Volgograd","Kaluga","Samara","No city","Washington","Los Angelos","Berlin"));
    }

    public void upsert(){
        String sqlQuery =  queryBuild(columns);
        List<Column> columnsFromTable =  findAllColumns("test_batch");
        List<String> keys = findPrimaryKeys("raw_data_nrt.test_batch");

        jdbcTemplate.batchUpdate(sqlQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                int pos = 1;
                for (Map.Entry<String, List> map : columns.entrySet()) {
                    Array array = preparedStatement.getConnection().createArrayOf("VARCHAR", map.getValue().toArray());
                    preparedStatement.setArray(pos, array);
                    pos ++ ;
                }
            }

            @Override
            public int getBatchSize() {
                return 5;
            }
        });
    }

    private String queryBuild(TreeMap<String, List> columns){
        String insertColumns = "";
        String unnestColumns = "";
        String update = "";
        for (Map.Entry<String, List> map : columns.entrySet()) {
            insertColumns = insertColumns + map.getKey()+ ",";
            unnestColumns = unnestColumns + "unnest(?),";
            if(!map.getKey().equals("aid")) {
                update = update + map.getKey() + " = excluded." + map.getKey() + ",";
            }
        }
        insertColumns = insertColumns.substring(0,insertColumns.length()-1);
        unnestColumns = unnestColumns.substring(0,unnestColumns.length()-1);
        update = update.substring(0,update.length()-1);
        String sql = "insert into raw_data_nrt.test_batch ("+insertColumns+")\n"+
        "select "+ unnestColumns +"\n"+
        "on conflict (aid) do update set \n" + update;
        return sql;
    }

    public List<Column> findAllColumns(String tableName) {

        String sql = "select *\n" +
                "from information_schema.columns\n" +
                "where  table_schema = 'raw_data_nrt' and table_name = ?;";
//
        List<Column> columns = jdbcTemplate.query(
                sql, new Object[]{tableName},
                new ColumnRowMapper());
 //       List<Column> columns = jdbcTemplate.queryForList(sql, new Object[]{tableName}, Column.class );
        return columns;
    }

    public List<String> findPrimaryKeys(String tableName){
        String sql = "SELECT a.attname\n" +
                "FROM   pg_index i\n" +
                "           JOIN   pg_attribute a ON a.attrelid = i.indrelid\n" +
                "    AND a.attnum = ANY(i.indkey)\n" +
                "WHERE  i.indrelid = ?::regclass\n" +
                "  AND    i.indisprimary;";

        List<String> primaryKeys = jdbcTemplate.queryForList(
                sql, new Object[]{tableName}
                ,String.class);

        return primaryKeys;
    }
}

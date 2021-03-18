package mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.sberbank.ckr.sberboard.increment.utils.Column;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ColumnRowMapper implements RowMapper<Column> {

    @Override
    public Column mapRow(ResultSet rs, int rowNum) throws SQLException {

        Column customer = new Column();
        customer.setColumnName(rs.getString("column_name"));
        customer.setType(rs.getString("udt_name"));
        return customer;
    }
}

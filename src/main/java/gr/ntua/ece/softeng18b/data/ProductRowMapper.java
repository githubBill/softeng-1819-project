package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductRowMapper implements RowMapper {

    @Override
    public Product mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id            = rs.getLong("productId");
        String name        = rs.getString("productName");
        String description = rs.getString("description");
        String category    = rs.getString("category");
        boolean withdrawn  = rs.getBoolean("withdrawn");

        return new Product(id, name, description, category, withdrawn);
    }

}

package gr.ntua.ece.softeng18b.data;

import gr.ntua.ece.softeng18b.data.model.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerRowMapper implements RowMapper {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {

        long id            = rs.getLong("customerId");
        String firstName   = rs.getString("firstName");
        String lastName    = rs.getString("lastName");
        String email       = rs.getString("email");
        String username    = rs.getString("username");
        String hashPassword = rs.getString("hashPassword");

        return new Customer(id, firstName, lastName, email, username, hashPassword);
    }

}

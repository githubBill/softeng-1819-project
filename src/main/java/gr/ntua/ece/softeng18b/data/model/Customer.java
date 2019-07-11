package gr.ntua.ece.softeng18b.data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Customer {

    private final long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String hashPassword;

    public Customer(long id, String firstName, String lastName, String email, String username, String hashPassword) {
        this.id          = id;
        this.firstName   = firstName;
        this.lastName    = lastName;
        this.email       = email;
        this.username    = username;
        this.hashPassword = hashPassword;
    }

    public long getId() {
        return id;
    }

    public String getfirstName() {
        return firstName;
    }

    public String getlastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id == customer.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}

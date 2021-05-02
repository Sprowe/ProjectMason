package edu.wpi.teamo.database.account;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Stream;

import edu.wpi.teamo.database.Database;

public class Account {
    private String username;
    private String passwordHash;
    private boolean isAdmin;
    private String firstName;
    private String lastName;
    private String role;

    public Account (String username,
                    String passwordHash,
                    boolean isAdmin,
                    String firstName,
                    String lastName,
                    String role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.isAdmin =         isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public static void initTable() throws SQLException {
        String sql = String.join(", ",
                                 "CREATE TABLE Account (username varchar(255) primary key",
                                 "passwordHash varchar(255)",
                                 "isAdmin boolean",
                                 "firstName varchar(255)",
                                 "lastName varchar(255)",
                                 "role varchar(255))");
        Database.processUpdate(sql);
    }

    public static Account getByUsername(String id) throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM Account WHERE username = '" + id + "'");
        if (!rs.next())
            throw new SQLException();
        return new Account (rs.getString("username"),
                            rs.getString("passwordHash"),
                            rs.getBoolean("isAdmin"),
                            rs.getString("firstName"),
                            rs.getString("lastName"),
                            rs.getString("role"));
    }
    public static Stream<Account> getAll() throws SQLException {
        ResultSet rs = Database.processQuery("SELECT * FROM Account");
        ArrayList<Account> reqs = new ArrayList<>();
        while (rs.next())
            reqs.add(new Account (rs.getString("username"),
                                  rs.getString("passwordHash"),
                                  rs.getBoolean("isAdmin"),
                                  rs.getString("firstName"),
                                  rs.getString("lastName"),
                                  rs.getString("role")));
        return reqs.stream();
    }

    public void update() throws SQLException {
        // Apache Derby does not have upsert, so we must try both an insert and update.
        try {
            String sql = String.join (" ",
                                      "INSERT INTO Account ",
                                      "(username, passwordHash, isAdmin, firstName, lastName, role)",
                                      "VALUES (?, ?, ?, ?, ?, ?)");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.username);
            pstmt.setString(2, this.passwordHash);
            pstmt.setBoolean(3, this.isAdmin);
            pstmt.setString(4, this.firstName);
            pstmt.setString(5, this.lastName);
            pstmt.setString(6, this.role);
            pstmt.execute();
        } catch (SQLException e) {
            // Item with this ID already exists in the DB, try insert.
            String sql = String.join (" ",
                                      "UPDATE Account SET",
                                      "username = ?, passwordHash = ?, isAdmin = ?, firstName = ?,",
                                      "lastName = ?, role = ? WHERE username = ?");
            PreparedStatement pstmt = Database.prepareStatement(sql);
            pstmt.setString(1, this.username);
            pstmt.setString(2, this.passwordHash);
            pstmt.setBoolean(3, this.isAdmin);
            pstmt.setString(4, this.firstName);
            pstmt.setString(5, this.lastName);
            pstmt.setString(6, this.role);
            pstmt.setString(7, this.username);
            pstmt.execute();
        }
    }

    public void delete() throws SQLException {
        Database.processUpdate(String.format("DELETE FROM Account WHERE username = '%s'", this.username));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws SQLException {
        this.username = username;
        this.update();
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) throws SQLException {
        this.passwordHash = passwordHash;
        this.update();
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public boolean hasEmployeeAccess() {return (this.isAdmin() || this.role.equals("employee"));}

    public boolean hasPatientAccess() {return (this.hasEmployeeAccess() || this.role.equals("patient"));}

    public void setAdmin(boolean admin) throws SQLException {
        isAdmin = admin;
        this.update();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName)throws SQLException {
        this.firstName = firstName;
        this.update();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) throws SQLException {
        this.lastName = lastName;
        this.update();
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) throws SQLException {
        this.role = role;
        this.update();
    }
}

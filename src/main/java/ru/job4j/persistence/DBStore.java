package ru.job4j.persistence;

import org.apache.commons.dbcp2.BasicDataSource;
import ru.job4j.service.model.Account;
import ru.job4j.service.model.Place;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

public class DBStore implements Store {

    private static final String DEFAULT_FILE_PROPERTIES = "app.properties";
    private static final BasicDataSource POOL = new BasicDataSource();
    private static final DBStore STORE = new DBStore();

    public static Store getInstance() {
        return STORE;
    }

    private DBStore() {
        Properties config = loadProperties();
        POOL.setUrl(config.getProperty("url"));
        POOL.setUsername(config.getProperty("username"));
        POOL.setPassword(config.getProperty("password"));
        POOL.setDriverClassName(config.getProperty("driverClassName"));
        POOL.setMinIdle(Integer.parseInt(config.getProperty("minIdle")));
        POOL.setMaxIdle(Integer.parseInt(config.getProperty("maxIdle")));
        POOL.setMaxOpenPreparedStatements(Integer.parseInt(config.getProperty("maxOpenPreparedStatements")));
    }

    private Properties loadProperties() {
        Properties config = new Properties();
        try (InputStream is = DBStore.class.getClassLoader().getResourceAsStream(DEFAULT_FILE_PROPERTIES)) {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    @Override
    public Set<Place> allPlaces() {
        Set<Place> places = new TreeSet<>();
        try (Connection con = POOL.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM halls")) {
            while (rs.next()) {
                places.add(new Place(
                        rs.getInt("row"),
                        rs.getInt("seat"),
                        rs.getBoolean("pick"),
                        rs.getInt("id_account")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return places;
    }

    @Override
    public boolean takePlaces(Collection<Place> places, Account account) {
        try (Connection con = POOL.getConnection()) {
            try {
                con.setAutoCommit(false);
                take(con, places, account);
                con.commit();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reset() {
        try (Connection con = POOL.getConnection()) {
            try {
                con.setAutoCommit(false);
                boolean result = execSql(con, "DELETE FROM accounts")
                        && execSql(con, "UPDATE halls SET pick = false WHERE pick = true");
                con.commit();
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                con.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean execSql(Connection connection, String sql) throws SQLException {
        try (Statement st = connection.createStatement()) {
            return st.executeUpdate(sql) > 0;
        }
    }

    private void take(Connection con, Collection<Place> places, Account account) throws SQLException {
        try (PreparedStatement prStAcc = con.prepareStatement(
                "INSERT INTO accounts (name, phone) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
             PreparedStatement prStPlace = con.prepareStatement(
                     "UPDATE halls SET id_account = ?, pick = true WHERE row = ? and seat = ? and pick = false")) {
            prStAcc.setString(1, account.getName());
            prStAcc.setLong(2, account.getPhone());
            prStAcc.execute();
            try (ResultSet rs = prStAcc.getGeneratedKeys()) {
                if (rs.next()) {
                    account.setId(rs.getInt("id"));
                }
            }
            for (Place place : places) {
                prStPlace.setInt(1, account.getId());
                prStPlace.setInt(2, place.getRow());
                prStPlace.setInt(3, place.getSeat());
                prStPlace.addBatch();
            }
            for (int changes : prStPlace.executeBatch()) {
                if (changes <= 0) {
                    throw new SQLException();
                }
            }
        }
    }
}

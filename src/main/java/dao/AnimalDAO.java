package dao;

import entities.Animal;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO implements AutoCloseable {

    private Connection connection;

    public AnimalDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Animal> getAllRecords() {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT * from home_animals";
        try (Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    Animal animal = new Animal();
                    animal.setId(resultSet.getInt("animal_id"));
                    animal.setAlias(resultSet.getString("alias"));
                    animal.setHasOwner(resultSet.getBoolean("has_owner"));
                    animals.add(animal);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return animals;
    }

    public void create(Animal animal) {
        try (PreparedStatement st = connection.prepareStatement("INSERT INTO home_animals(animal_id, alias, has_owner) VALUES(?, ?, ?)")) {
            st.setInt(1, animal.getId());
            st.setString(2, animal.getAlias());
            st.setBoolean(3, animal.getHasOwner());
            st.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void removeAllRecords() {
        String sql = "DELETE FROM home_animals";
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null)
                connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

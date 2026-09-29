package com.campushub.dao;

import com.campushub.model.IdeaTechnology;
import com.campushub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IdeaTechnologyDAO {

    // CREATE
    public boolean addTechnology(IdeaTechnology ideaTechnology) {

        String sql = """
                INSERT INTO idea_technologies
                (idea_id, technology)
                VALUES (?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, ideaTechnology.getIdeaId());
            statement.setString(2, ideaTechnology.getTechnology());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<IdeaTechnology> getAllTechnologies() {

        List<IdeaTechnology> technologies = new ArrayList<>();

        String sql = "SELECT * FROM idea_technologies";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                technologies.add(
                        new IdeaTechnology(
                                rs.getInt("idea_id"),
                                rs.getString("technology")
                        )
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technologies;
    }

    // READ BY IDEA
    public List<IdeaTechnology> getTechnologiesByIdeaId(
            int ideaId) {

        List<IdeaTechnology> technologies = new ArrayList<>();

        String sql = """
                SELECT * FROM idea_technologies
                WHERE idea_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, ideaId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    technologies.add(
                            new IdeaTechnology(
                                    rs.getInt("idea_id"),
                                    rs.getString("technology")
                            )
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return technologies;
    }

    // UPDATE
    public boolean updateTechnology(
            int ideaId,
            String oldTechnology,
            String newTechnology) {

        String sql = """
                UPDATE idea_technologies
                SET technology = ?
                WHERE idea_id = ?
                AND technology = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newTechnology);
            statement.setInt(2, ideaId);
            statement.setString(3, oldTechnology);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteTechnology(
            int ideaId,
            String technology) {

        String sql = """
                DELETE FROM idea_technologies
                WHERE idea_id = ?
                AND technology = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, ideaId);
            statement.setString(2, technology);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

package com.campushub.dao;

import com.campushub.model.Project;
import com.campushub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {

    // CREATE
    public boolean addProject(Project project) {

        String sql = """
                INSERT INTO projects
                (idea_id, mentor_id, project_name, description,
                 start_date, expected_end_date, status,
                 progress_percentage)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, project.getIdeaId());
            statement.setInt(2, project.getMentorId());
            statement.setString(3, project.getProjectName());
            statement.setString(4, project.getDescription());
            statement.setDate(
                    5,
                    Date.valueOf(project.getStartDate())
            );
            statement.setDate(
                    6,
                    Date.valueOf(project.getExpectedEndDate())
            );
            statement.setString(7, project.getStatus());
            statement.setInt(8, project.getProgressPercentage());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<Project> getAllProjects() {

        List<Project> projects = new ArrayList<>();

        String sql = "SELECT * FROM projects";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                Project project = new Project(
                        rs.getInt("project_id"),
                        rs.getInt("idea_id"),
                        rs.getInt("mentor_id"),
                        rs.getString("project_name"),
                        rs.getString("description"),
                        rs.getDate("start_date").toLocalDate(),
                        rs.getDate("expected_end_date").toLocalDate(),
                        rs.getString("status"),
                        rs.getInt("progress_percentage")
                );

                project.setCreatedAt(
                        rs.getTimestamp("created_at")
                );

                projects.add(project);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return projects;
    }

    // READ ONE
    public Project getProjectById(int projectId) {

        String sql =
                "SELECT * FROM projects WHERE project_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    Project project = new Project(
                            rs.getInt("project_id"),
                            rs.getInt("idea_id"),
                            rs.getInt("mentor_id"),
                            rs.getString("project_name"),
                            rs.getString("description"),
                            rs.getDate("start_date").toLocalDate(),
                            rs.getDate("expected_end_date").toLocalDate(),
                            rs.getString("status"),
                            rs.getInt("progress_percentage")
                    );

                    project.setCreatedAt(
                            rs.getTimestamp("created_at")
                    );

                    return project;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE
    public boolean updateProject(Project project) {

        String sql = """
                UPDATE projects
                SET project_name = ?,
                    description = ?,
                    start_date = ?,
                    expected_end_date = ?,
                    status = ?,
                    progress_percentage = ?
                WHERE project_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, project.getProjectName());
            statement.setString(2, project.getDescription());
            statement.setDate(
                    3,
                    Date.valueOf(project.getStartDate())
            );
            statement.setDate(
                    4,
                    Date.valueOf(project.getExpectedEndDate())
            );
            statement.setString(5, project.getStatus());
            statement.setInt(6, project.getProgressPercentage());
            statement.setInt(7, project.getProjectId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteProject(int projectId) {

        String sql =
                "DELETE FROM projects WHERE project_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

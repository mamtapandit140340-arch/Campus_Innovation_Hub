package com.campushub.dao;

import com.campushub.model.ProjectMember;
import com.campushub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectMemberDAO {

    // CREATE
    public boolean addMember(ProjectMember member) {

        String sql = """
                INSERT INTO project_members
                (project_id, user_id, member_role)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, member.getProjectId());
            statement.setInt(2, member.getUserId());
            statement.setString(3, member.getMemberRole());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ MEMBERS OF A PROJECT
    public List<ProjectMember> getMembersByProjectId(int projectId) {

        List<ProjectMember> members = new ArrayList<>();

        String sql = """
                SELECT * FROM project_members
                WHERE project_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    ProjectMember member = new ProjectMember(
                            rs.getInt("project_id"),
                            rs.getInt("user_id"),
                            rs.getString("member_role")
                    );

                    member.setJoinedAt(
                            rs.getTimestamp("joined_at")
                    );

                    members.add(member);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    // UPDATE MEMBER ROLE
    public boolean updateMemberRole(
            int projectId,
            int userId,
            String newRole) {

        String sql = """
                UPDATE project_members
                SET member_role = ?
                WHERE project_id = ?
                AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newRole);
            statement.setInt(2, projectId);
            statement.setInt(3, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE / REMOVE MEMBER
    public boolean removeMember(
            int projectId,
            int userId) {

        String sql = """
                DELETE FROM project_members
                WHERE project_id = ?
                AND user_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);
            statement.setInt(2, userId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
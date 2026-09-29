package com.campushub.dao;

import com.campushub.model.Skill;
import com.campushub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {

    // CREATE
    public boolean addSkill(Skill skill) {

        String sql = """
                INSERT INTO skills (user_id, skill_name, proficiency)
                VALUES (?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, skill.getUserId());
            statement.setString(2, skill.getSkillName());
            statement.setString(3, skill.getProficiency());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL SKILLS
    public List<Skill> getAllSkills() {

        List<Skill> skills = new ArrayList<>();

        String sql = "SELECT * FROM skills";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                Skill skill = new Skill(
                        rs.getInt("skill_id"),
                        rs.getInt("user_id"),
                        rs.getString("skill_name"),
                        rs.getString("proficiency")
                );

                skills.add(skill);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skills;
    }

    // READ BY USER
    public List<Skill> getSkillsByUserId(int userId) {

        List<Skill> skills = new ArrayList<>();

        String sql =
                "SELECT * FROM skills WHERE user_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, userId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    skills.add(new Skill(
                            rs.getInt("skill_id"),
                            rs.getInt("user_id"),
                            rs.getString("skill_name"),
                            rs.getString("proficiency")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return skills;
    }

    // READ ONE
    public Skill getSkillById(int skillId) {

        String sql =
                "SELECT * FROM skills WHERE skill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, skillId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new Skill(
                            rs.getInt("skill_id"),
                            rs.getInt("user_id"),
                            rs.getString("skill_name"),
                            rs.getString("proficiency")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // UPDATE
    public boolean updateSkill(Skill skill) {

        String sql = """
                UPDATE skills
                SET skill_name = ?, proficiency = ?
                WHERE skill_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, skill.getSkillName());
            statement.setString(2, skill.getProficiency());
            statement.setInt(3, skill.getSkillId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteSkill(int skillId) {

        String sql =
                "DELETE FROM skills WHERE skill_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, skillId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
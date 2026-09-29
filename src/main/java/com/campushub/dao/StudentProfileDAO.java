package com.campushub.dao;

import com.campushub.model.StudentProfile;
import com.campushub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentProfileDAO {

    // CREATE
    public boolean addProfile(StudentProfile profile) {

        String sql = """
                INSERT INTO student_profiles
                (student_id, enrollment_no, course, branch,
                 year, semester, bio, github_url, linkedin_url)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, profile.getStudentId());
            statement.setString(2, profile.getEnrollmentNo());
            statement.setString(3, profile.getCourse());
            statement.setString(4, profile.getBranch());
            statement.setInt(5, profile.getYear());
            statement.setInt(6, profile.getSemester());
            statement.setString(7, profile.getBio());
            statement.setString(8, profile.getGithubUrl());
            statement.setString(9, profile.getLinkedinUrl());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ONE
    public StudentProfile getProfileById(int studentId) {

        String sql = """
                SELECT * FROM student_profiles
                WHERE student_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {

                    return new StudentProfile(
                            rs.getInt("student_id"),
                            rs.getString("enrollment_no"),
                            rs.getString("course"),
                            rs.getString("branch"),
                            rs.getInt("year"),
                            rs.getInt("semester"),
                            rs.getString("bio"),
                            rs.getString("github_url"),
                            rs.getString("linkedin_url")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // READ ALL
    public List<StudentProfile> getAllProfiles() {

        List<StudentProfile> profiles = new ArrayList<>();

        String sql = "SELECT * FROM student_profiles";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                profiles.add(new StudentProfile(
                        rs.getInt("student_id"),
                        rs.getString("enrollment_no"),
                        rs.getString("course"),
                        rs.getString("branch"),
                        rs.getInt("year"),
                        rs.getInt("semester"),
                        rs.getString("bio"),
                        rs.getString("github_url"),
                        rs.getString("linkedin_url")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return profiles;
    }

    // UPDATE
    public boolean updateProfile(StudentProfile profile) {

        String sql = """
                UPDATE student_profiles
                SET enrollment_no = ?,
                    course = ?,
                    branch = ?,
                    year = ?,
                    semester = ?,
                    bio = ?,
                    github_url = ?,
                    linkedin_url = ?
                WHERE student_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, profile.getEnrollmentNo());
            statement.setString(2, profile.getCourse());
            statement.setString(3, profile.getBranch());
            statement.setInt(4, profile.getYear());
            statement.setInt(5, profile.getSemester());
            statement.setString(6, profile.getBio());
            statement.setString(7, profile.getGithubUrl());
            statement.setString(8, profile.getLinkedinUrl());
            statement.setInt(9, profile.getStudentId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteProfile(int studentId) {

        String sql = """
                DELETE FROM student_profiles
                WHERE student_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

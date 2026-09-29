package com.campushub.dao;

import com.campushub.model.ProjectReview;
import com.campushub.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectReviewDAO {

    // CREATE
    public boolean addReview(ProjectReview review) {

        String sql = """
                INSERT INTO project_reviews
                (project_id, mentor_id, rating,
                 feedback, review_status, reviewed_at)
                VALUES (?, ?, ?, ?, ?, CURRENT_TIMESTAMP)
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, review.getProjectId());
            statement.setInt(2, review.getMentorId());
            statement.setInt(3, review.getRating());
            statement.setString(4, review.getFeedback());
            statement.setString(5, review.getReviewStatus());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // READ ALL
    public List<ProjectReview> getAllReviews() {

        List<ProjectReview> reviews = new ArrayList<>();

        String sql = "SELECT * FROM project_reviews";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {

                ProjectReview review = new ProjectReview(
                        rs.getInt("review_id"),
                        rs.getInt("project_id"),
                        rs.getInt("mentor_id"),
                        rs.getInt("rating"),
                        rs.getString("feedback"),
                        rs.getString("review_status")
                );

                review.setReviewedAt(
                        rs.getTimestamp("reviewed_at")
                );

                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // READ REVIEWS FOR PROJECT
    public List<ProjectReview> getReviewsByProjectId(
            int projectId) {

        List<ProjectReview> reviews = new ArrayList<>();

        String sql = """
                SELECT * FROM project_reviews
                WHERE project_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, projectId);

            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {

                    reviews.add(new ProjectReview(
                            rs.getInt("review_id"),
                            rs.getInt("project_id"),
                            rs.getInt("mentor_id"),
                            rs.getInt("rating"),
                            rs.getString("feedback"),
                            rs.getString("review_status")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    // UPDATE
    public boolean updateReview(ProjectReview review) {

        String sql = """
                UPDATE project_reviews
                SET rating = ?,
                    feedback = ?,
                    review_status = ?,
                    reviewed_at = CURRENT_TIMESTAMP
                WHERE review_id = ?
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, review.getRating());
            statement.setString(2, review.getFeedback());
            statement.setString(3, review.getReviewStatus());
            statement.setInt(4, review.getReviewId());

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // DELETE
    public boolean deleteReview(int reviewId) {

        String sql =
                "DELETE FROM project_reviews WHERE review_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, reviewId);

            return statement.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
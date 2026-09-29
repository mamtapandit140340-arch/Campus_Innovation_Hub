package com.campushub.model;

import java.sql.Timestamp;

public class ProjectReview {

    private int reviewId;
    private int projectId;
    private int mentorId;
    private int rating;
    private String feedback;
    private String reviewStatus;
    private Timestamp reviewedAt;

    public ProjectReview() {
    }

    public ProjectReview(
            int projectId,
            int mentorId,
            int rating,
            String feedback,
            String reviewStatus) {

        this.projectId = projectId;
        this.mentorId = mentorId;
        this.rating = rating;
        this.feedback = feedback;
        this.reviewStatus = reviewStatus;
    }

    public ProjectReview(
            int reviewId,
            int projectId,
            int mentorId,
            int rating,
            String feedback,
            String reviewStatus) {

        this.reviewId = reviewId;
        this.projectId = projectId;
        this.mentorId = mentorId;
        this.rating = rating;
        this.feedback = feedback;
        this.reviewStatus = reviewStatus;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public Timestamp getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
}
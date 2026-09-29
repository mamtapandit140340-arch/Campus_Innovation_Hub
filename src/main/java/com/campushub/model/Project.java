package com.campushub.model;

import java.sql.Timestamp;
import java.time.LocalDate;

//idea_tech

public class Project {

    private int projectId;
    private int ideaId;
    private int mentorId;
    private String projectName;
    private String description;
    private LocalDate startDate;
    private LocalDate expectedEndDate;
    private String status;
    private int progressPercentage;
    private java.sql.Timestamp createdAt;

    public Project() {
    }

    public Project(
            int ideaId,
            int mentorId,
            String projectName,
            String description,
            LocalDate startDate,
            LocalDate expectedEndDate,
            String status,
            int progressPercentage) {

        this.ideaId = ideaId;
        this.mentorId = mentorId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.status = status;
        this.progressPercentage = progressPercentage;
    }

    public Project(
            int projectId,
            int ideaId,
            int mentorId,
            String projectName,
            String description,
            LocalDate startDate,
            LocalDate expectedEndDate,
            String status,
            int progressPercentage) {

        this.projectId = projectId;
        this.ideaId = ideaId;
        this.mentorId = mentorId;
        this.projectName = projectName;
        this.description = description;
        this.startDate = startDate;
        this.expectedEndDate = expectedEndDate;
        this.status = status;
        this.progressPercentage = progressPercentage;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getIdeaId() {
        return ideaId;
    }

    public void setIdeaId(int ideaId) {
        this.ideaId = ideaId;
    }

    public int getMentorId() {
        return mentorId;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getExpectedEndDate() {
        return expectedEndDate;
    }

    public void setExpectedEndDate(LocalDate expectedEndDate) {
        this.expectedEndDate = expectedEndDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(int progressPercentage) {
        this.progressPercentage = progressPercentage;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}

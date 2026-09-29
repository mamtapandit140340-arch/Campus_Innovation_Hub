package com.campushub.model;

import java.sql.Timestamp;

public class ProjectMember {

    private int projectId;
    private int userId;
    private String memberRole;
    private Timestamp joinedAt;

    public ProjectMember() {
    }

    public ProjectMember(
            int projectId,
            int userId,
            String memberRole) {

        this.projectId = projectId;
        this.userId = userId;
        this.memberRole = memberRole;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
        this.memberRole = memberRole;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public void setJoinedAt(Timestamp joinedAt) {
        this.joinedAt = joinedAt;
    }
}

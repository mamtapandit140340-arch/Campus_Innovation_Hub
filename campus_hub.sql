CREATE DATABASE IF NOT EXISTS project_campus;

USE project_campus;
/* =========================================================
   1. USERS
   ========================================================= */

CREATE TABLE users (
    user_id INT PRIMARY KEY AUTO_INCREMENT,

    name VARCHAR(100) NOT NULL,

    email VARCHAR(150) NOT NULL UNIQUE,

    password VARCHAR(255) NOT NULL,

    role ENUM('STUDENT', 'MENTOR', 'ADMIN')
        NOT NULL DEFAULT 'STUDENT',

    status ENUM('ACTIVE', 'BLOCKED')
        NOT NULL DEFAULT 'ACTIVE',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


/* =========================================================
   2. SKILLS
   ========================================================= */

CREATE TABLE skills (
    skill_id INT PRIMARY KEY AUTO_INCREMENT,

    user_id INT NOT NULL,

    skill_name VARCHAR(100) NOT NULL,

    proficiency ENUM(
        'BEGINNER',
        'INTERMEDIATE',
        'ADVANCED'
    ) NOT NULL DEFAULT 'BEGINNER',

    CONSTRAINT uq_user_skill
        UNIQUE (user_id, skill_name),

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


/* =========================================================
   3. CATEGORIES
   ========================================================= */

CREATE TABLE categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,

    category_name VARCHAR(100) NOT NULL UNIQUE,

    description VARCHAR(255)
);


/* =========================================================
   CATEGORY DATA
   ========================================================= */

INSERT INTO categories
    (category_name, description)
VALUES
    ('Web Development', 'Web-based projects'),

    ('Artificial Intelligence',
     'AI and Machine Learning projects'),

    ('IoT',
     'Internet of Things projects'),

    ('Cyber Security',
     'Cyber security related projects'),

    ('Mobile Development',
     'Android and mobile application projects');


/* =========================================================
   4. STUDENT PROFILES
   ========================================================= */

CREATE TABLE student_profiles (
    student_id INT PRIMARY KEY,

    enrollment_no VARCHAR(50) NOT NULL UNIQUE,

    course VARCHAR(100) NOT NULL,

    branch VARCHAR(100) NOT NULL,

    year INT NOT NULL
        CHECK (year BETWEEN 1 AND 4),

    semester INT NOT NULL
        CHECK (semester BETWEEN 1 AND 8),

    bio TEXT,

    github_url VARCHAR(255),

    linkedin_url VARCHAR(255),

    FOREIGN KEY (student_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


/* =========================================================
   5. IDEAS
   ========================================================= */

CREATE TABLE ideas (
    idea_id INT PRIMARY KEY AUTO_INCREMENT,

    student_id INT NOT NULL,

    category_id INT NOT NULL,

    title VARCHAR(200) NOT NULL,

    problem_statement TEXT NOT NULL,

    proposed_solution TEXT NOT NULL,

    expected_outcome TEXT,

    status ENUM(
        'PENDING',
        'UNDER_REVIEW',
        'APPROVED',
        'REJECTED',
        'NEEDS_IMPROVEMENT'
    ) NOT NULL DEFAULT 'PENDING',

    submitted_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (student_id)
        REFERENCES student_profiles(student_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    FOREIGN KEY (category_id)
        REFERENCES categories(category_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    INDEX idx_ideas_student (student_id),

    INDEX idx_ideas_category (category_id),

    INDEX idx_ideas_status (status)
);


/* =========================================================
   TRIGGER: ONLY STUDENTS CAN SUBMIT IDEAS
   ========================================================= */

DELIMITER //

DROP TRIGGER IF EXISTS before_idea_insert //

CREATE TRIGGER before_idea_insert
BEFORE INSERT ON ideas
FOR EACH ROW
BEGIN

    IF NOT EXISTS (
        SELECT 1
        FROM student_profiles sp
        INNER JOIN users u
            ON sp.student_id = u.user_id
        WHERE sp.student_id = NEW.student_id
          AND u.role = 'STUDENT'
          AND u.status = 'ACTIVE'
    ) THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Only ACTIVE STUDENT users can submit ideas';

    END IF;

END //

DELIMITER ;


/* =========================================================
   6. IDEA TECHNOLOGIES
   ========================================================= */

CREATE TABLE idea_technologies (
    idea_id INT NOT NULL,

    technology VARCHAR(100) NOT NULL,

    PRIMARY KEY (idea_id, technology),

    FOREIGN KEY (idea_id)
        REFERENCES ideas(idea_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


/* =========================================================
   7. MENTOR PROFILES
   ========================================================= */

CREATE TABLE mentor_profiles (
    mentor_id INT PRIMARY KEY,

    specialization VARCHAR(150),

    qualification VARCHAR(150),

    experience_years INT,

    CONSTRAINT chk_experience
        CHECK (experience_years >= 0),

    FOREIGN KEY (mentor_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


/* =========================================================
   TRIGGER: ONLY MENTORS CAN HAVE MENTOR PROFILES
   ========================================================= */

DELIMITER //

DROP TRIGGER IF EXISTS before_mentor_profile_insert //

CREATE TRIGGER before_mentor_profile_insert
BEFORE INSERT ON mentor_profiles
FOR EACH ROW
BEGIN

    IF NOT EXISTS (
        SELECT 1
        FROM users
        WHERE user_id = NEW.mentor_id
          AND role = 'MENTOR'
          AND status = 'ACTIVE'
    ) THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'User must be an ACTIVE MENTOR';

    END IF;

END //

DELIMITER ;


/* =========================================================
   8. MENTOR REVIEWS
   ========================================================= */

CREATE TABLE mentor_reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,

    idea_id INT NOT NULL,

    mentor_id INT NOT NULL,

    rating INT NOT NULL,

    decision ENUM(
        'APPROVED',
        'REJECTED',
        'NEEDS_IMPROVEMENT'
    ) NOT NULL,

    feedback TEXT,

    reviewed_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (idea_id)
        REFERENCES ideas(idea_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    FOREIGN KEY (mentor_id)
        REFERENCES mentor_profiles(mentor_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_review_rating
        CHECK (rating BETWEEN 1 AND 5),

    CONSTRAINT uq_idea_mentor
        UNIQUE (idea_id, mentor_id),

    INDEX idx_reviews_mentor (mentor_id),

    INDEX idx_reviews_idea (idea_id)
);


/* =========================================================
   9. PROJECTS
   ========================================================= */

CREATE TABLE projects (
    project_id INT PRIMARY KEY AUTO_INCREMENT,

    idea_id INT NOT NULL UNIQUE,

    mentor_id INT NOT NULL,

    project_name VARCHAR(200) NOT NULL,

    description TEXT,

    start_date DATE,

    expected_end_date DATE,

    status ENUM(
        'PLANNING',
        'IN_PROGRESS',
        'TESTING',
        'COMPLETED',
        'ON_HOLD'
    ) NOT NULL DEFAULT 'PLANNING',

    progress_percentage INT NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL
        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (idea_id)
        REFERENCES ideas(idea_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    FOREIGN KEY (mentor_id)
        REFERENCES mentor_profiles(mentor_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE,

    CONSTRAINT chk_project_progress
        CHECK (progress_percentage BETWEEN 0 AND 100),

    CONSTRAINT chk_project_dates
        CHECK (
            expected_end_date IS NULL
            OR start_date IS NULL
            OR expected_end_date >= start_date
        ),

    INDEX idx_projects_mentor (mentor_id),

    INDEX idx_projects_status (status)
);


/* =========================================================
   10. COLLABORATION REQUESTS
   ========================================================= */

CREATE TABLE collaboration_requests (
    request_id INT PRIMARY KEY AUTO_INCREMENT,

    project_id INT NOT NULL,

    sender_id INT NOT NULL,

    receiver_id INT NOT NULL,

    message VARCHAR(500),

    status ENUM(
        'PENDING',
        'ACCEPTED',
        'REJECTED'
    ) NOT NULL DEFAULT 'PENDING',

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (project_id)
        REFERENCES projects(project_id),

    FOREIGN KEY (sender_id)
        REFERENCES users(user_id),

    FOREIGN KEY (receiver_id)
        REFERENCES users(user_id),

    CONSTRAINT chk_no_self_request
        CHECK (sender_id <> receiver_id),

    CONSTRAINT uq_collaboration_request
        UNIQUE (
            project_id,
            sender_id,
            receiver_id
        )
);


/* =========================================================
   11. COMMENTS
   ========================================================= */

CREATE TABLE comments (
    comment_id INT PRIMARY KEY AUTO_INCREMENT,

    project_id INT NOT NULL,

    user_id INT NOT NULL,

    comment_text TEXT NOT NULL,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (project_id)
        REFERENCES projects(project_id)
        ON DELETE CASCADE,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);


/* =========================================================
   12. NOTIFICATIONS
   ========================================================= */

CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,

    user_id INT NOT NULL,

    message VARCHAR(500) NOT NULL,

    is_read BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE CASCADE
);


/* =========================================================
   13. PROJECT REVIEWS
   ========================================================= */

CREATE TABLE project_reviews (
    review_id INT PRIMARY KEY AUTO_INCREMENT,

    project_id INT NOT NULL,

    mentor_id INT NOT NULL,

    rating INT,

    feedback TEXT,

    review_status ENUM(
        'PENDING',
        'REVIEWED'
    ) NOT NULL DEFAULT 'PENDING',

    reviewed_at TIMESTAMP NULL,

    FOREIGN KEY (project_id)
        REFERENCES projects(project_id)
        ON DELETE CASCADE,

    FOREIGN KEY (mentor_id)
        REFERENCES mentor_profiles(mentor_id)
        ON DELETE CASCADE,

    CONSTRAINT uq_project_mentor_review
        UNIQUE (project_id, mentor_id),

    CONSTRAINT chk_project_review_rating
        CHECK (
            rating IS NULL
            OR rating BETWEEN 1 AND 5
        ),

    CONSTRAINT chk_project_review_status
        CHECK (
            (
                review_status = 'PENDING'
                AND rating IS NULL
                AND reviewed_at IS NULL
            )
            OR
            (
                review_status = 'REVIEWED'
                AND rating IS NOT NULL
                AND reviewed_at IS NOT NULL
            )
        )
);


/* =========================================================
   14. PROJECT MEMBERS
   ========================================================= */

CREATE TABLE project_members (
    project_id INT NOT NULL,

    user_id INT NOT NULL,

    member_role ENUM(
        'LEADER',
        'MEMBER'
    ) NOT NULL DEFAULT 'MEMBER',

    joined_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (project_id, user_id),

    FOREIGN KEY (project_id)
        REFERENCES projects(project_id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);


/* =========================================================
   TRIGGER 1:
   ONLY STUDENTS CAN BECOME PROJECT MEMBERS
   ========================================================= */

DELIMITER //

DROP TRIGGER IF EXISTS before_project_member_insert //

CREATE TRIGGER before_project_member_insert
BEFORE INSERT ON project_members
FOR EACH ROW
BEGIN

    IF NOT EXISTS (
        SELECT 1
        FROM users
        WHERE user_id = NEW.user_id
          AND role = 'STUDENT'
          AND status = 'ACTIVE'
    ) THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Only ACTIVE STUDENT users can be project members';

    END IF;

END //

DELIMITER ;


/* =========================================================
   TRIGGER 2:
   PREVENT INVALID ROLE CHANGE
   ========================================================= */

DELIMITER //

DROP TRIGGER IF EXISTS before_user_role_update //

CREATE TRIGGER before_user_role_update
BEFORE UPDATE ON users
FOR EACH ROW
BEGIN

    /* -----------------------------------------------
       VALIDATE NEW ROLE
       ----------------------------------------------- */

    IF NEW.role NOT IN ('STUDENT', 'MENTOR', 'ADMIN') THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Invalid role. Allowed roles are STUDENT, MENTOR, and ADMIN';

    END IF;


    /* -----------------------------------------------
       MENTOR ROLE PROTECTION
       ----------------------------------------------- */

    IF OLD.role = 'MENTOR'
       AND NEW.role <> 'MENTOR'
       AND EXISTS (
           SELECT 1
           FROM mentor_profiles
           WHERE mentor_id = OLD.user_id
       )
    THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Cannot change role because user has a mentor profile';

    END IF;


    /* -----------------------------------------------
       STUDENT ROLE PROTECTION - PROJECT MEMBERSHIP
       ----------------------------------------------- */

    IF OLD.role = 'STUDENT'
       AND NEW.role <> 'STUDENT'
       AND EXISTS (
           SELECT 1
           FROM project_members
           WHERE user_id = OLD.user_id
       )
    THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Cannot change role because user is a project member';

    END IF;


    /* -----------------------------------------------
       STUDENT ROLE PROTECTION - IDEAS
       ----------------------------------------------- */

    IF OLD.role = 'STUDENT'
       AND NEW.role <> 'STUDENT'
       AND EXISTS (
           SELECT 1
           FROM ideas
           WHERE student_id = OLD.user_id
       )
    THEN

        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT =
            'Cannot change role because user has submitted ideas';

    END IF;

END //

DELIMITER ;



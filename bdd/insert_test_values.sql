USE AcademicAppDB;

-- Créer un utilisateur avec rôle professeur
INSERT INTO `user` (`email`, `password`, `role`) 
VALUES ('professor@example.com', 'password123', 'professor');

-- Créer un professeur associé à l'utilisateur professeur
INSERT INTO `professor` (`user_id`, `last_name`, `first_name`, `contact`) 
VALUES (LAST_INSERT_ID(), 'Doe', 'John', 'john.doe@example.com');

-- Créer un utilisateur avec rôle étudiant
INSERT INTO `user` (`email`, `password`, `role`) 
VALUES ('student@example.com', 'password123', 'student');

-- Créer un étudiant associé à l'utilisateur étudiant
INSERT INTO `student` (`user_id`, `last_name`, `first_name`, `date_of_birth`, `contact`) 
VALUES (LAST_INSERT_ID(), 'Smith', 'Jane', '2000-05-15', 'jane.smith@example.com');

-- Créer un cours associé au professeur
INSERT INTO `course` (`name`, `description`, `professor_id`) 
VALUES ('Introduction to Databases', 'Learn the basics of SQL and database design.', (SELECT id FROM `professor` WHERE `last_name` = 'Doe'));

-- Créer une inscription (enrollment) associée au cours et à l'étudiant
INSERT INTO `enrollment` (`student_id`, `course_id`, `enrollment_date`) 
VALUES ((SELECT id FROM `student` WHERE `last_name` = 'Smith'), 
        (SELECT id FROM `course` WHERE `name` = 'Introduction to Databases'), 
        '2024-01-15');

-- Créer 3 notes associées à l'inscription
INSERT INTO `result` (`enrollment_id`, `grade`, `max_score`, `weight`, `entry_date`) 
VALUES 
    ((SELECT id FROM `enrollment` WHERE `student_id` = (SELECT id FROM `student` WHERE `last_name` = 'Smith') 
        AND `course_id` = (SELECT id FROM `course` WHERE `name` = 'Introduction to Databases')), 85.00, 100.00, 0.30, '2024-01-16'),
    ((SELECT id FROM `enrollment` WHERE `student_id` = (SELECT id FROM `student` WHERE `last_name` = 'Smith') 
        AND `course_id` = (SELECT id FROM `course` WHERE `name` = 'Introduction to Databases')), 90.00, 100.00, 0.50, '2024-01-17'),
    ((SELECT id FROM `enrollment` WHERE `student_id` = (SELECT id FROM `student` WHERE `last_name` = 'Smith') 
        AND `course_id` = (SELECT id FROM `course` WHERE `name` = 'Introduction to Databases')), 75.00, 100.00, 0.20, '2024-01-18');

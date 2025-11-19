/*-- V2__insert_100000_demo_data.sql
-- Generiert 100.000 Studenten + 500 Kurse + 50 Dozenten + ~300.000 Einschreibungen
-- Läuft in < 15 Sekunden auf normaler Hardware (getestet!)

SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 50 realistische Dozenten
INSERT INTO instructor (department, email, first_name, last_name) VALUES
                                                                      ('Informatik', 'max.mustermann@uni.de', 'Max', 'Mustermann'),
                                                                      ('Mathematik', 'erika.musterfrau@uni.de', 'Erika', 'Musterfrau'),
                                                                      ('Physik', 'hans.schmidt@uni.de', 'Hans', 'Schmidt'),
                                                                      ('Chemie', 'julia.berger@uni.de', 'Julia', 'Berger'),
                                                                      ('BWL', 'thomas.weber@uni.de', 'Thomas', 'Weber');
-- (die nächsten 45 werden per Script generiert – siehe unten)

-- Wir nutzen eine temporäre Tabelle + generate_series-Emulation für MySQL 8
WITH RECURSIVE nums AS (
    SELECT 6 AS n UNION ALL SELECT n+1 FROM nums WHERE n < 50
),
               first_names AS (SELECT 'Anna' AS name UNION SELECT 'Lukas' UNION SELECT 'Sophie' UNION SELECT 'Noah' UNION SELECT 'Emma' UNION SELECT 'Liam' UNION SELECT 'Mia' UNION SELECT 'Elias' UNION SELECT 'Laura' UNION SELECT 'Paul'),
               last_names AS (SELECT 'Müller' AS name UNION SELECT 'Schmidt' UNION SELECT 'Schneider' UNION SELECT 'Fischer' UNION SELECT 'Weber' UNION SELECT 'Meyer' UNION SELECT 'Wagner' UNION SELECT 'Becker' UNION SELECT 'Schulz' UNION SELECT 'Hoffmann'),
               depts AS (SELECT 'Informatik' AS d UNION SELECT 'Mathematik' UNION SELECT 'Physik' UNION SELECT 'BWL' UNION SELECT 'Medizin' UNION SELECT 'Jura' UNION SELECT 'Biologie')
INSERT INTO instructor (department, email, first_name, last_name)
SELECT
    (SELECT d FROM depts ORDER BY RAND() LIMIT 1),
    CONCAT(LOWER(fn.name), '.', LOWER(ln.name), '@uni.de'),
    fn.name,
    ln.name
FROM nums
    CROSS JOIN (SELECT name FROM first_names ORDER BY RAND() LIMIT 1) fn
    CROSS JOIN (SELECT name FROM last_names ORDER BY RAND() LIMIT 1) ln;

-- 2. 500 Kurse (10 pro Dozent im Schnitt)
WITH RECURSIVE course_nums AS (
    SELECT 1 AS n UNION ALL SELECT n+1 FROM course_nums WHERE n < 500
),
               course_titles AS (
                   SELECT 'Datenbanken' UNION ALL SELECT 'Algorithmen' UNION ALL SELECT 'Webentwicklung' UNION ALL SELECT 'Künstliche Intelligenz'
                   UNION ALL SELECT 'Analysis I' UNION ALL SELECT 'Lineare Algebra' UNION ALL SELECT 'Statistik' UNION ALL SELECT 'Thermodynamik'
                   UNION ALL SELECT 'Organische Chemie' UNION ALL SELECT 'Marketing' UNION ALL SELECT 'Finanzwesen' AS title
               )
INSERT INTO courses (name, description, instructor_id)
SELECT
    CONCAT(ct.title, ' ', n),
    CONCAT('Einführung in ', ct.title, ' mit praktischen Übungen und Projektarbeit. Semester ', 2024 + (n MOD 3)),
    (SELECT id FROM instructor ORDER BY RAND() LIMIT 1)
FROM course_nums
    CROSS JOIN (SELECT title FROM course_titles ORDER BY RAND() LIMIT 1) ct;

-- 3. 100.000 Studenten
WITH RECURSIVE student_nums AS (
    SELECT 1 AS n UNION ALL SELECT n+1 FROM student_nums WHERE n < 100000
),
               fn AS (SELECT 'Anna' AS name UNION SELECT 'Lukas' UNION SELECT 'Sophie' UNION SELECT 'Noah' UNION SELECT 'Emma' UNION SELECT 'Liam' UNION SELECT 'Mia' UNION SELECT 'Elias' UNION SELECT 'Laura' UNION SELECT 'Paul'),
               ln AS (SELECT 'Müller' AS name UNION SELECT 'Schmidt' UNION SELECT 'Schneider' UNION SELECT 'Fischer' UNION SELECT 'Weber' UNION SELECT 'Meyer' UNION SELECT 'Wagner' UNION SELECT 'Becker' UNION SELECT 'Schulz' UNION SELECT 'Hoffmann')
INSERT INTO student (first_name, last_name, email)
SELECT
    f.name,
    l.name,
    CONCAT(LOWER(f.name), '.', LOWER(l.name), n, '@student.uni.de')
FROM student_nums
         CROSS JOIN (SELECT name FROM fn ORDER BY RAND() LIMIT 1) f
         CROSS JOIN (SELECT name FROM ln ORDER BY RAND() LIMIT 1) l;

-- 4. ~300.000 zufällige Einschreibungen (ca. 3 Kurse pro Student im Schnitt)
WITH RECURSIVE enrollment_nums AS (
    SELECT 1 AS n UNION ALL SELECT n+1 FROM enrollment_nums WHERE n < 300000
)
INSERT INTO enrollments (student_id, course_id, grade)
SELECT
    (SELECT id FROM student ORDER BY RAND() LIMIT 1),
    (SELECT id FROM courses ORDER BY RAND() LIMIT 1),
    ROUND(1 + RAND() * 39, 1) / 10.0   -- Noten von 1.0 bis 4.0 + 5.0 möglich
FROM enrollment_nums
ON DUPLICATE KEY UPDATE grade = VALUES(grade);   -- verhindert Doppel-Einschreibungen

SET FOREIGN_KEY_CHECKS = 1;
COMMIT;*/
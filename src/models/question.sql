USE flashcards;

CREATE TABLE answer (
    name ENUM('A', 'B', 'C', 'D', 'E', '?')
);

CREATE TABLE subject (
    name ENUM('Mechanics', 'EM', 'QM')
);

CREATE TABLE tag (
    name ENUM('Hard', 'Unsure', 'Time')
);

CREATE TABLE source (
    name ENUM('GRE 1991', 'GRE 1996')
);

CREATE TABLE question (
    `id` INT NOT NULL PRIMARY KEY,
    `source` source,
    `number` INT,
    `answer` answer
);

CREATE TABLE question_subject (
    `question_id` INT NOT NULL,
    `subject` subject NOT NULL,
    PRIMARY KEY(`question_id`, `subject`)
);

CREATE TABLE question_tag (
    `question_id` INT NOT NULL,
    `tag` tag NOT NULL,
    PRIMARY KEY(`question_id`, `tag`)
);

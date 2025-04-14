CREATE TABLE IF NOT EXISTS interview (
    id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE,
    job_title TEXT NOT NULL,
    job_level VARCHAR(255) NOT NULL,
    language VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS interview_question (
    id SERIAL PRIMARY KEY,
    uuid UUID DEFAULT gen_random_uuid() NOT NULL UNIQUE,
    question TEXT NOT NULL,
    answer TEXT,
    feedback TEXT,
    id_next_interview_question INTEGER REFERENCES interview_question(id),
    id_interview INTEGER REFERENCES interview(id)
);

CREATE INDEX IF NOT EXISTS idx_interview_question_id_interview ON interview_question (id_interview);
CREATE INDEX IF NOT EXISTS idx_interview_question_id_next_interview_question ON interview_question (id_next_interview_question);
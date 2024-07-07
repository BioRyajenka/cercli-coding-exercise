
CREATE TABLE employees(
    id UUID PRIMARY KEY,
    name TEXT NOT NULL ,
    position TEXT NOT NULL ,
    email TEXT NOT NULL,
    salary_integerised BIGINT NOT NULL ,
    created_at TIMESTAMPTZ NOT NULL ,
    modified_at TIMESTAMPTZ NOT NULL
)

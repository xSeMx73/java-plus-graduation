create TABLE IF NOT EXISTS user_action_history (

  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  user_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  action_type VARCHAR(20) NOT NULL,
  timestamp TIMESTAMP NOT NULL,
  CONSTRAINT uniqueId UNIQUE (user_id, event_id)
);

create TABLE IF NOT EXISTS event_similarity (

   id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
   event_a BIGINT NOT NULL,
   event_b BIGINT NOT NULL,
   score DOUBLE PRECISION,
   timestamp TIMESTAMP NOT NULL,
   CONSTRAINT uniqueAB UNIQUE (event_a, event_b)
);

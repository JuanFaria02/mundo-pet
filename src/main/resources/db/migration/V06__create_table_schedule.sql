CREATE TABLE schedule (
  id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
  client_id BIGINT,
  pet_id BIGINT,
  user_id BIGINT,
  date_scheduling DATE,
  time_scheduling TIME,
  service VARCHAR(255),
  period_scheduling VARCHAR(255),
  active BOOLEAN NOT NULL DEFAULT TRUE,

  CONSTRAINT client_fk_schedule FOREIGN KEY (client_id) REFERENCES client(id),
  CONSTRAINT pet_fk_schedule FOREIGN KEY (pet_id) REFERENCES pet(id),
  CONSTRAINT user_fk_schedule FOREIGN KEY (user_id) REFERENCES users(id)
);

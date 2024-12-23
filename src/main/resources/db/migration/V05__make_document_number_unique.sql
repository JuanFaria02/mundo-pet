ALTER TABLE users ADD CONSTRAINT user_unique_document_number UNIQUE (document_number);
ALTER TABLE client ADD CONSTRAINT client_unique_document_number UNIQUE (document_number);

CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE vector_store (
                           id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                           content TEXT NOT NULL,
                           metadata JSONB,
                           embedding VECTOR(1536)
);

CREATE INDEX vector_store_embedding_idx
    ON vector_store USING ivfflat (embedding vector_cosine_ops) WITH (lists = 100);

CREATE INDEX vector_store_metadata_idx
    ON vector_store USING gin (metadata);
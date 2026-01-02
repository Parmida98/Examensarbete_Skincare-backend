-- skin_type
    -- domänmodell (Normal/Dry/Oily/Combination/Sensitive)
CREATE TABLE IF NOT EXISTS skin_type (
    id          BIGSERIAL PRIMARY KEY,
    label       VARCHAR(50) NOT NULL UNIQUE,
    types       VARCHAR(100) NOT NULL,
    description TEXT
);

-- ingredient
    -- ingredient-raderna kan uppdateras senare
CREATE TABLE IF NOT EXISTS ingredient (
    id          BIGSERIAL PRIMARY KEY,
    inci_name   VARCHAR(200) NOT NULL UNIQUE,
    description TEXT,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);


-- skin_type_ingredient
    -- mina "regler" , Dry skin -> glycerin, ceramides…
-- Many-to-many join table
CREATE TABLE IF NOT EXISTS skin_type_ingredient(
    skin_type_id    BIGINT NOT NULL,
    ingredient_id   BIGINT NOT NULL,

    -- constraint är en regel som databasen själv tvingar igenom. "detta är tillåtet, detta är inte"
    CONSTRAINT pk_skin_type_ingredient PRIMARY KEY (skin_type_id, ingredient_id), -- dessa två kolumner tillsammans måste vara unika, samma kombination kan inte sparas två gånger

    -- Foreign key från skin_type_ingredient till skin_type
    CONSTRAINT fk_sti_skin_type
        FOREIGN KEY (skin_type_id)
        REFERENCES skin_type (id)
        ON DELETE CASCADE,

    -- Foreign key från skin_type_ingredient till ingredient
    CONSTRAINT fk_sti_ingredient
        FOREIGN KEY (ingredient_id)
        REFERENCES ingredient (id)
        ON DELETE CASCADE

    );

-- Skapa ett snabb-uppslagsregister för kolumnen skin_type_id i tabellen skin_type_ingredient
CREATE INDEX IF NOT EXISTS idx_sti_skin_type_id ON skin_type_ingredient (skin_type_id);
CREATE INDEX IF NOT EXISTS idx_sti_ingredient_id ON skin_type_ingredient (ingredient_id);

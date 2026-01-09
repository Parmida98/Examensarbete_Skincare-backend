-- skin types
INSERT INTO skin_type (label, types, description)
VALUES
    ('NORMAL',      'Normal skin',      'Balanced skin with minimal dryness or oiliness.'),
    ('DRY',         'Dry skin',         'Often feels tight; may appear flaky or rough.'),
    ('OILY',        'Oily skin',        'Produces excess sebum and can look shiny, especially in the T-zone.'),
    ('COMBINATION', 'Combination skin', 'Mix of oily and dry/normal areas, often oily in the T-zone.'),
    ('SENSITIVE',   'Sensitive skin',   'Easily irritated; may react with redness, stinging, or burning.')
ON CONFLICT (label) DO NOTHING;

-- ingredients
INSERT INTO ingredient (inci_name, description, created_at, updated_at)
VALUES
    ('Glycerin',           'A humectant that helps attract and retain moisture in the skin.', NOW(), NOW()),
    ('Hyaluronic Acid',    'A humectant that can hold water and support skin hydration.', NOW(), NOW()),
    ('Niacinamide',        'A form of vitamin B3 often used for barrier support and uneven tone.', NOW(), NOW()),
    ('Ceramide NP',        'A skin-identical lipid that supports the skin barrier.', NOW(), NOW()),
    ('Panthenol',          'Pro-vitamin B5, commonly used for soothing and hydration support.', NOW(), NOW()),
    ('Allantoin',          'Often used to soothe and support comfort in sensitive skin.', NOW(), NOW()),
    ('Salicylic Acid',     'A BHA exfoliant commonly used for oily skin and clogged pores.', NOW(), NOW()),
    ('Azelaic Acid',       'Often used for uneven tone and blemish-prone skin; can be gentle for many.', NOW(), NOW()),
    ('Centella Asiatica',  'Botanical extract commonly used for soothing and barrier support.', NOW(), NOW()),
    ('Squalane',           'An emollient that supports softness and reduces moisture loss.', NOW(), NOW())
ON CONFLICT (inci_name) DO NOTHING;

-- mapping: skin type -> ingredients
-- DRY -> Glycerin, Hyaluronic Acid, Ceramide, Squalane
INSERT INTO skin_type_ingredient (skin_type_id, ingredient_id)
SELECT st.id, i.id
FROM skin_type st
JOIN ingredient i ON i.inci_name in ('Glycerin', 'Hyaluronic Acid', 'Ceramide', 'Squalane')
WHERE st.label = 'DRY'
ON CONFLICT DO NOTHING;

-- OILY -> Niacinamide, Salicylic Acid, Azelaic Acid
INSERT INTO skin_type_ingredient (skin_type_id, ingredient_id)
SELECT st.id, i.id
FROM skin_type st
JOIN ingredient i ON i.inci_name IN ('Niacinamide', 'Salicylic Acid', 'Azelaic Acid')
WHERE st.label = 'OILY'
ON CONFLICT DO NOTHING;

-- SENSITIVE -> Panthenol, Allantoin, Centella Asiatica
INSERT INTO skin_type_ingredient (skin_type_id, ingredient_id)
SELECT st.id, i.id
FROM skin_type st
JOIN ingredient i ON i.inci_name IN ('Panthenol', 'Allantoin', 'Centella Asiatica')
WHERE st.label = 'SENSITIVE'
ON CONFLICT DO NOTHING;

-- COMBINATION -> Niacinamide, Glycerin, Azelaic Acid
INSERT INTO skin_type_ingredient (skin_type_id, ingredient_id)
SELECT st.id, i.id
FROM skin_type st
JOIN ingredient i ON i.inci_name IN ('Niacinamide', 'Glycerin', 'Azelaic Acid')
WHERE st.label = 'COMBINATION'
ON CONFLICT DO NOTHING;

-- NORMAL -> Glycerin, Niacinamide
INSERT INTO skin_type_ingredient (skin_type_id, ingredient_id)
SELECT st.id, i.id
FROM skin_type st
JOIN ingredient i ON i.inci_name IN ('Glycerin', 'Niacinamide')
WHERE st.label = 'NORMAL'
ON CONFLICT DO NOTHING;
-- Ensure the Adaptive Pricing Flag is always there when this service starts
INSERT INTO flag(flagId, name, portionIn)
    SELECT 1, 'adaptive-pricing', 50
WHERE NOT EXISTS (
    SELECT 1 FROM flag WHERE flagId = 1
);
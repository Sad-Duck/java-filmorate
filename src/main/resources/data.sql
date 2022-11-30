INSERT INTO PUBLIC.MPA (rating)
SELECT rating
FROM
     (SELECT 'G' as rating UNION ALL
      SELECT 'PG' as rating UNION ALL
      SELECT 'PG-13' as rating UNION ALL
      SELECT 'R' as rating UNION ALL
      SELECT 'NC-17' as rating)
WHERE NOT EXISTS (SELECT * from PUBLIC.MPA);

INSERT INTO GENRES (NAME)
SELECT genre
FROM
    (SELECT 'Комедия' as genre UNION ALL
    SELECT 'Драма' as genre UNION ALL
    SELECT 'Мультфильм' as genre UNION ALL
    SELECT 'Триллер' as genre UNION ALL
    SELECT 'Документальный' as genre UNION ALL
    SELECT 'Боевик' as genre)

WHERE NOT EXISTS (SELECT * from GENRES);


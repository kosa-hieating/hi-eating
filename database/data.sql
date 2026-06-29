-- Greenfood base seed data for Oracle.
-- Product and product_option rows are excluded because the crawler project will insert them.
-- Seed login password for both users: password

MERGE INTO users u
USING (
    SELECT
        1 AS id,
        'user@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '일반 사용자' AS name,
        'FEMALE' AS gender,
        DATE '1995-05-20' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN NOT MATCHED THEN
    INSERT (id, email, password, name, gender, birth, created_at)
    VALUES (s.id, s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO users u
USING (
    SELECT
        2 AS id,
        'admin@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '관리자' AS name,
        'MALE' AS gender,
        DATE '1990-01-10' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN NOT MATCHED THEN
    INSERT (id, email, password, name, gender, birth, created_at)
    VALUES (s.id, s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO user_auth ua
USING (
    SELECT u.id AS user_id, 'ROLE_USER' AS auth
    FROM users u
    WHERE u.email = 'user@greenfood.test'
) s
ON (ua.user_id = s.user_id AND ua.auth = s.auth)
WHEN NOT MATCHED THEN
    INSERT (user_id, auth, created_at)
    VALUES (s.user_id, s.auth, SYSTIMESTAMP);

MERGE INTO user_auth ua
USING (
    SELECT u.id AS user_id, 'ROLE_ADMIN' AS auth
    FROM users u
    WHERE u.email = 'admin@greenfood.test'
) s
ON (ua.user_id = s.user_id AND ua.auth = s.auth)
WHEN NOT MATCHED THEN
    INSERT (user_id, auth, created_at)
    VALUES (s.user_id, s.auth, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 1 AS id, '유기농 채소' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 2 AS id, '제철 과일' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 3 AS id, '쌀/잡곡' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 4 AS id, '정육/계란' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 5 AS id, '수산/해산' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 6 AS id, '건강 간편식' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 7 AS id, '샐러드/밀키트' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 8 AS id, '음료/차' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 9 AS id, '간식/디저트' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT 10 AS id, '양념/소스' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (id, name, created_at)
    VALUES (s.id, s.name, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        1 AS id,
        '그린푸드 오픈 기획전' AS title,
        '/images/promotions/greenfood-open.png' AS img_src,
        '/products' AS link,
        1 AS display_order
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (id, title, img_src, link, display_order, created_at)
    VALUES (s.id, s.title, s.img_src, s.link, s.display_order, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        2 AS id,
        '제철 신선식품 특가' AS title,
        '/images/promotions/seasonal-fresh.png' AS img_src,
        '/products?sort=latest' AS link,
        2 AS display_order
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (id, title, img_src, link, display_order, created_at)
    VALUES (s.id, s.title, s.img_src, s.link, s.display_order, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        3 AS id,
        '건강한 한 끼 밀키트' AS title,
        '/images/promotions/healthy-mealkit.png' AS img_src,
        '/products?category=meal-kit' AS link,
        3 AS display_order
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (id, title, img_src, link, display_order, created_at)
    VALUES (s.id, s.title, s.img_src, s.link, s.display_order, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        4 AS id,
        '친환경 장보기 추천' AS title,
        '/images/promotions/eco-market.png' AS img_src,
        '/products?category=organic' AS link,
        4 AS display_order
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (id, title, img_src, link, display_order, created_at)
    VALUES (s.id, s.title, s.img_src, s.link, s.display_order, SYSTIMESTAMP);

COMMIT;

MERGE INTO users u
USING (
    SELECT
        'user@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '일반 사용자' AS name,
        'FEMALE' AS gender,
        DATE '1995-05-20' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO users u
USING (
    SELECT
        'admin@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '관리자' AS name,
        'MALE' AS gender,
        DATE '1990-01-10' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

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
USING (SELECT '유기농 채소' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '제철 과일' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '쌀/잡곡' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '정육/계란' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '수산/해산' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '건강 간편식' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '샐러드/밀키트' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '음료/차' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '간식/디저트' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO categories c
USING (SELECT '양념/소스' AS name FROM dual) s
ON (c.name = s.name)
WHEN NOT MATCHED THEN
    INSERT (name, created_at)
    VALUES (s.name, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        '그린푸드 오픈 기획전' AS title,
        '/images/promotions/greenfood-open.png' AS img_src,
        '/products' AS link,
        1000 AS display_order,
        SYSTIMESTAMP AS starts_at,
        SYSTIMESTAMP + INTERVAL '30' DAY AS ends_at
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (title, img_src, link, display_order, starts_at, ends_at, created_at)
    VALUES (s.title, s.img_src, s.link, s.display_order, s.starts_at, s.ends_at, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        '제철 신선식품 특가' AS title,
        '/images/promotions/seasonal-fresh.png' AS img_src,
        '/products?sort=latest' AS link,
        2000 AS display_order,
        SYSTIMESTAMP AS starts_at,
        SYSTIMESTAMP + INTERVAL '30' DAY AS ends_at
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (title, img_src, link, display_order, starts_at, ends_at, created_at)
    VALUES (s.title, s.img_src, s.link, s.display_order, s.starts_at, s.ends_at, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        '건강한 한 끼 밀키트' AS title,
        '/images/promotions/healthy-mealkit.png' AS img_src,
        '/products?category=meal-kit' AS link,
        3000 AS display_order,
        SYSTIMESTAMP AS starts_at,
        SYSTIMESTAMP + INTERVAL '30' DAY AS ends_at
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (title, img_src, link, display_order, starts_at, ends_at, created_at)
    VALUES (s.title, s.img_src, s.link, s.display_order, s.starts_at, s.ends_at, SYSTIMESTAMP);

MERGE INTO promotions p
USING (
    SELECT
        '친환경 장보기 추천' AS title,
        '/images/promotions/eco-market.png' AS img_src,
        '/products?category=organic' AS link,
        4000 AS display_order,
        SYSTIMESTAMP AS starts_at,
        SYSTIMESTAMP + INTERVAL '30' DAY AS ends_at
    FROM dual
) s
ON (p.display_order = s.display_order)
WHEN NOT MATCHED THEN
    INSERT (title, img_src, link, display_order, starts_at, ends_at, created_at)
    VALUES (s.title, s.img_src, s.link, s.display_order, s.starts_at, s.ends_at, SYSTIMESTAMP);

-- --------------------------------------------------------
-- PRODUCTS, OPTIONS, PICTURES DATA SEEDING
-- --------------------------------------------------------

-- Products
MERGE INTO products p USING (
    SELECT 1 AS id, (SELECT id FROM categories WHERE name = '샐러드/밀키트') AS category_id, '춘천식 닭갈비 900g' AS name, '매콤하고 달콤한 춘천식 닭갈비 900g 밀키트' AS description, 25000 AS price, 100 AS view_count, 'ON_SALE' AS status FROM dual UNION ALL
    SELECT 2 AS id, (SELECT id FROM categories WHERE name = '샐러드/밀키트') AS category_id, '[유기농] 신선한 한끼 샐러드' AS name, '신선한 유기농 야채로 가득 채운 한끼 샐러드' AS description, 8900 AS price, 150 AS view_count, 'ON_SALE' AS status FROM dual UNION ALL
    SELECT 3 AS id, (SELECT id FROM categories WHERE name = '정육/계란') AS category_id, '[정육] 국내산 1등급 한우 등심 200g' AS name, '부드러운 식감과 고소한 육즙이 일품인 국내산 한우 등심' AS description, 35000 AS price, 200 AS view_count, 'ON_SALE' AS status FROM dual UNION ALL
    SELECT 4 AS id, (SELECT id FROM categories WHERE name = '건강 간편식') AS category_id, '[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g' AS name, '고기 함량이 높은 진하고 맛있는 일본식 카레' AS description, 4500 AS price, 50 AS view_count, 'ON_SALE' AS status FROM dual UNION ALL
    SELECT 5 AS id, (SELECT id FROM categories WHERE name = '샐러드/밀키트') AS category_id, 'NEW 버섯 듬뿍 잡채 353g' AS name, '버섯이 아낌없이 들어가 쫄깃하고 담백한 전통 잡채' AS description, 12900 AS price, 80 AS view_count, 'ON_SALE' AS status FROM dual
) s ON (p.id = s.id)
WHEN NOT MATCHED THEN
    INSERT (id, category_id, name, description, price, view_count, status, created_at)
    VALUES (s.id, s.category_id, s.name, s.description, s.price, s.view_count, s.status, SYSTIMESTAMP);

-- Options
MERGE INTO product_options po USING (
    SELECT 1 AS id, 1 AS product_id, 100 AS stock, DATE '2026-12-31' AS expire_date FROM dual UNION ALL
    SELECT 2 AS id, 2 AS product_id, 150 AS stock, DATE '2026-12-31' AS expire_date FROM dual UNION ALL
    SELECT 3 AS id, 3 AS product_id, 80 AS stock, DATE '2026-12-31' AS expire_date FROM dual UNION ALL
    SELECT 4 AS id, 4 AS product_id, 200 AS stock, DATE '2026-12-31' AS expire_date FROM dual UNION ALL
    SELECT 5 AS id, 5 AS product_id, 120 AS stock, DATE '2026-12-31' AS expire_date FROM dual
) s ON (po.id = s.id)
WHEN NOT MATCHED THEN
    INSERT (id, product_id, stock, expire_date, created_at)
    VALUES (s.id, s.product_id, s.stock, s.expire_date, SYSTIMESTAMP);

-- Pictures
MERGE INTO product_pictures pp USING (
    SELECT 1 AS id, 1 AS product_id, '/images/logo-hi-eating.png' AS picture_saved_location, 1 AS display_order FROM dual UNION ALL
    SELECT 2 AS id, 2 AS product_id, '/images/logo-hi-eating.png' AS picture_saved_location, 1 AS display_order FROM dual UNION ALL
    SELECT 3 AS id, 3 AS product_id, '/images/logo-hi-eating.png' AS picture_saved_location, 1 AS display_order FROM dual UNION ALL
    SELECT 4 AS id, 4 AS product_id, '/images/logo-hi-eating.png' AS picture_saved_location, 1 AS display_order FROM dual UNION ALL
    SELECT 5 AS id, 5 AS product_id, '/images/logo-hi-eating.png' AS picture_saved_location, 1 AS display_order FROM dual
) s ON (pp.id = s.id)
WHEN NOT MATCHED THEN
    INSERT (id, product_id, picture_saved_location, display_order, created_at)
    VALUES (s.id, s.product_id, s.picture_saved_location, s.display_order, SYSTIMESTAMP);

COMMIT;

-- PHASE4_MEATBALL_EMAIL_VALIDATION_DEMO_START
-- 원격 운영 데이터의 P-55/O-55가 존재할 때만 테스트 사용자의 활동 데이터를 구성한다.
-- 활동 강도를 다르게 만들어 대상 선정(80점 기준)부터 이메일 생성/검증까지 확인한다.

MERGE INTO users u
USING (
    SELECT 'ai-meatball-high@greenfood.test' email, '완자 고관심 사용자' name, 'FEMALE' gender, DATE '1992-03-15' birth FROM dual UNION ALL
    SELECT 'ai-meatball-selected@greenfood.test', '완자 선정 사용자', 'MALE', DATE '1988-09-21' FROM dual UNION ALL
    SELECT 'ai-meatball-medium@greenfood.test', '완자 중관심 사용자', 'OTHER', DATE '1997-01-08' FROM dual UNION ALL
    SELECT 'ai-meatball-low@greenfood.test', '완자 저관심 사용자', 'FEMALE', DATE '2001-11-30' FROM dual
) s
ON (u.email = s.email)
WHEN MATCHED THEN UPDATE SET
    u.name = s.name, u.gender = s.gender, u.birth = s.birth,
    u.deleted_at = NULL, u.updated_at = SYSTIMESTAMP
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
            s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO favorites f
USING (
    SELECT u.id user_id, p.id product_id
    FROM users u
    CROSS JOIN products p
    WHERE u.email IN (
        'ai-meatball-high@greenfood.test',
        'ai-meatball-selected@greenfood.test',
        'ai-meatball-medium@greenfood.test',
        'ai-meatball-low@greenfood.test'
    )
      AND p.id = 55
      AND p.name = '아이 촉촉 고기 한입 완자 150g'
) s
ON (f.user_id = s.user_id AND f.product_id = s.product_id)
WHEN NOT MATCHED THEN
    INSERT (user_id, product_id, created_at)
    VALUES (s.user_id, s.product_id, SYSTIMESTAMP);

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 1, p.price, SYSTIMESTAMP - s.days_ago
FROM (
    SELECT 'ai-meatball-high@greenfood.test' email, 1 purchase_no, 5 days_ago FROM dual UNION ALL
    SELECT 'ai-meatball-high@greenfood.test', 2, 13 FROM dual UNION ALL
    SELECT 'ai-meatball-high@greenfood.test', 3, 21 FROM dual UNION ALL
    SELECT 'ai-meatball-high@greenfood.test', 4, 29 FROM dual UNION ALL
    SELECT 'ai-meatball-selected@greenfood.test', 1, 7 FROM dual UNION ALL
    SELECT 'ai-meatball-selected@greenfood.test', 2, 20 FROM dual UNION ALL
    SELECT 'ai-meatball-medium@greenfood.test', 1, 30 FROM dual
) s
JOIN users u ON u.email = s.email
JOIN products p ON p.id = 55 AND p.name = '아이 촉촉 고기 한입 완자 150g'
WHERE s.purchase_no > (
    SELECT COUNT(*) FROM purchases existing
    WHERE existing.user_id = u.id AND existing.product_id = p.id
);

MERGE INTO purchase_product_options ppo
USING (
    SELECT pu.id purchase_id, po.id product_option_id, pu.quantity
    FROM purchases pu
    JOIN users u ON u.id = pu.user_id AND u.email LIKE 'ai-meatball-%@greenfood.test'
    JOIN product_options po ON po.id = 55 AND po.product_id = pu.product_id
    WHERE pu.product_id = 55
) s
ON (ppo.purchase_id = s.purchase_id AND ppo.product_option_id = s.product_option_id)
WHEN NOT MATCHED THEN
    INSERT (purchase_id, product_option_id, quantity, created_at)
    VALUES (s.purchase_id, s.product_option_id, s.quantity, SYSTIMESTAMP);

INSERT INTO reviews (user_id, product_id, purchase_id, rating, content, created_at)
SELECT u.id, 55, MIN(pu.id),
       CASE WHEN u.email = 'ai-meatball-high@greenfood.test' THEN 5 ELSE 4 END,
       CASE WHEN u.email = 'ai-meatball-high@greenfood.test'
            THEN '촉촉하고 간편해서 재구매했습니다.'
            ELSE '아이 반찬으로 만족스러웠습니다.' END,
       SYSTIMESTAMP - INTERVAL '2' DAY
FROM users u
JOIN purchases pu ON pu.user_id = u.id AND pu.product_id = 55
WHERE u.email IN ('ai-meatball-high@greenfood.test', 'ai-meatball-selected@greenfood.test')
  AND NOT EXISTS (
      SELECT 1 FROM reviews r
      WHERE r.user_id = u.id AND r.product_id = 55 AND r.deleted_at IS NULL
  )
GROUP BY u.id, u.email;

COMMIT;
-- PHASE4_MEATBALL_EMAIL_VALIDATION_DEMO_END


-- PHASE2_CURRY_TARGET_SELECTION_DEMO_START

-- 실제 상품 P-4 카레와 P-5 잡채 기준 대상 선정 데이터.

MERGE INTO users u
USING (
    SELECT
        'ai-curry-high@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '카레 고관심 사용자' AS name,
        'FEMALE' AS gender,
        DATE '1992-03-15' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN MATCHED THEN
    UPDATE SET u.deleted_at = NULL, u.updated_at = SYSTIMESTAMP
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO users u
USING (
    SELECT
        'ai-curry-selected@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '카레 선정 사용자' AS name,
        'MALE' AS gender,
        DATE '1988-09-21' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN MATCHED THEN
    UPDATE SET u.deleted_at = NULL, u.updated_at = SYSTIMESTAMP
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO users u
USING (
    SELECT
        'ai-curry-medium@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '카레 중관심 사용자' AS name,
        'OTHER' AS gender,
        DATE '1997-01-08' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN MATCHED THEN
    UPDATE SET u.deleted_at = NULL, u.updated_at = SYSTIMESTAMP
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

MERGE INTO users u
USING (
    SELECT
        'ai-curry-low@greenfood.test' AS email,
        '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy' AS password,
        '카레 저관심 사용자' AS name,
        'FEMALE' AS gender,
        DATE '2001-11-30' AS birth
    FROM dual
) s
ON (u.email = s.email)
WHEN MATCHED THEN
    UPDATE SET u.deleted_at = NULL, u.updated_at = SYSTIMESTAMP
WHEN NOT MATCHED THEN
    INSERT (email, password, name, gender, birth, created_at)
    VALUES (s.email, s.password, s.name, s.gender, s.birth, SYSTIMESTAMP);

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 1, p.price, SYSTIMESTAMP - INTERVAL '5' DAY
FROM users u
JOIN products p ON p.name IN ('[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g', 'NEW 버섯 듬뿍 잡채 353g')
WHERE u.email = 'ai-curry-high@greenfood.test';

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 1, p.price, SYSTIMESTAMP - INTERVAL '15' DAY
FROM users u
JOIN products p ON p.name IN ('[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g', 'NEW 버섯 듬뿍 잡채 353g')
WHERE u.email = 'ai-curry-high@greenfood.test';

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 2, p.price, SYSTIMESTAMP - INTERVAL '25' DAY
FROM users u
JOIN products p ON p.name IN ('[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g', 'NEW 버섯 듬뿍 잡채 353g')
WHERE u.email = 'ai-curry-high@greenfood.test';

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 1, p.price, SYSTIMESTAMP - INTERVAL '35' DAY
FROM users u
JOIN products p ON p.name IN ('[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g', 'NEW 버섯 듬뿍 잡채 353g')
WHERE u.email = 'ai-curry-high@greenfood.test';


INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 1, p.price, SYSTIMESTAMP - INTERVAL '7' DAY
FROM users u
JOIN products p ON p.name IN ('[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g', 'NEW 버섯 듬뿍 잡채 353g')
WHERE u.email = 'ai-curry-selected@greenfood.test';

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT u.id, p.id, 1, p.price, SYSTIMESTAMP - INTERVAL '20' DAY
FROM users u
JOIN products p ON p.name IN ('[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g', 'NEW 버섯 듬뿍 잡채 353g')
WHERE u.email = 'ai-curry-selected@greenfood.test';

INSERT INTO purchases (user_id, product_id, quantity, purchase_price, created_at)
SELECT
    u.id,
    p.id,
    1,
    p.price,
    SYSTIMESTAMP - INTERVAL '45' DAY
FROM users u
JOIN products p ON p.name IN (
    '[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g',
    'NEW 버섯 듬뿍 잡채 353g'
)
WHERE u.email = 'ai-curry-medium@greenfood.test'
  AND NOT EXISTS (
      SELECT 1 FROM purchases existing
      WHERE existing.user_id = u.id AND existing.product_id = p.id
  );

MERGE INTO favorites f
USING (
    SELECT u.id AS user_id, p.id AS product_id
    FROM users u
    CROSS JOIN products p
    WHERE u.email IN (
        'ai-curry-high@greenfood.test',
        'ai-curry-selected@greenfood.test',
        'ai-curry-medium@greenfood.test',
        'ai-curry-low@greenfood.test'
    )
      AND p.name IN (
          '[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g',
          'NEW 버섯 듬뿍 잡채 353g'
      )
) s
ON (f.user_id = s.user_id AND f.product_id = s.product_id)
WHEN NOT MATCHED THEN
    INSERT (user_id, product_id, created_at)
    VALUES (s.user_id, s.product_id, SYSTIMESTAMP);

INSERT INTO reviews (user_id, product_id, purchase_id, rating, content, created_at)
SELECT
    u.id,
    p.id,
    MIN(pu.id),
    5,
    '카레 고관심 사용자 리뷰',
    SYSTIMESTAMP - INTERVAL '3' DAY
FROM users u
JOIN products p ON p.name IN (
    '[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g',
    'NEW 버섯 듬뿍 잡채 353g'
)
JOIN purchases pu ON pu.user_id = u.id AND pu.product_id = p.id
WHERE u.email = 'ai-curry-high@greenfood.test'
  AND NOT EXISTS (
      SELECT 1 FROM reviews existing
      WHERE existing.user_id = u.id AND existing.product_id = p.id
  )
GROUP BY u.id, p.id;

INSERT INTO reviews (user_id, product_id, purchase_id, rating, content, created_at)
SELECT
    u.id,
    p.id,
    MIN(pu.id),
    4,
    '카레 선정 사용자 리뷰',
    SYSTIMESTAMP - INTERVAL '6' DAY
FROM users u
JOIN products p ON p.name IN (
    '[소프트프로틴] 고기듬뿍 일본식카레 (1인분) 150g',
    'NEW 버섯 듬뿍 잡채 353g'
)
JOIN purchases pu ON pu.user_id = u.id AND pu.product_id = p.id
WHERE u.email = 'ai-curry-selected@greenfood.test'
  AND NOT EXISTS (
      SELECT 1 FROM reviews existing
      WHERE existing.user_id = u.id AND existing.product_id = p.id
  )
GROUP BY u.id, p.id;

-- PHASE2_CURRY_TARGET_SELECTION_DEMO_END

COMMIT;

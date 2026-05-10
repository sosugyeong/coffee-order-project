-- 초기 멤버(사용자) 데이터 삽입
INSERT INTO members (user_identifier, name, phone_number, amount, created_at, updated_at)
VALUES 
('user1', '유저1', '010-1111-2222', 100000, NOW(), NOW()),
('user2', '유저2', '010-3333-4444', 50000, NOW(), NOW()),
('user3', '유저3', '010-5555-6666', 0, NOW(), NOW()),
('user4', '유저4', '010-6666-7777', 50000, NOW(), NOW()),
('user5', '유저5', '010-7777-8888', 50000, NOW(), NOW()),
('user6', '유저6', '010-8888-9999', 50000, NOW(), NOW()),
('user7', '유저7', '010-9999-0000', 50000, NOW(), NOW());

-- 초기 메뉴 데이터 30개 삽입
INSERT INTO menus (title, price, category, status, temp, created_at, updated_at) VALUES 
('아이스 아메리카노', 4500, 'coffee', 'SALE', 'ICED', NOW(), NOW()),
('핫 아메리카노', 4000, 'coffee', 'SALE', 'HOT', NOW(), NOW()),
('카페라떼', 5000, 'coffee', 'SALE', 'ICED', NOW(), NOW()),
('카페라떼', 4500, 'coffee', 'SALE', 'HOT', NOW(), NOW()),
('바닐라 라떼', 5500, 'coffee', 'SALE', 'ICED', NOW(), NOW()),
('헤이즐넛 라떼', 5500, 'coffee', 'SALE', 'HOT', NOW(), NOW()),
('카라멜 마끼아또', 5800, 'coffee', 'SALE', 'ICED', NOW(), NOW()),
('디카페인 아메리카노', 5000, 'decaf', 'SALE', 'ICED', NOW(), NOW()),
('디카페인 카페라떼', 5500, 'decaf', 'SALE', 'HOT', NOW(), NOW()),
('콜드브루', 5000, 'coffee', 'SALE', 'ICED', NOW(), NOW()),
('초코 라떼', 5000, 'non_coffee', 'SALE', 'ICED', NOW(), NOW()),
('녹차 라떼', 5000, 'non_coffee', 'SALE', 'HOT', NOW(), NOW()),
('고구마 라떼', 5500, 'non_coffee', 'SALE', 'HOT', NOW(), NOW()),
('밀크티', 5500, 'non_coffee', 'SALE', 'ICED', NOW(), NOW()),
('레몬 에이드', 5500, 'ade', 'SALE', 'ICED', NOW(), NOW()),
('자몽 에이드', 5500, 'ade', 'SALE', 'ICED', NOW(), NOW()),
('청포도 에이드', 5800, 'ade', 'SALE', 'ICED', NOW(), NOW()),
('플레인 요거트 스무디', 6000, 'smoothie', 'SALE', 'ICED', NOW(), NOW()),
('딸기 요거트 스무디', 6500, 'smoothie', 'SALE', 'ICED', NOW(), NOW()),
('망고 스무디', 6500, 'smoothie', 'SALE', 'ICED', NOW(), NOW()),
('자바칩 프라페', 6800, 'smoothie', 'SALE', 'ICED', NOW(), NOW()),
('쿠키앤크림 프라페', 6800, 'smoothie', 'SALE', 'ICED', NOW(), NOW()),
('캐모마일 티', 4500, 'tea', 'SALE', 'HOT', NOW(), NOW()),
('페퍼민트 티', 4500, 'tea', 'SALE', 'HOT', NOW(), NOW()),
('얼그레이 티', 4500, 'tea', 'SALE', 'ICED', NOW(), NOW()),
('유자차', 5000, 'tea', 'SALE', 'HOT', NOW(), NOW()),
('치즈 케이크', 6500, 'food', 'SALE', 'ICED', NOW(), NOW()),
('티라미수', 6500, 'food', 'SALE', 'ICED', NOW(), NOW()),
('초코 머핀', 3500, 'food', 'SALE', 'HOT', NOW(), NOW()),
('버터떡', 1500, 'food', 'SALE', 'HOT', NOW(), NOW()),
('크로크무슈', 4500, 'food', 'SALE', 'HOT', NOW(), NOW());

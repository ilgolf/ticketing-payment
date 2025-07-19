# seller
INSERT INTO ticketing.seller (created_at, modified_at, representative_name, company_name, license_number, email, phone_number, is_active)
VALUES (now(), now(), '노경태', 'golf', '1234-5678', 'ilgolc@naver.com', '010-1234-5678', true);

# seat
INSERT INTO ticketing.seat (floor, is_available, number, row_index, section) VALUES (0, true, 1, 1, 'STANDING');
INSERT INTO ticketing.seat (floor, is_available, number, row_index, section) VALUES (0, true, 2, 2, 'STANDING');
INSERT INTO ticketing.seat (floor, is_available, number, row_index, section) VALUES (0, true, 3, 3, 'STANDING');
INSERT INTO ticketing.seat (floor, is_available, number, row_index, section) VALUES (0, true, 4, 4, 'STANDING');

# ticket
INSERT INTO ticketing.ticket (price, seller_id, open_date_time, seat_id, status) VALUES (100000.00, 1, '2025-01-29 11:59:56.000000', 1, 'AVAILABLE');
INSERT INTO ticketing.ticket (price, seller_id, open_date_time, seat_id, status) VALUES (100000.00, 1, '2025-01-29 11:59:58.000000', 2, 'AVAILABLE');
INSERT INTO ticketing.ticket (price, seller_id, open_date_time, seat_id, status) VALUES (100000.00, 1, '2025-01-29 11:59:59.000000', 3, 'AVAILABLE');
INSERT INTO ticketing.ticket (price, seller_id, open_date_time, seat_id, status) VALUES (100000.00, 1, '2025-01-29 12:00:01.000000', 4, 'AVAILABLE');


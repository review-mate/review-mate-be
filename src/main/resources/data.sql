--  Partner  #####################################################################################################################################################################################################################
--  Partner  #####################################################################################################################################################################################################################

INSERT INTO partner_company (created_at, updated_at, is_withdrawn, name, domain)
VALUES('2023-10-17 12:00:00', '2023-10-17 12:00:00', 0, '여기어때', 'goodchoice.kr');

INSERT INTO partner_console_config (target_reviewing_rate, achievement_time_series_unit, partner_company_domain)
VALUES (100, 'QUARTERLY', 'goodchoice.kr');

INSERT INTO partner_manager (created_at, updated_at, email, name, password, role, partner_company_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'sckwon770@gmail.com', '권순찬', '1234', 'ADMIN', 1);

INSERT INTO partner_seller (created_at, updated_at, kakao_id, name, phone_number, partner_company_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'sckwon770kakao', '권순찬', '01012341234', 1);



--  Client side  #####################################################################################################################################################################################################################
--  Client side  #####################################################################################################################################################################################################################

INSERT INTO customer (created_at, updated_at, kakao_id, name, partner_custom_id, phone_number, partner_company_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'sckwon770kakao', '권순찬', 'CUSTOMER_0001', '010-1234-1234', 1);

INSERT INTO travel_product (dtype, created_at, updated_at, name, partner_custom_id, review_count, rating, thumbnail_url, category, partner_company_id, partner_seller_id)
VALUES ('SingleTravelProduct', '2023-10-17 12:00:00', '2023-10-17 12:00:00', '신라더스테이 호텔 - 스위트룸', 'CUSTOMER_9', 1, 5, 'testurl.com', 'ACCOMMODATION', 1, 1);


--  Review  #####################################################################################################################################################################################################################
--  Review  #####################################################################################################################################################################################################################

INSERT INTO reservation (created_at, updated_at, partner_custom_id, start_date_time, end_date_time, customer_id, travel_product_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'RESERVATION_0001', '2023-10-18T13:00:00', '2023-10-19T12:00:00', 1, 1);

INSERT INTO review (created_at, updated_at, polarity, content, rating, title, positive_tags_count, negative_tags_count, reservation_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'POSITIVE', '리뷰 내용 1', 3, '리뷰 제목 1', 0, 1, 1);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', '먼지', 'NEGATIVE', 'ROOM', 0, 1, 1);

INSERT INTO review_image (created_at, updated_at, file_name, review_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'testurl.com', 1);



INSERT INTO reservation (created_at, updated_at, partner_custom_id, start_date_time, end_date_time, customer_id, travel_product_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'RESERVATION_0002', '2023-10-18T13:00:00', '2023-10-19T12:00:00', 1, 1);

INSERT INTO review (created_at, updated_at, polarity, content, rating, title, positive_tags_count, negative_tags_count, reservation_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'POSITIVE', '리뷰 내용 1', 5, '리뷰 제목 1', 0, 3, 2);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', '냄새', 'NEGATIVE', 'ROOM', 0, 2, 2);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', '냄새', 'NEGATIVE', 'ROOM', 0, 2, 2);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', '냄새', 'NEGATIVE', 'ROOM', 0, 2, 2);



INSERT INTO reservation (created_at, updated_at, partner_custom_id, start_date_time, end_date_time, customer_id, travel_product_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', 'RESERVATION_0003', '2023-10-18T13:00:00', '2023-10-19T12:00:00', 1, 1);

INSERT INTO review (created_at, updated_at, polarity, content, rating, title, positive_tags_count, negative_tags_count, reservation_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', 'POSITIVE', '리뷰 내용 1', 4, '리뷰 제목 1', 3, 2, 3);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', '먼지', 'POSITIVE', 'KINDNESS', 0, 3, 3);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', '먼지', 'POSITIVE', 'KINDNESS', 0, 3, 3);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', '먼지', 'POSITIVE', 'KINDNESS', 0, 3, 3);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', '먼지', 'NEGATIVE', 'KINDNESS', 0, 3, 3);

INSERT INTO review_tag (created_at, updated_at, keyword, polarity, property, start_index, end_index, review_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', '먼지', 'NEGATIVE', 'KINDNESS', 0, 3, 3);



INSERT INTO reservation (created_at, updated_at, partner_custom_id, start_date_time, end_date_time, customer_id, travel_product_id)
VALUES ('2023-10-01 12:00:00', '2023-10-01 12:00:00', 'RESERVATION_0004', '2023-10-18T13:00:00', '2023-10-19T12:00:00', 1, 1);




--  Live Chatbot  #####################################################################################################################################################################################################
--  Live Chatbot  #####################################################################################################################################################################################################

INSERT INTO live_feedback (created_at, updated_at, customer_media_url, customer_message, is_handled, is_reported, is_solved, seller_message, reservation_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', null, '피드백 메시지 1', 0, 0, 0, '응답 메시지 1', 1);

INSERT INTO live_satisfaction (created_at, updated_at, dissatisfied_review_property, rating, satisfied_review_property, reservation_id)
VALUES ('2023-10-17 12:00:00', '2023-10-17 12:00:00', 'KINDNESS', 5, 'LOCATION', 1);
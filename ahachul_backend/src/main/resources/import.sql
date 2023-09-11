INSERT INTO tb_member(nickname, provider_user_id, provider, region_type, email, gender, age_range) values('세미', '22012412', 'KAKAO', 'METROPOLITAN', 'ss@naver.com', 'MALE', '20');

INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(1, '1호선', 'METROPOLITAN', '02-000-0000', 1001);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(2, '2호선', 'METROPOLITAN', '02-000-0000', 1002);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(3, '3호선', 'METROPOLITAN', '02-000-0000', 1003);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(4, '4호선', 'METROPOLITAN', '02-000-0000', 1004);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(5, '5호선', 'METROPOLITAN', '02-000-0000', 1005);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(6, '6호선', 'METROPOLITAN', '02-000-0000', 1006);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(7, '7호선', 'METROPOLITAN', '02-000-0000', 1007);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(8, '8호선', 'METROPOLITAN', '02-000-0000', 1008);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(9, '9호선', 'METROPOLITAN', '02-000-0000', 1009);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(10, '신분당선', 'METROPOLITAN', '02-000-0000', 1077);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(11, '수인분당선', 'METROPOLITAN', '02-000-0000', 1075);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(12, '경의중앙선', 'METROPOLITAN', '02-000-0000', 1063);
INSERT INTO tb_subway_line(subway_line_id, name, region_type, phone_number, identity) values(13, '우이신설선', 'METROPOLITAN', '02-000-0000', 1092);


-- # 1호선
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('0', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('K', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('S', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('1', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('311', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('312', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('319', 1);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('341', 1);

-- # 2 ~ 9호선
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('2', 2);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('3', 3);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('4', 4);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('5', 5);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('6', 6);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('7', 7);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('8', 8);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('9', 9);

-- # 신분당선
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D01', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D02', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D03', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D04', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D05', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D06', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D07', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D08', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D09', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D10', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D11', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D12', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D13', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D14', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D15', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D16', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D17', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D18', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D19', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D20', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D21', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D22', 10);
INSERT INTO tb_train(prefix_train_no, subway_line_id) values('D23', 10);

-- # 수인분당선
INSERT IGNORE INTO tb_train(prefix_train_no, subway_line_id) values('351', 11);

-- # 경의중앙선
INSERT IGNORE INTO tb_train(prefix_train_no, subway_line_id) values('321', 12);
INSERT IGNORE INTO tb_train(prefix_train_no, subway_line_id) values('331', 12);

-- # 우이신설선
INSERT IGNORE INTO tb_train(prefix_train_no, subway_line_id) values('UL', 13);

INSERT INTO tb_category(name) values('가방');
INSERT INTO tb_category(name) values('귀금속');
INSERT INTO tb_category(name) values('도서용품');
INSERT INTO tb_category(name) values('서류');
INSERT INTO tb_category(name) values('산업용품');
INSERT INTO tb_category(name) values('쇼핑백');
INSERT INTO tb_category(name) values('스포츠용품');
INSERT INTO tb_category(name) values('악기');
INSERT INTO tb_category(name) values('의류');
INSERT INTO tb_category(name) values('자동차');
INSERT INTO tb_category(name) values('전자기기');
INSERT INTO tb_category(name) values('지갑');
INSERT INTO tb_category(name) values('증명서');
INSERT INTO tb_category(name) values('컴퓨터');
INSERT INTO tb_category(name) values('카드');
INSERT INTO tb_category(name) values('현금');
INSERT INTO tb_category(name) values('휴대폰');
INSERT INTO tb_category(name) values('기타물품');

INSERT INTO tb_station(name, identity, subway_line_id) VALUES('소요산', 1001000100, 1);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('동두천', 1001000101, 1);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('시청', 1002000201, 2);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('을지로입구', 1002000202, 2);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('지축', 1003000319, 3);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('구파발', 1003000320, 3);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('정왕', 1004000455, 4);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('오이도', 1004000456, 4);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('방화', 1005000510, 5);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('김포공항', 1005000512, 5);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('봉화산', 1006000647, 6);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('신내', 1006000648, 6);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('장암', 1007000709, 7);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('도봉산', 1007000710, 7);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('송파', 1008000816, 8);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('가락시장', 1008000817, 8);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('둔촌오륜', 1009000937, 9);
INSERT INTO tb_station(name, identity, subway_line_id) VALUES('중앙보훈병원', 1009000938, 9);

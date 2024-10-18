
-- BUILDING --
INSERT INTO building(building_id, location, building_name, phone_contact) VALUES ('BD001','01 Khu CNC, Thu Duc','FPT WorkSpace','0924797913');
INSERT INTO building(building_id, location, building_name, phone_contact) VALUES ('BD002','100 Le Van Viet','FPT NVH','09204524');

-- ROOMTYPE --
INSERT INTO roomtype(id, room_type_name) VALUES ('RT001', 'Phòng đơn');
INSERT INTO roomtype(id, room_type_name) VALUES ('RT002', 'Phòng đôi');
INSERT INTO roomtype(id, room_type_name) VALUES ('RT003', 'Phòng họp');
INSERT INTO roomtype(id, room_type_name) VALUES ('RT004', 'Phòng sự kiện');


-- ROOM --
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('S001', '2024-10-09', 50, 'Phòng đơn 1', 'AVAILABLE', 'BD001', 'RT001', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('S002', '2024-10-09', 55, 'Phòng đơn 2', 'AVAILABLE', 'BD001', 'RT001', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('D001', '2024-10-09', 100, 'Phòng đôi 1', 'AVAILABLE', 'BD001', 'RT002', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('D002', '2024-10-09', 105, 'Phòng đôi 2', 'AVAILABLE', 'BD001', 'RT002', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('M001', '2024-10-09', 150, 'Phòng họp 1', 'AVAILABLE', 'BD001', 'RT003', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('M002', '2024-10-09', 150, 'Phòng họp 2', 'AVAILABLE', 'BD002', 'RT003', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('E001', '2024-10-09', 200, 'Phòng sự kiện 1', 'AVAILABLE', 'BD002', 'RT004', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');
INSERT INTO room(room_id, creation_time, price, room_name, status, building_id, room_typeid, room_img) VALUES ('E002', '2024-10-09', 200, 'Phòng sự kiện 2', 'AVAILABLE', 'BD002', 'RT004', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/thiet-ke-noi-that-phong-hop.jpg');



-- ITEMS --
INSERT INTO serviceitems(price, quantity, service_name, service_type, status, service_img) VALUES (10, 10, 'Ổ cắm', 'Thiết bị', 'AVAILABLE', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/o-cam-lioa-5-lo.webp');
INSERT INTO serviceitems(price, quantity, service_name, service_type, status, service_img) VALUES (50, 10, 'Máy chiếu', 'Thiết bị', 'AVAILABLE', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/xiaomi-wanbo-x1-projektor-cover_1280x672-800-resize.jpg');
INSERT INTO serviceitems(price, quantity, service_name, service_type, status, service_img) VALUES (30, 10, 'Bàn', 'Thiết bị', 'AVAILABLE', 'https://swp-workspace-images.s3.us-east-2.amazonaws.com/q8_b8d8b4205f684de989e1309f67ca4c2b_master.webp');

-- TIMESLOT --
INSERT INTO timeslot(time_slot_id, status, time_start, time_end) VALUES (1, 'AVAILABLE', '7:00:00.000000', '10:00:00.000000');
INSERT INTO timeslot(time_slot_id, status, time_start, time_end) VALUES (2, 'AVAILABLE', '11:00:00.000000', '14:00:00.000000');
INSERT INTO timeslot(time_slot_id, status, time_start, time_end) VALUES (3, 'AVAILABLE', '15:00:00.000000', '18:00:00.000000');
INSERT INTO timeslot(time_slot_id, status, time_start, time_end) VALUES (4, 'AVAILABLE', '19:00:00.000000', '22:00:00.000000');

-- USER --
INSERT INTO user (user_id, password, role_name, username) VALUES ('OWN005', '$2a$10$923vpnosP9l1xDmLPXXph.3h5H4mUWmgSKSbXiEalfplWwpuyCraC', 'OWNER', 'owner1s' );
INSERT INTO user (user_id, password, role_name, username) VALUES ('MA005', '$2a$10$9pBfv0jKNAdzV9ipro0jweN22CUDDFnMYCtwlNxxXHoqITqCuMUkm', 'MANAGER', 'ma01' );
INSERT INTO user (user_id, password, role_name, username) VALUES ('ST01', '$2a$10$9pBfv0jKNAdzV9ipro0jweN22CUDDFnMYCtwlNxxXHoqITqCuMUkm', 'STAFF', 'st01' );
INSERT INTO user (user_id, password, role_name, username) VALUES ('ST02', '$2a$10$9pBfv0jKNAdzV9ipro0jweN22CUDDFnMYCtwlNxxXHoqITqCuMUkm', 'STAFF', 'st02' );

-- STAFF --
INSERT INTO staff (create_at, date_of_birth, phone_number, work_shift, email, full_name, work_days, building_wsid, user_id, status)
VALUES ('2024-10-13', '1990-05-20', '0123456789', 'Morning', 'example@example.com', 'John Doe', 'Mon,Tue,Wed', 'BD001', 'ST01', 'Active');
INSERT INTO staff (create_at, date_of_birth, phone_number, work_shift, email, full_name, work_days, building_wsid, user_id, status)
VALUES ('2024-10-13', '1990-05-20', '0123456789', 'Morning', 'example@example.com', 'Marry', 'Mon,Tue,Wed', 'BD001', 'ST02', 'Active');

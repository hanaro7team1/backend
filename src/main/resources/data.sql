SET FOREIGN_KEY_CHECKS = 0;
truncate table Member;
truncate table Stay;
truncate table StayAvailDate;
truncate table Reservation;
truncate table Festival;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `Festival`
VALUES ('2025-10-15', 10000, '2025-10-01', '2025-08-24 04:43:16', 1, '2025-08-24 04:43:16', '안동시', '어딘가로 1', '축제1',
        'andong.com/festival=1', 'festival description'),
       ('2025-10-16', 10000, '2025-10-02', '2025-08-24 04:43:16', 2, '2025-08-24 04:43:16', '안동시', '어딘가로 2', '축제2',
        'andong.com/festival=2', 'festival description'),
       ('2025-10-17', 10000, '2025-10-03', '2025-08-24 04:43:16', 3, '2025-08-24 04:43:16', '안동시', '어딘가로 3', '축제3',
        'andong.com/festival=3', 'festival description'),
       ('2025-10-18', 10000, '2025-10-04', '2025-08-24 04:43:16', 4, '2025-08-24 04:43:16', '안동시', '어딘가로 4', '축제4',
        'andong.com/festival=4', 'festival description'),
       ('2025-10-19', 10000, '2025-10-05', '2025-08-24 04:43:16', 5, '2025-08-24 04:43:16', '안동시', '어딘가로 5', '축제5',
        'andong.com/festival=5', 'festival description'),
       ('2025-10-20', 10000, '2025-10-06', '2025-08-24 04:43:16', 6, '2025-08-24 04:43:16', '안동시', '어딘가로 6', '축제6',
        'andong.com/festival=6', 'festival description'),
       ('2025-10-21', 10000, '2025-10-07', '2025-08-24 04:43:16', 7, '2025-08-24 04:43:16', '안동시', '어딘가로 7', '축제7',
        'andong.com/festival=7', 'festival description'),
       ('2025-10-22', 10000, '2025-10-08', '2025-08-24 04:43:16', 8, '2025-08-24 04:43:16', '안동시', '어딘가로 8', '축제8',
        'andong.com/festival=8', 'festival description'),
       ('2025-10-23', 10000, '2025-10-09', '2025-08-24 04:43:16', 9, '2025-08-24 04:43:16', '안동시', '어딘가로 9', '축제9',
        'andong.com/festival=9', 'festival description'),
       ('2025-10-24', 10000, '2025-10-10', '2025-08-24 04:43:16', 10, '2025-08-24 04:43:16', '안동시', '어딘가로 10', '축제10',
        'andong.com/festival=10', 'festival description'),
       ('2025-10-25', 10000, '2025-10-11', '2025-08-24 04:43:16', 11, '2025-08-24 04:43:16', '안동시', '어딘가로 11', '축제11',
        'andong.com/festival=11', 'festival description'),
       ('2025-10-26', 10000, '2025-10-12', '2025-08-24 04:43:16', 12, '2025-08-24 04:43:16', '안동시', '어딘가로 12', '축제12',
        'andong.com/festival=12', 'festival description'),
       ('2025-10-27', 10000, '2025-10-13', '2025-08-24 04:43:16', 13, '2025-08-24 04:43:16', '안동시', '어딘가로 13', '축제13',
        'andong.com/festival=13', 'festival description'),
       ('2025-10-28', 10000, '2025-10-14', '2025-08-24 04:43:16', 14, '2025-08-24 04:43:16', '안동시', '어딘가로 14', '축제14',
        'andong.com/festival=14', 'festival description'),
       ('2025-10-29', 10000, '2025-10-15', '2025-08-24 04:43:16', 15, '2025-08-24 04:43:16', '안동시', '어딘가로 15', '축제15',
        'andong.com/festival=15', 'festival description'),
       ('2025-10-30', 10000, '2025-10-16', '2025-08-24 04:43:16', 16, '2025-08-24 04:43:16', '안동시', '어딘가로 16', '축제16',
        'andong.com/festival=16', 'festival description'),
       ('2025-10-31', 10000, '2025-10-17', '2025-08-24 04:43:16', 17, '2025-08-24 04:43:16', '안동시', '어딘가로 17', '축제17',
        'andong.com/festival=17', 'festival description'),
       ('2025-11-01', 10000, '2025-10-18', '2025-08-24 04:43:16', 18, '2025-08-24 04:43:16', '안동시', '어딘가로 18', '축제18',
        'andong.com/festival=18', 'festival description'),
       ('2025-11-02', 10000, '2025-10-19', '2025-08-24 04:43:16', 19, '2025-08-24 04:43:16', '안동시', '어딘가로 19', '축제19',
        'andong.com/festival=19', 'festival description'),
       ('2025-11-03', 10000, '2025-10-20', '2025-08-24 04:43:16', 20, '2025-08-24 04:43:16', '안동시', '어딘가로 20', '축제20',
        'andong.com/festival=20', 'festival description');

INSERT INTO `Member` VALUES (1,'ADMIN','055-000-0000',NULL,'가람마을','garam','경남 창원시','$2a$10$8QZZbiHKpqRxSomOCBzk5u.5z.pwDY2KsU6WksgkNijf9SJ1eIa.6','ROLE_ADMIN'),(2,'ADMIN','055-111-0001',NULL,'village1','host1','region1','$2a$10$2YeV0Pto3v0rRbPzWFZhmeSGCBlDaoNWi1CO4.bp8X2xEZ1g.Ie16','ROLE_ADMIN'),(3,'ADMIN','055-111-0002',NULL,'village2','host2','region2','$2a$10$7E15DDWiMFllvKxGMMxSWuSOR7iK.crxXRy2de8xqwfQcK9uPDHwu','ROLE_ADMIN'),(4,'ADMIN','055-111-0003',NULL,'village3','host3','region3','$2a$10$iszLSp7r1glaWZNWopAHheqa9yANRDVQH1ugwh8VLQOq9wSuaxJmG','ROLE_ADMIN'),(5,'USER','010-2222-0001','user1',NULL,'user1',NULL,'$2a$10$x0gzXfsuEifEcNryKKyziegEyGv8gjKU2mp3vGRgEZRd8XEGDImdG','ROLE_USER'),(6,'USER','010-2222-0002','user2',NULL,'user2',NULL,'$2a$10$eQjsYpHxhqQdFAxCaN9Au.FN3LcReNarPw3H12FNUum8ekw/F1dq.','ROLE_USER'),(7,'USER','010-2222-0003','user3',NULL,'user3',NULL,'$2a$10$WSD7P6LuiRHBYDn2c586m.klElE3OWqaQYQq0oOpuLKPVPXM9Kh1O','ROLE_USER'),(8,'USER','010-2222-0004','user4',NULL,'user4',NULL,'$2a$10$zC60dFn4fP114cnq/EAxA.iFI7C5f6aMfhrvZ9A.F2KRJuEv/lYg6','ROLE_USER'),(9,'USER','010-2222-0005','user5',NULL,'user5',NULL,'$2a$10$2.yJyXlv.TFhWXtzrkHMUek8MBvOs4qTjUzxfONkPMM6dxblOwpe.','ROLE_USER'),(10,'USER','010-2222-0006','user6',NULL,'user6',NULL,'$2a$10$WThORgw/V0qVadA7BdX.EOWrRghxDrnnCmi1vYnacFd063xRbK6/u','ROLE_USER'),(11,'USER','010-2222-0007','user7',NULL,'user7',NULL,'$2a$10$T5S6ySXy.3J/WNTOmR6JT.YfalVW059/KjBUD.m/bpEzJuwtC6B1a','ROLE_USER'),(12,'USER','010-2222-0008','user8',NULL,'user8',NULL,'$2a$10$4fn5bENTtUprJfYb9Kl.POykH0XfJsHOTVvPZQghJe4FuDY43wODO','ROLE_USER'),(13,'USER','010-2222-0009','user9',NULL,'user9',NULL,'$2a$10$hhaiWRVpjkPPLcXvd3TqXe9VfN6PyLk9x88untPA28ZIbAlFL90vG','ROLE_USER'),(14,'USER','010-2222-0010','user10',NULL,'user10',NULL,'$2a$10$CK6/X5sc73q1eNtu1GeBtuiUkgk2BevLwvyqGkFB0RSjQRjN8god6','ROLE_USER');

INSERT INTO `Stay` VALUES (40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,1,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 1호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,2,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 2호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(50,10,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,3,'2025-08-26 06:49:01',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 3호','고양이들이 많음\n작은 텃밭 있음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,4,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 4호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,5,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 5호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,6,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 6호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,7,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 7호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,8,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 8호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,9,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 9호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음'),(40,5,_binary '',_binary '\0','2025-08-26 06:47:53',NULL,10,'2025-08-26 06:47:53',NULL,NULL,'전남 해남 화산면 새꽃마을','사랑방 10호','전기가 아닌 진짜 온돌집\n집근처에 맹꽁이가 아름답게 울음');

INSERT INTO `StayAvailDate` VALUES ('2025-08-26',_binary '',1,1),('2025-08-27',_binary '',2,1),('2025-08-28',_binary '',3,1),('2025-08-29',_binary '',4,1),('2025-08-30',_binary '',5,1),('2025-08-26',_binary '',6,2),('2025-08-27',_binary '',7,2),('2025-08-28',_binary '',8,2),('2025-08-29',_binary '',9,2),('2025-08-30',_binary '',10,2),('2025-08-26',_binary '',11,3),('2025-08-27',_binary '',12,3),('2025-08-28',_binary '',13,3),('2025-08-29',_binary '',14,3),('2025-08-30',_binary '',15,3),('2025-08-26',_binary '',16,4),('2025-08-27',_binary '',17,4),('2025-08-28',_binary '',18,4),('2025-08-29',_binary '',19,4),('2025-08-30',_binary '',20,4),('2025-08-26',_binary '',21,5),('2025-08-27',_binary '',22,5),('2025-08-28',_binary '',23,5),('2025-08-29',_binary '',24,5),('2025-08-30',_binary '',25,5),('2025-09-05',_binary '',26,1),('2025-09-06',_binary '',27,1),('2025-09-07',_binary '',28,1),('2025-09-05',_binary '',29,2),('2025-09-06',_binary '',30,2),('2025-09-07',_binary '',31,2),('2025-09-05',_binary '',32,3),('2025-09-06',_binary '',33,3),('2025-09-07',_binary '',34,3),('2025-09-05',_binary '',35,4),('2025-09-06',_binary '',36,4),('2025-09-07',_binary '',37,4),('2025-09-05',_binary '',38,5),('2025-09-06',_binary '',39,5),('2025-09-07',_binary '',40,5);

INSERT INTO `RealEstates` VALUES (100,84.5,6,1000000000,3,'2025-08-28 07:35:51',1,'2025-08-28 07:35:51','서울시 강남구 테헤란로 123','강남역 근처 아파트','아파트','매매'),(345,158,61,1090000000,2,'2025-08-28 07:35:51',2,'2025-08-28 07:35:51','서울시 송파구 올림픽로 59','잠실 근처 오피스텔 1','오피스텔','매매'),(615,266,115,1630000000,2,'2025-08-28 07:35:51',3,'2025-08-28 07:35:51','서울시 송파구 올림픽로 113','잠실 근처 오피스텔 2','오피스텔','매매'),(1145,478,221,1604967296,2,'2025-08-28 07:35:51',4,'2025-08-28 07:35:51','서울시 송파구 올림픽로 219','잠실 근처 오피스텔 3','오피스텔','매매'),(380,172,68,1160000000,1,'2025-08-28 07:35:51',5,'2025-08-28 07:35:51','서울시 송파구 올림픽로 66','잠실 근처 오피스텔 4','오피스텔','전세'),(2945,1198,581,1995032704,2,'2025-08-28 07:35:51',6,'2025-08-28 07:35:51','서울시 송파구 올림픽로 579','잠실 근처 오피스텔 5','오피스텔','매매');

INSERT INTO `RealEstateImage` VALUES (1,1,'original_image_0.jpg','/images/realEstate/test/0.jpg','saved_image_0.jpg'),(2,1,'original_image_1.jpg','/images/realEstate/test/1.jpg','saved_image_1.jpg'),(3,2,'multi_image_59_0.jpg','/images/realEstate/multi/59_0.jpg','multi_saved_59_0.jpg'),(4,2,'multi_image_59_1.jpg','/images/realEstate/multi/59_1.jpg','multi_saved_59_1.jpg'),(5,2,'multi_image_59_2.jpg','/images/realEstate/multi/59_2.jpg','multi_saved_59_2.jpg'),(6,3,'multi_image_113_0.jpg','/images/realEstate/multi/113_0.jpg','multi_saved_113_0.jpg'),(7,3,'multi_image_113_1.jpg','/images/realEstate/multi/113_1.jpg','multi_saved_113_1.jpg'),(8,3,'multi_image_113_2.jpg','/images/realEstate/multi/113_2.jpg','multi_saved_113_2.jpg'),(9,4,'multi_image_219_0.jpg','/images/realEstate/multi/219_0.jpg','multi_saved_219_0.jpg'),(10,4,'multi_image_219_1.jpg','/images/realEstate/multi/219_1.jpg','multi_saved_219_1.jpg'),(11,4,'multi_image_219_2.jpg','/images/realEstate/multi/219_2.jpg','multi_saved_219_2.jpg'),(12,5,'multi_image_66_0.jpg','/images/realEstate/multi/66_0.jpg','multi_saved_66_0.jpg'),(13,5,'multi_image_66_1.jpg','/images/realEstate/multi/66_1.jpg','multi_saved_66_1.jpg'),(14,5,'multi_image_66_2.jpg','/images/realEstate/multi/66_2.jpg','multi_saved_66_2.jpg'),(15,6,'multi_image_579_0.jpg','/images/realEstate/multi/579_0.jpg','multi_saved_579_0.jpg'),(16,6,'multi_image_579_1.jpg','/images/realEstate/multi/579_1.jpg','multi_saved_579_1.jpg'),(17,6,'multi_image_579_2.jpg','/images/realEstate/multi/579_2.jpg','multi_saved_579_2.jpg');

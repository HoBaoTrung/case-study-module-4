USE MobileStore;
INSERT INTO Brand (brand_name, country, website, image_url) VALUES
('Apple', 'USA', 'https://www.apple.com', 'https://cdn.tgdd.vn/Files/2023/08/21/1543753/appleinsidercopy-210823-160539.jpg'),
('Samsung', 'South Korea', 'https://www.samsung.com','https://images.samsung.com/is/image/samsung/assets/vn/about-us/brand/logo/mo/360_197_1.png?$720_N_PNG$'),
('Xiaomi', 'China', 'https://www.mi.com','https://1000logos.net/wp-content/uploads/2018/10/Xiaomi-Logo-2019.png'),
('Asus', 'Taiwan', 'https://www.asus.com/vn/','https://img-new.cgtrader.com/items/79630/asus_logo_3d_model_3ds_fbx_obj_max_4c03af55-1cba-4b7c-8ad5-fd88a1cebeab.jpg');

INSERT INTO Category (category_name, description) VALUES 
('Laptop', 'Laptop hiệu năng mạnh mẽ, thiết kế mỏng nhẹ – lý tưởng cho công việc, học tập và giải trí hàng ngày. Trang bị vi xử lý mới nhất, màn hình sắc nét và thời lượng pin ấn tượng, giúp bạn luôn sẵn sàng trong mọi tình huống.'),
('Điện thoại thông minh', 'Các dòng smartphone từ nhiều thương hiệu khác nhau'),
('Máy tính bảng', 'Tablet phục vụ nhu cầu học tập và giải trí'),
('Phụ kiện', 'Tai nghe, sạc, ốp lưng, cáp kết nối...'),
('Đồng hồ thông minh', 'Smartwatch hỗ trợ theo dõi sức khỏe và kết nối di động');


INSERT INTO Product (product_name, description, price, stock_quantity, category_id, image_url, brand_id) VALUES
-- Smartphone
('iPhone 14 Pro Max 128GB', 'iPhone 14 Pro Max với chip A16 Bionic, màn hình Super Retina XDR', 32990000, 1, 1, 'https://cdn2.cellphones.com.vn/insecure/rs:fill:0:358/q:90/plain/https://cellphones.com.vn/media/catalog/product/i/p/iphone-14-pro-max-256gb.png', 1),
('Samsung Galaxy S23 Ultra', 'Flagship của Samsung với camera 200MP và S-Pen', 29990000, 40, 1, 'https://azmobile.net/files/product/2025/01/18/678b63651943a.jpg', 2),
('Xiaomi 13T Pro', 'Điện thoại cao cấp với chip Snapdragon 8 Gen 2', 17990000, 60, 1, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRAsOw4HYa6BDM7c7_OEA43LUWrCI4B7JgtXQ&s', 3),

-- Máy tính bảng
('iPad Pro 11 inch (M2)', 'Tablet cao cấp chạy chip M2, hỗ trợ Apple Pencil 2', 25990000, 30, 2, 'https://product.hstatic.net/200000275073/product/1715087752_1826704_432a7ffe208c464a9e8a2df13618ed61_large.jpg', 1),
('Samsung Galaxy Tab S9', 'Máy tính bảng Android hiệu năng cao, màn hình AMOLED', 18990000, 25, 2, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8YgcUOifSWRbluMGqlFPzP_AZ7HIg8HOqQw&s', 2),

-- Phụ kiện
('AirPods Pro 2', 'Tai nghe chống ồn chủ động, chip H2 mới nhất', 5990000, 100, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRs2XoDERwsBuV_mFYrjIaHR2jxQb5pO1RgrQ&s', 1),
('Sạc nhanh 45W Samsung', 'Sạc nhanh cho điện thoại và máy tính bảng Samsung', 850000, 200, 3, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsin4HvWkU2kh3pJ70tS1rbB44YPzRpqvl6w&s', 2),

-- Đồng hồ thông minh
('Apple Watch Series 9', 'Smartwatch cao cấp với tính năng đo điện tâm đồ, SPO2', 11990000, 35, 4, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRzqK7AR-y1DArmdxqsuELok6tM39tfpdjKKQ&s', 1),
('Xiaomi Watch S1 Active', 'Đồng hồ thông minh giá tốt, nhiều tính năng thể thao', 3490000, 80, 4, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRQeLYIGihzbT3AXYnOidRHuRELa_t78rOUbA&s', 3);

INSERT INTO `user`(username,`password`) values
('admin', '$2a$10$EeQCL7M4Q/b.1DBBEubdwOLhI/r5kg2S9liDvcQ746/iE6iPUhQEC'),
('user1', '$2a$10$EeQCL7M4Q/b.1DBBEubdwOLhI/r5kg2S9liDvcQ746/iE6iPUhQEC'),
('user2', '$2a$10$EeQCL7M4Q/b.1DBBEubdwOLhI/r5kg2S9liDvcQ746/iE6iPUhQEC'); 

INSERT INTO `role`(`name`) values ('ROLE_ADMIN'),('ROLE_CUSTOMER');

INSERT INTO `users_roles` values (1,1),(2,2),(3,2);
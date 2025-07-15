use MobileStore;
DELIMITER //

CREATE PROCEDURE sp_get_all_products()
 BEGIN
	select * from product;
 END //

DELIMITER ;

DELIMITER //
CREATE PROCEDURE sp_check_product_quantity (
    IN p_product_id INT,
    IN p_quantity_needed INT,
    OUT p_enough BOOLEAN
)
BEGIN
    DECLARE current_stock INT;

    SELECT stock_quantity INTO current_stock
    FROM product
    WHERE product_id = p_product_id;

    SET p_enough = current_stock >= p_quantity_needed;
END //
DELIMITER ;

-- insert customer info
delimiter //
CREATE PROCEDURE sp_insert_customer_info (
    IN p_name VARCHAR(255),
    IN p_phone VARCHAR(10),
    IN p_address VARCHAR(255),
    IN p_payment_method VARCHAR(10),
    out _customer_id int
)
BEGIN
    INSERT INTO customer_info (name, phone, address)
    VALUES (p_name, p_phone, p_address);
    set _customer_id = (select id from customer_info order by id desc limit 1);
end //
delimiter ;

-- insert order
delimiter //
create procedure sp_insert_orders(
	in _customer_id int,
	in _total_price double,
    in _payment_method varchar(10),
	out _order_id int
)
	begin
		insert into orders(total_price, customer_id, payment_method) values(_total_price, _customer_id, _payment_method);
		set _order_id = (select id from orders order by id desc limit 1);
	end //
delimiter ;


-- insert order_detail
delimiter //
create procedure sp_insert_order_detail(
	in _order_id int,
	in _product_id int,
	in _quantity int
)
	begin
		insert into order_detail(order_id, product_id, quantity) values(_order_id, _product_id, _quantity);
	end //
delimiter ;


DELIMITER //
CREATE PROCEDURE sp_update_product_quantity(
    IN _product_id INT,
    IN quantity INT
)
BEGIN
    DECLARE current_quantity INT;

    -- Lấy số lượng tồn kho hiện tại
    SELECT stock_quantity
    INTO current_quantity
    FROM product
    WHERE product_id = _product_id
    FOR UPDATE;

    -- Kiểm tra nếu trừ đi mà < 0 thì báo lỗi
    IF (current_quantity - quantity) < 0 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Error: Not enough stock quantity.';
    ELSE
        -- Nếu hợp lệ thì cập nhật
        UPDATE product
        SET stock_quantity = current_quantity - quantity
        WHERE product_id = _product_id;
    END IF;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE sp_get_order_detail(
    IN _order_id INT
)
BEGIN
    SELECT od.*
            FROM order_detail od
            JOIN product p ON od.product_id = p.product_id
            WHERE od.order_id = _order_id;
END //
DELIMITER ;


DELIMITER //
CREATE PROCEDURE sp_search_products(
    IN p_product_name VARCHAR(100),
    IN p_min_price DECIMAL(10,2),
    IN p_max_price DECIMAL(10,2),
    IN p_category_ids VARCHAR(255),
    IN p_brand_ids VARCHAR(255),
    IN offset INT,
    IN limit_num INT
)
BEGIN
    DECLARE v_sql_query TEXT;
    DECLARE v_where_clause TEXT DEFAULT ' WHERE 1=1';

    -- Xây dựng điều kiện WHERE linh hoạt
    IF p_product_name IS NOT NULL AND p_product_name != '' THEN
        SET v_where_clause = CONCAT(v_where_clause, ' AND p.product_name LIKE ''%', p_product_name, '%''');
    END IF;

    IF p_min_price IS NOT NULL THEN
        SET v_where_clause = CONCAT(v_where_clause, ' AND p.price >= ', p_min_price);
    END IF;

    IF p_max_price IS NOT NULL THEN
        SET v_where_clause = CONCAT(v_where_clause, ' AND p.price <= ', p_max_price);
    END IF;

    IF p_brand_ids IS NOT NULL AND p_brand_ids != '' THEN
        SET v_where_clause = CONCAT(v_where_clause, ' AND FIND_IN_SET(p.brand_id, ''', p_brand_ids, ''')');
    END IF;

    IF p_category_ids IS NOT NULL AND p_category_ids != '' THEN
        SET v_where_clause = CONCAT(v_where_clause, ' AND FIND_IN_SET(p.category_id, ''', p_category_ids, ''')');
    END IF;

    -- Query chính: truy vấn dữ liệu
    SET @v_sql_query = CONCAT(
        'SELECT * FROM Product p',
        v_where_clause,
        ' ORDER BY p.product_name',
        ' LIMIT ', offset, ', ', limit_num, ';'
    );

    -- Query đếm tổng số bản ghi
    SET @v_count_query = CONCAT(
        'SELECT COUNT(*) AS total FROM Product p',
        v_where_clause, ';'
    );

    -- Thực thi truy vấn chính
    PREPARE stmt1 FROM @v_sql_query;
    EXECUTE stmt1;
    DEALLOCATE PREPARE stmt1;

    -- Trả về tổng số kết quả
    PREPARE stmt2 FROM @v_count_query;
    EXECUTE stmt2;
    DEALLOCATE PREPARE stmt2;
END //
DELIMITER ;

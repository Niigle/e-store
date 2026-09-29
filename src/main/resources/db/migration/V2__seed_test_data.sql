INSERT INTO users (`id`, `firstname`, `lastname`, `username`, `email`, `address`, `phone`, `password`, `created_on`, `modified_on`) VALUES
                (1, 'Nikola', 'Djurovic', 'ndj', 'nikolapc@outlook.com', 'Branka Krsmanovica 10', '0648292087', '$2y$10$gtSnOa0rLoeX7u.ttBAuLe73RTlzwWWOQRIjQRqXdsjoAvLKI39iG', '2026-06-23 21:45:58', '2026-06-23 21:46:01'),
                (12, 'Carl', 'Johnson', 'cj', 'cj@gmail.com', 'grove st', '0100555333', '$2y$10$a1Fxz.GlhKtxUUFHFrDLcOMuRLeiv4Bb.wPNUYA241Nomz6f60GfG', '2026-06-23 21:45:58', '2026-06-23 21:46:01'),
                (13, 'Niko', 'Belic', 'nb', 'nb@gmail.com', 'hove beach', '55533322', '$2y$10$3HOlGKdYU7jvnk2rE7GVcOqETOfFLlJzXTUnTUpp5.g7jlX1eh4yu', '2026-06-23 21:45:58', '2026-06-23 21:46:01'),
                (14, 'Nikola', 'Nic', 'gtap', 'grandtheftautopet@gmail.com', 'Branka Krsmanovica 6', '0648292085', '$2y$10$cV35eo7W5KmUJz5PtNoKnutgq75PeawRPHd0mW0ioJRGllU41Le0q', '2026-06-23 21:45:58', '2026-06-23 21:46:01');


INSERT INTO roles (`id`, `role_name`) VALUES (3, 'User');
INSERT INTO roles (`id`, `role_name`) VALUES (2, 'Manager');
INSERT INTO roles (`id`, `role_name`) VALUES (1, 'Admin');


INSERT INTO permissions (`id`, `description`) VALUES (1, 'EDIT_ALL_CONTENT');
INSERT INTO permissions (`id`, `description`) VALUES (2, 'EDIT_STORES_CONTENT');
INSERT INTO permissions (`id`, `description`) VALUES (3, 'EDIT_USER_CONTENT');


INSERT INTO role_permission (`id`, `role_id`, `permission_id`) VALUES (1, 1, 1);
INSERT INTO role_permission (`id`, `role_id`, `permission_id`) VALUES (2, 2, 2);
INSERT INTO role_permission (`id`, `role_id`, `permission_id`) VALUES (3, 3, 3);


INSERT INTO user_roles (`id`, `role_id`, `user_id`) VALUES (1, 1, 1);
INSERT INTO user_roles (`id`, `role_id`, `user_id`) VALUES (2, 2, 12);
INSERT INTO user_roles (`id`, `role_id`, `user_id`) VALUES (3, 3, 13);
INSERT INTO user_roles (`id`, `role_id`, `user_id`) VALUES (5, 3, 1);


INSERT INTO category (`id`, `name`, `description`) VALUES
                   (1, 'General store', 'selling general items'),
                   (2, 'Tech store', 'selling technology products'),
                   (3, 'Second hand', 'selling second hand items');


INSERT INTO stores (`id`, `name`, `address`, `category_id`, `phone`, `manager_id`, `is_active`, logo) VALUES (1, 'Lidl', 'bulevar', 1, '06433552214', 2, true, 'images/stores/lidl.png');
INSERT INTO stores (`id`, `name`, `address`, `category_id`, `phone`, `manager_id`, `is_active`, logo) VALUES (2, 'Gigatron1', 'Delta', 2, '06433578214', 2, true, 'images/stores/gigatronl.png');
INSERT INTO stores (`id`, `name`, `address`, `category_id`, `phone`, `manager_id`, `is_active`, logo) VALUES (3, 'Idea1', 'beograd', 2, '06433578214', 2, true, 'images/stores/idea.png');


INSERT INTO products (`id`, name, type, description, barcode, image) VALUES
                     (10, 'Cow yogurt',       'Dairy',  'Full fat yogurt', '1322554',  'images/products/yogurt.png'),
                     (11, 'Cow milk',         'Dairy',  'Full fat milk',   '1222554',  'images/products/yogurt.png'),
                     (12, 'Chocolate yogurt', 'Dairy',  'Full fat yogurt', '1122554',  'images/products/yogurt.png'),
                     (13, 'cheese',           'Dairy',  'Cheese',          '1022554',  'images/products/yogurt.png'),
                     (14, 'fanta',            'Dairy',  'Soft drink',      '2322554',  'images/products/yogurt.png'),
                     (15, 'coca cola',        'Dairy',  'Soft drink',      '1252554',  'images/products/yogurt.png'),
                     (16, 'mouse',            'Tech',   'Computer mouse',  '2122554',  'images/products/mouse.png'),
                     (17, 'keyboard',         'Tech',   'Keyboard',        '3322554',  'images/products/mouse.png'),
                     (18, 'monitor acb273',   'Tech',   'Monitor',         '3422554',  'images/products/mouse.png'),
                     (19, 'rtx 6090',         'Tech',   'Graphics card',   '13522554', 'images/products/mouse.png'),
                     (20, 'chipsy',           'Sweets', 'Chips',           '1382554',  'images/products/choc.png'),
                     (21, 'smoki',            'Sweets', 'Snack',           '135554',   'images/products/choc.png'),
                     (22, 'crisps',           'Sweets', 'Crisps',          '722554',   'images/products/choc.png'),
                     (23, 'choc choc',        'Sweets', 'Chocolate',       '8322554',  'images/products/choc.png'),
                     (24, 'cream',            'Sweets', 'Cream',           '9322554',  'images/products/choc.png');

INSERT INTO store_products (`id`, `store_id`, `product_id`, `price`, `stock`, `name`) VALUES (1, 3, 10, 220.00, 5, 'Cow yogurt');
INSERT INTO store_products (`id`, `store_id`, `product_id`, `price`, `stock`, `name`) VALUES
                          (2, 1, 10, 220.00, 5, 'Cow yogurt'),
                          (3, 1, 11, 220.00, 5, 'Cow milk'),
                          (4, 1, 12, 220.00, 5, 'Chocolate yogurt'),
                          (5, 1, 13, 220.00, 5, 'cheese'),
                          (6, 1, 14, 220.00, 5, 'fanta'),
                          (7, 1, 15, 220.00, 5, 'coca cola'),
                          (8, 3, 11, 220.00, 5, 'Cow milk'),
                          (9, 3, 12, 220.00, 5, 'Chocolate yogurt'),
                          (10, 3, 13, 220.00, 5, 'cheese'),
                          (11, 3, 14, 220.00, 5, 'fanta'),
                          (12, 3, 15, 220.00, 5, 'coca cola'),
                          (13, 2, 16, 220.00, 5, 'mouse'),
                          (14, 2, 19, 220.00, 5, 'rtx 6090'),
                          (15, 2, 17, 220.00, 5, 'keyboard'),
                          (16, 3, 23, 280.00, 15, 'choc choc');


INSERT INTO exchange_rates (`id`, `currency_from`, `currency_to`, `exchange_rate`, `date_of`) VALUES
                          (1, 'eur', 'rsd', 120.00, current_date()),
                          (2, 'usd', 'rsd', 100.00, current_date()),
                          (3, 'chf', 'rsd', 130.00, DATE('2026-07-14'));
INSERT INTO exchange_rates (`id`, `currency_from`, `currency_to`, `exchange_rate`, `date_of`) VALUES
                          (4, 'rsd', 'eur', 0.010, current_date()),
                          (5, 'rsd', 'usd', 0.100, current_date()),
                          (6, 'rsd', 'chf', 0.005, DATE('2026-07-14'));
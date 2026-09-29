CREATE TABLE IF NOT EXISTS users (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `firstname` varchar(55) COLLATE utf16_slovenian_ci NOT NULL,
  `lastname` varchar(55) COLLATE utf16_slovenian_ci NOT NULL,
  `username` VARCHAR(100) NOT NULL UNIQUE,
  `email` varchar(50) COLLATE utf16_slovenian_ci NOT NULL,
  `address` varchar(100) COLLATE utf16_slovenian_ci NOT NULL,
  `phone` varchar(100) COLLATE utf16_slovenian_ci NOT NULL,
  `password` varchar(100) COLLATE utf16_slovenian_ci NOT NULL,
  `created_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `modified_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS roles (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_name` varchar(200) COLLATE utf16_slovenian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS permissions (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `description` varchar(200) COLLATE utf16_slovenian_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS role_permission (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT UNSIGNED NOT NULL,
  `permission_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`),
  CONSTRAINT `fk_role_permission_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `roles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_role_permission_permission`
    FOREIGN KEY (`permission_id`)
    REFERENCES `permissions` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS user_roles (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `role_id` BIGINT UNSIGNED NOT NULL,
  `user_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_user_roles_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `users` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_user_roles_role`
    FOREIGN KEY (`role_id`)
    REFERENCES `roles` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS category (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) COLLATE utf16_slovenian_ci NOT NULL,
    `description` VARCHAR(1000)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS stores (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(55) COLLATE utf16_slovenian_ci NOT NULL,
  `address` varchar(100) COLLATE utf16_slovenian_ci NOT NULL,
  `category_id` BIGINT UNSIGNED NOT NULL,
  `phone` varchar(100) COLLATE utf16_slovenian_ci NOT NULL,
  `manager_id` BIGINT UNSIGNED NOT NULL,
  `is_active` BOOL DEFAULT TRUE,
  `logo` VARCHAR(255) DEFAULT NULL,
  `created_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `modified_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT fk_stores_category
        FOREIGN KEY (category_id)
        REFERENCES category(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS products (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `name` varchar(55) COLLATE utf16_slovenian_ci NOT NULL,
  `type` varchar(50) COLLATE utf16_slovenian_ci NOT NULL,
  `description` VARCHAR(1000),
  `barcode` VARCHAR(50) UNIQUE,
  `image` VARCHAR(255) DEFAULT NULL,
  `created_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  `modified_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS store_products (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `store_id` BIGINT UNSIGNED NOT NULL,
  `product_id` BIGINT UNSIGNED NOT NULL,
  `name` varchar(55) COLLATE utf16_slovenian_ci NOT NULL,
  `price` DECIMAL(10,2) NOT NULL,
  `stock` INT DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `store_id` (`store_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `uq_store_product` UNIQUE (`store_id`, `product_id`),
  CONSTRAINT `fk_store_products_store`
    FOREIGN KEY (`store_id`)
    REFERENCES `stores` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_store_products_product`
    FOREIGN KEY (`product_id`)
    REFERENCES `products` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS orders (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `total` DECIMAL(10,2) NOT NULL,
    `created_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `status` VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS', -- CART
    CONSTRAINT `fk_orders_user`
        FOREIGN KEY (`user_id`)
        REFERENCES `users` (`id`)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS order_items (
    id BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    order_id BIGINT UNSIGNED NOT NULL,
    store_product_id BIGINT UNSIGNED NOT NULL,
    quantity INT NOT NULL,
    price_at_purchase DECIMAL(10,2) NOT NULL,
    CONSTRAINT `fk_order_items_order`
            FOREIGN KEY (`order_id`)
            REFERENCES `orders` (`id`)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT `fk_order_items_store_product`
            FOREIGN KEY (`store_product_id`)
            REFERENCES `store_products` (`id`)
            ON DELETE RESTRICT
            ON UPDATE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS order_history (
    `id` BIGINT UNSIGNED NOT NULL,
    `user_id` BIGINT UNSIGNED NOT NULL,
    `total` DECIMAL(10,2) NOT NULL,
    `created_on` TIMESTAMP NOT NULL,
    `status` VARCHAR(20) NOT NULL,
    `archived_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS order_items_history (
    `id` BIGINT UNSIGNED NOT NULL,
    `order_id` BIGINT UNSIGNED NOT NULL,
    `store_product_id` BIGINT UNSIGNED NOT NULL,
    `quantity` INT NOT NULL,
    `price_at_purchase` DECIMAL(10,2) NOT NULL,
    `archived_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS exchange_rates (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `currency_from` VARCHAR(10) COLLATE utf16_slovenian_ci NOT NULL,
    `currency_to` VARCHAR(10) COLLATE utf16_slovenian_ci NOT NULL,
    `exchange_rate` DECIMAL(10,4) NOT NULL,
    `date_of` DATE NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS price_history_log (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `product_id` BIGINT UNSIGNED NOT NULL,
    `store_id` BIGINT UNSIGNED NOT NULL,
    `old_price` DECIMAL(12,2) NOT NULL,
    `new_price` DECIMAL(12,2) NOT NULL,
    `modified_on` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

CREATE TABLE IF NOT EXISTS daily_turnover_log (
    `id` BIGINT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    `date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `store_id` BIGINT UNSIGNED NOT NULL,
    `total_turnover` DECIMAL(12,2) NOT NULL,
    `unique_purchase_count` INT NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf16 COLLATE=utf16_slovenian_ci;

ALTER TABLE daily_turnover_log ADD UNIQUE KEY uq_date_store (`date`, `store_id`);

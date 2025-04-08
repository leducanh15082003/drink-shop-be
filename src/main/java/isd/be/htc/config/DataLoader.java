package isd.be.htc.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DataLoader(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM category", Integer.class);
        if (count != null && count > 0) {
            return;
        }

        // Chèn dữ liệu vào bảng category
        jdbcTemplate.execute("INSERT INTO category (name, description) VALUES ('Coffee', 'Các loại cà phê truyền thống và pha chế')");
        jdbcTemplate.execute("INSERT INTO category (name, description) VALUES ('Tea', 'Các loại trà và đồ uống từ trà')");
        jdbcTemplate.execute("INSERT INTO category (name, description) VALUES ('Pastry', 'Bánh ngọt và đồ ăn kèm')");

        // --- Sản phẩm cho danh mục Coffee (category_id = 1) ---
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Espresso', 'Cà phê Espresso đậm đà', 'Cà phê rang, nước', 2.50, '/images/menu/FriedEggs.png', 1)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Cappuccino', 'Cà phê Cappuccino mịn màng', 'Espresso, sữa nóng, bọt sữa', 3.00, '/images/menu/FriedEggs.png', 1)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Latte', 'Cà phê Latte thơm ngon', 'Espresso, sữa tươi, bọt sữa', 3.50, '/images/menu/FriedEggs.png', 1)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Mocha', 'Cà phê Mocha kết hợp sô cô la', 'Espresso, sữa, sô cô la', 3.80, '/images/menu/FriedEggs.png', 1)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Americano', 'Cà phê Americano nhẹ nhàng', 'Espresso, nước nóng', 2.80, '/images/menu/FriedEggs.png', 1)");

        // --- Sản phẩm cho danh mục Tea (category_id = 2) ---
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Green Tea', 'Trà xanh thanh mát', 'Trà xanh, nước', 2.00, '/images/menu/FriedEggs.png', 2)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Black Tea', 'Trà đen truyền thống', 'Trà đen, nước sôi', 2.20, '/images/menu/FriedEggs.png', 2)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Oolong Tea', 'Trà Oolong đặc trưng', 'Trà Oolong, nước', 2.50, '/images/menu/FriedEggs.png', 2)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Herbal Tea', 'Trà thảo mộc tự nhiên', 'Các loại thảo mộc, nước', 2.70, '/images/menu/FriedEggs.png', 2)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('White Tea', 'Trà trắng tinh khiết', 'Trà trắng, nước', 2.90, '/images/menu/FriedEggs.png', 2)");

        // --- Sản phẩm cho danh mục Pastry (category_id = 3) ---
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Croissant', 'Bánh sừng bò Pháp giòn rụm', 'Bột mì, bơ, đường', 1.80, '/images/menu/FriedEggs.png', 3)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Muffin', 'Bánh muffin mềm mịn', 'Bột mì, trứng, đường', 2.00, '/images/menu/FriedEggs.png', 3)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Donut', 'Bánh donut ngọt ngào', 'Bột mì, đường, dầu', 1.50, '/images/menu/FriedEggs.png', 3)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Danish', 'Bánh Danish bơ thơm phức', 'Bột mì, bơ, trứng', 2.20, '/images/menu/FriedEggs.png', 3)");
        jdbcTemplate.execute("INSERT INTO product (name, description, ingredients, price, image_url, category_id) " +
                "VALUES ('Eclair', 'Bánh éclair kem socola', 'Bột mì, kem, socola', 2.50, '/images/menu/FriedEggs.png', 3)");

        for (int productId = 1; productId <= 15; productId++) {
            int quantity;
            if (productId <= 5) {
                quantity = 100; // Coffee
            } else if (productId <= 10) {
                quantity = 200; // Tea
            } else {
                quantity = 150; // Pastry
            }
            jdbcTemplate.execute("INSERT INTO inventory (product_id, quantity, updated_at) " +
                    "VALUES (" + productId + ", " + quantity + ", '2025-04-01 10:00:00')");
        }
    }
}

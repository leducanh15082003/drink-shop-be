package isd.be.htc.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

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
		jdbcTemplate.execute(
				"INSERT INTO category (name, description) VALUES ('Coffee', 'Các loại cà phê truyền thống và pha chế')");
		jdbcTemplate.execute(
				"INSERT INTO category (name, description) VALUES ('Tea', 'Các loại trà và đồ uống từ trà')");
		jdbcTemplate.execute(
				"INSERT INTO category (name, description) VALUES ('Pastry', 'Bánh ngọt và đồ ăn kèm')");

		// --- Sản phẩm cho danh mục Coffee (category_id = 1) ---
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Espresso', 'Rich, bold, and the foundation of it all.', 'Cà phê rang, nước', 35000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/espresso.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Cappuccino', 'Equal parts espresso, steamed milk, and milk foam.', 'Espresso, sữa nóng, bọt sữa', 50000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/cappuchino.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Latte', 'Creamy espresso drink with lots of steamed milk.', 'Espresso, sữa tươi, bọt sữa', 50000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/latte.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Mocha', 'A chocolatey twist on the latte, topped with whipped cream.', 'Espresso, sữa, sô cô la', 55000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/mocha.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Americano', 'Espresso with hot water for a smooth, long drink.', 'Espresso, nước nóng', 40000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/cappuchino.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Cold Brew', 'Slow-steeped for a smooth, bold finish.', 'Cà phê ủ lạnh, nước, đá', 55000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/coldbrew.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Salted Caramel Latte', 'A sweet-salty hug in a cup', 'Espresso, sữa, caramel mặn', 60000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/caramellatte.png', 1, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Coconut Coffee', 'Creamy, coconut-infused iced coffee, tropical and bold.', 'Espresso, cốt dừa, đá', 60000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/coconut%20coffee.png', 1, 0)");

		// --- Sản phẩm cho danh mục Tea (category_id = 2) ---
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Peach Iced Tea', ' Sweet, fruity tea with juicy peach notes', 'Trà xanh, đào, nước', 35000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/peach.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Lychee Green Tea', ' Light green tea paired with fragrant lychee', 'Trà đen, nước sôi', 40000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/lychee.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Milk Tea', 'Creamy black tea with rich milk and a touch of sweetness.', 'Trà Oolong, sữa', 50000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/milktea.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Peppermint Tea', ' Cool, refreshing, and great for digestion', 'Các loại thảo mộc, nước', 50000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/peppermint.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Jasmine Green Tea', 'Delicately floral and soothing', 'Trà trắng, nước', 55000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/jasmine.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Thai Green Milk Tea', 'Vibrant, sweet, and aromatic with pandan and condensed milk.', 'Trà trắng, nước', 55000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/thaigreen.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Matcha Latte', 'Smooth, earthy Japanese matcha with steamed milk.', 'Trà trắng, nước', 60000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/matchalatte.png', 2, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Rose Oolong Tea', ' Fragrant oolong with delicate rose petals.', 'Trà trắng, nước', 60000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/rose.png', 2, 0)");

		// --- Sản phẩm cho danh mục Pastry (category_id = 3) ---
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Butter Croissant', 'Classic, buttery, and perfectly flaky.', 'Bột mì, bơ, đường', 35000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/buttercrois.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Cinnamon Roll', 'Soft and swirled with cinnamon, topped with a sugar glaze.', 'Bột mì, trứng, đường', 40000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/cinna.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Matcha Muffin', 'Earthy matcha flavor with a soft and moist texture.', 'Bột mì, đường, dầu', 50000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/muffin.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Mini Quiche', 'Perfectly baked egg custard in a flaky crust.', 'Bột mì, bơ, trứng', 50000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/quiche.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Almond Croissant', 'Sweet almond cream filling, topped with sliced almonds.', 'Bột mì, kem, socola', 55000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/almold.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Chocolate Croissant', 'Filled with rich chocolate and wrapped in golden layers.', 'Bột mì, kem, socola', 55000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/socola.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Banana Bread Slice', 'Moist, naturally sweet, and great with coffee.', 'Bột mì, kem, socola', 60000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/banana.png', 3, 0)");
		jdbcTemplate.execute(
				"INSERT INTO product (name, description, ingredients, price, image_url, category_id, quantity_sold) " +
						"VALUES ('Spinach & Feta Puff', 'Crispy puff pastry with a creamy veggie filling.', 'Bột mì, kem, socola', 60000, 'https://ydxwwsgwzousllputjeh.supabase.co/storage/v1/object/public/drinkshop/temp/puff.png', 3, 0)");

		for (int productId = 1; productId <= 24; productId++) {
			int quantity;
			if (productId <= 8) {
				quantity = 500; // Coffee
			} else if (productId <= 16) {
				quantity = 500; // Tea
			} else {
				quantity = 550; // Pastry
			}
			jdbcTemplate.execute("INSERT INTO inventory (product_id, quantity, updated_at) " +
					"VALUES (" + productId + ", " + quantity + ", '2025-04-01 10:00:00')");
		}

		Random random = new Random();
		for (int month = 1; month <= 4; month++) {
			int baseAmount = 80_000;
			int variation   = random.nextInt(40_001);
			int totalAmount = baseAmount + variation + month * 10_000;

			LocalDateTime orderTime = LocalDateTime.of(2025, month, 15, 10, 0, 0);
			jdbcTemplate.update(
					"INSERT INTO orders (total_amount, order_time, status, address, phone_number) " +
							"VALUES (?, ?, ?, ?, ?)",
					totalAmount,
					Timestamp.valueOf(orderTime),
					"COMPLETED",
					"Test Address",
					"0123456789"
			);
		}

		List<Long> orderIds = jdbcTemplate.queryForList("SELECT id FROM orders", Long.class);

		for (Long orderId : orderIds) {
			Integer amount = jdbcTemplate.queryForObject(
					"SELECT total_amount FROM orders WHERE id = ?",
					new Object[]{orderId},
					Integer.class
			);
			jdbcTemplate.update(
					"INSERT INTO payment (order_id, amount, payment_method, status, transaction_date) VALUES (?, ?, ?, ?, ?)",
					orderId,
					amount,
					"CASH",
					"PAID",
					Timestamp.valueOf("2025-05-01 10:00:00")
			);
		}

		for (Long orderId : orderIds) {
			Integer month = jdbcTemplate.queryForObject(
					"SELECT EXTRACT(MONTH FROM order_time) FROM orders WHERE id = ?",
					new Object[]{orderId},
					Integer.class
			);
			int detailsCount = 1 + random.nextInt(2);
			for (int i = 0; i < detailsCount; i++) {
				int productId = 1 + random.nextInt(24);
				String size    = random.nextBoolean() ? "M" : "L";
				int sugarRate  = random.nextInt(101);
				int iceRate    = random.nextInt(101);
				int quantity   = 1 + random.nextInt(3);

				Integer basePrice = jdbcTemplate.queryForObject(
						"SELECT price FROM product WHERE id = ?",
						new Object[]{productId},
						Integer.class
				);
				if (basePrice == null) continue;

				double multiplier = 1 + (month - 1) * 0.05;
				int unitPrice = (int) Math.round(basePrice * multiplier);

				jdbcTemplate.update(
						"INSERT INTO order_detail (order_id, product_id, size, sugar_rate, ice_rate, quantity, unit_price) " +
								"VALUES (?, ?, ?, ?, ?, ?, ?)",
						orderId, productId, size, sugarRate, iceRate, quantity, unitPrice
				);
			}
		}
	}
}

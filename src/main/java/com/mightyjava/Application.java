package com.mightyjava;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.mightyjava.domain.Book;
import com.mightyjava.domain.Role;
import com.mightyjava.domain.User;
import com.mightyjava.service.IRoleService;
import com.mightyjava.service.IService;
import com.mightyjava.utils.ConstantUtils;

@SpringBootApplication
public class Application implements CommandLineRunner {
	@Autowired
	private IService<User> userService;

	@Autowired
	private IRoleService<Role> roleService;

	@Autowired
	private IService<Book> bookService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		if (roleService.findAll().isEmpty()) {
			roleService.saveOrUpdate(new Role(ConstantUtils.ADMIN.toString()));
			roleService.saveOrUpdate(new Role(ConstantUtils.USER.toString()));
		}

		if (userService.findAll().isEmpty()) {
			User user1 = new User();
			user1.setEmail("test@user.com");
			user1.setName("Test User");
			user1.setMobile("9787456545");
			user1.setRole(roleService.findByName(ConstantUtils.USER.toString()));
			user1.setPassword(new BCryptPasswordEncoder().encode("testuser"));
			userService.saveOrUpdate(user1);

			User user2 = new User();
			user2.setEmail("admin@gmail.com");
			user2.setName("Admin");
			user2.setMobile("0347823057");
			user2.setRole(roleService.findByName(ConstantUtils.ADMIN.toString()));
			user2.setPassword(new BCryptPasswordEncoder().encode("CONFIRMER"));
			userService.saveOrUpdate(user2);
		}

		if (bookService.findAll().isEmpty()) {
			for (int i = 1; i <= 10; i++) {
				Book book = new Book();
				book.setTitle("Angular vol: " + i);
				book.setAuthor("Avotra");
				book.setCoverPhotoURL("");
				book.setIsbnNumber(1617293989L);
				book.setPrice(2776.00 + i);
				book.setLanguage("Malagasy");
				book.setGenre("Technology");
				bookService.saveOrUpdate(book);
			}
		}
	}

}

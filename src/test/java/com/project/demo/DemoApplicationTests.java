package com.project.demo;

import com.project.demo.logic.entity.Otp.OtpRepository;
import com.project.demo.logic.entity.email.EmailService;
import com.project.demo.logic.entity.product.ProductRepository;
import com.project.demo.logic.entity.rateProduct.RateProductRepository;
import com.project.demo.logic.entity.user.User;
import com.project.demo.logic.entity.user.UserRepository;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import com.project.demo.rest.userBuyer.UserBuyerRestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DemoApplicationTests {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserBuyerRepository userBuyerRepository;
	@Autowired
	private UserBrandRepository userBrandRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private OtpRepository otpRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private RateProductRepository rateProductRepository;
    @Autowired
    private UserBuyerRestController userBuyerRestController;
    @Autowired
    private ProductRepository productRepository;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}


	@Test
	void disableAccountUser_UserFound_StatusUpdated() {
		// given
		String email = "user.buyer@gmail.com";
		String status = "Inactivo";

		// when
		Optional<User> optionalUser = userRepository.findByEmail(email);
		optionalUser.ifPresent(user -> {
			user.setStatus(status);
			userRepository.save(user);
			// then
			assertEquals(status, user.getStatus());
		});
	}

	@Test
	void disableAccountUser_UserNotFound_StatusNotUpdated() {
		// given
		String email = "userBuyer@gmail.com";

		// when
		Optional<User> optionalUser = userRepository.findByEmail(email);
		optionalUser.ifPresent(user -> {
			user.setStatus("Inactivo");
			userRepository.save(user);
		});

		// then
		assertTrue(optionalUser.isEmpty());
	}

	@Test
	void resetPassword_EmailFound() {
		// given
		String email = "user.buyer@gmail.com";
		String newPassword = "newPassword123";

		// when
		Optional<User> optionalUser = userRepository.findByEmail(email);
		optionalUser.ifPresent(user -> {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
		});

		// then
		Optional<User> updatedUser = userRepository.findByEmail(email);
		assertTrue(updatedUser.isPresent());
		assertTrue(passwordEncoder.matches(newPassword, updatedUser.get().getPassword()));
	}

	@Test
	void resetPassword_EmailNotFound() {
		// given
		String email = "userBuyer@gmail.com";
		String newPassword = "newPassword123";

		// when
		Optional<User> optionalUser = userRepository.findByEmail(email);
		optionalUser.ifPresent(user -> {
			user.setPassword(passwordEncoder.encode(newPassword));
			userRepository.save(user);
		});

		// then
		assertTrue(optionalUser.isEmpty());
	}

	@Test
	void loginUser_UserAuthenticatedSuccessfully() {
		// given
		String email = "user.buyer@gmail.com";
		String password = "newPassword123";

		// when
		Optional<User> optionalUser = userRepository.findByEmail(email);

		// then
		optionalUser.ifPresent(user -> {
			assertTrue(passwordEncoder.matches(password, user.getPassword()));
		});
		assertTrue(optionalUser.isPresent());
	}

	@Test
	void loginUser_UserNotAuthenticated() {
		// given
		String email = "lacapital@gmail.com";
		String password = "capital123";

		// when
		Optional<User> optionalUser = userRepository.findByEmail(email);

		// then
		optionalUser.ifPresent(user -> {
			assertFalse(passwordEncoder.matches(password, user.getPassword()));
		});
		assertTrue(optionalUser.isEmpty());
	}

	@Test
	void delete_User() {
		// given
		long id = 18;

		// when
		Optional<User> optionalUser = userRepository.findById(id);
		optionalUser.ifPresent(user -> {
			userRepository.delete(user);
		});

		// then
		assertTrue(userRepository.findById(id).isEmpty());
	}
}
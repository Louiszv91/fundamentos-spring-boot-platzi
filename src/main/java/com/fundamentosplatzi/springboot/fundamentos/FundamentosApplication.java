package com.fundamentosplatzi.springboot.fundamentos;

import com.fundamentosplatzi.springboot.fundamentos.bean.MyBean;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithDependency;
import com.fundamentosplatzi.springboot.fundamentos.bean.MyBeanWithProperties;
import com.fundamentosplatzi.springboot.fundamentos.component.ComponentDependency;
import com.fundamentosplatzi.springboot.fundamentos.entity.User;
import com.fundamentosplatzi.springboot.fundamentos.pojo.UserPojo;
import com.fundamentosplatzi.springboot.fundamentos.repository.UserRepository;
import com.fundamentosplatzi.springboot.fundamentos.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class FundamentosApplication implements CommandLineRunner {

	private final Log LOGGER = LogFactory.getLog(FundamentosApplication.class);

	private final ComponentDependency componentDependency;
	private final MyBean myBean;

	private MyBeanWithDependency myBeanWithDependency;
	private MyBeanWithProperties myBeanWithProperties;
	private UserPojo userPojo;

	private UserRepository userRepository;

	private UserService userService;

	@Autowired
	public FundamentosApplication(@Qualifier("componentTwoImplement") ComponentDependency componentDependency, MyBean myBean, MyBeanWithDependency myBeanWithDependency, MyBeanWithProperties myBeanWithProperties, UserPojo userPojo, UserRepository userRepository, UserService userService) {
		this.componentDependency = componentDependency;
		this.myBean = myBean;
		this.myBeanWithDependency = myBeanWithDependency;
		this.myBeanWithProperties = myBeanWithProperties;
		this.userPojo = userPojo;
		this.userRepository = userRepository;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(FundamentosApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {
		//ejemplosAnteriores();
		saveUserInDatabase();
		getInformationJpqlFromUser();
		saveWithErrorTransactional();
	}

	private void saveWithErrorTransactional(){
		User test1 = new User("TestTransactional1", "TestTransactional1@gmail.com", LocalDate.now());
		User test2 = new User("TestTransactional2", "TestTransactional2@gmail.com", LocalDate.now());
		User test3 = new User("TestTransactional3", "TestTransactional3@gmail.com", LocalDate.now());
		User test4 = new User("TestTransactional4", "TestTransactional4@gmail.com", LocalDate.now());

		List<User> users = Arrays.asList(test1, test2, test3, test4);
		try {
			userService.saveTransactional(users);
		}catch (Exception e){
			LOGGER.error("Esta es una excepcion dentro del metodo transaccional" + e.getMessage());
		}
		userService.getAllUsers()
				.stream()
				.forEach(user -> LOGGER.info("Este es el usuario dentro del metodo transaccional: " + user));
	}

	private void getInformationJpqlFromUser(){
		/*
		LOGGER.info("Usuario con el metodo findByUserEmail " +
				userRepository.findByUserEmail("louis@domain.com")
						.orElseThrow(() -> new RuntimeException("No se encontro el usuario")));

		userRepository.findAndSort("s", Sort.by("id").descending())
				.stream()
				.forEach(user -> LOGGER.info("Usuario con metodo sort" + user));

		userRepository.findByName("John")
				.stream()
				.forEach(user -> LOGGER.info("Usuario con query method" + user));

		LOGGER.info("Usuario con Qchrome://vivaldi-webui/startpage?section=Speed-dials&background-color=#181818uery Method: " + userRepository.findByEmailAndName("julie@domain.com", "Julie")
				.orElseThrow(()-> new RuntimeException("Usuario no encontrado")));

		userRepository.findByNameLike("%J%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameLike: " + user));

		userRepository.findByNameOrEmail(null, "diana@domain.com")
				.stream()
				.forEach(user -> LOGGER.info("Usuario findByNameOrEmail: " + user));

		 */
		userRepository
				.findByBirthdateBetween(LocalDate.of(2022, 3, 1), LocalDate.of(2022, 5, 1))
				.stream()
				.forEach(user -> LOGGER.info("Usuario con intervalo de fechas: " + user));

		userRepository
				.findByNameLikeOrderByIdDesc("%John%")
				.stream()
				.forEach(user -> LOGGER.info("Usuario encontrado con like y ordenado: " + user));

		LOGGER.info("El Usuario a partir del named parameter es: " + userRepository
				.getAllByBirthDateAndEmail(LocalDate.of(2022, 5, 21), "julie@domain.com")
				.orElseThrow(()->new RuntimeException("No se encontro el parametro a partir del named parameter")));
	}

	private void saveUserInDatabase(){
		User user1 = new User("John", "john@domain.com", LocalDate.of(2022, 3, 20));
		User user2 = new User("Julie", "julie@domain.com", LocalDate.of(2022, 5, 21));
		User user3 = new User("Louis", "louis@domain.com", LocalDate.of(2022, 6, 22));
		User user4 = new User("Carlos", "carlos@domain.com", LocalDate.of(2022, 7, 23));
		User user5 = new User("Diana", "diana@domain.com", LocalDate.of(2022, 8, 24));
		User user6 = new User("Andre", "andre@domain.com", LocalDate.of(2022, 9, 25));
		User user7 = new User("Tania", "tania@domain.com", LocalDate.of(2022, 10, 26));
		User user8 = new User("Jose", "jose@domain.com", LocalDate.of(2022, 11, 27));
		User user9 = new User("Ronald", "ronald@domain.com", LocalDate.of(2022, 12, 28));
		User user10 = new User("John", "john1@domain.com", LocalDate.of(2022, 3, 20));
		List<User> list = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8, user9, user10);
		list.stream().forEach(userRepository::save);
	}

	protected void ejemplosAnteriores(){
		componentDependency.saludar();
		myBean.print();
		myBeanWithDependency.printWithDependency();
		System.out.println(myBeanWithProperties.function());
		System.out.println(userPojo.getEmail() + " - " + userPojo.getPassword() + " - " + userPojo.getAge());
		try{
			int value = 10 / 0;
			LOGGER.debug("Mi valor: " + value);
		} catch (Exception e) {
			LOGGER.error("Estos es un error al dividir por cero: " + e.getMessage());
		}
	}
}

package tqs.lab6_1;

import org.springframework.boot.SpringApplication;

public class TestLab6_1Application {

	public static void main(String[] args) {
		SpringApplication.from(Lab6_1Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}

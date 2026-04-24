package com.mustafaz.telemed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

//@RequiredArgsConstructor
@EnableAsync // enable async processing, so we can use an extra threads
@SpringBootApplication
public class TelemedApplication {

//    private final NotificationService notificationService;

	static void main(String[] args) {
		SpringApplication.run(TelemedApplication.class, args);
	}

//    @Bean
//    CommandLineRunner runner(){
//		return args -> {
//			NotificationDTO notificationDTO = NotificationDTO.builder()
//					.recipient("darsh.zayed22@gmail.com")
//					.subject("Testing Email")
//					.message("Hey, this is a test mail")
//					.build();
//			notificationService.sendEmail(notificationDTO, new User());
//		};
//	}
}

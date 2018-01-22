package com.sample.notification.snsaws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sample.notification.snsaws.service.NotificationService;

@SpringBootApplication
public class SnsAwsApplication implements CommandLineRunner{

	@Autowired
	private NotificationService notificationService;

	public static void main(String[] args) {
		SpringApplication.run(SnsAwsApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		notificationService.pushNotificationToAndroid();

	}
}

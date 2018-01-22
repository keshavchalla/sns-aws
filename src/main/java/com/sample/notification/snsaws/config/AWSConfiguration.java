package com.sample.notification.snsaws.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;

@Configuration
public class AWSConfiguration {

	@Value("${cloud.aws.credentials.accessKey}")
	private String accessKey;

	@Value("${cloud.aws.credentials.secretKey}")
	private String secretKey;

	@Value("${cloud.aws.region.static}")
	private String region;

	@Bean
	public BasicAWSCredentials basicAWSCredentials() {
		return new BasicAWSCredentials(accessKey, secretKey);
	}
	
	@Bean
	public AmazonSNS snsClient() {
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
				.withRegion(Regions.fromName(region))
				.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials()))
				.build();
		return snsClient;
	}
	
	@Bean
	public NotificationMessagingTemplate snsMessagingTemplate() {
		return new NotificationMessagingTemplate(snsClient());
	}
}

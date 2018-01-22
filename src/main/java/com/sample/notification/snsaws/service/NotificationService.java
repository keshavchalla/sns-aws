package com.sample.notification.snsaws.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.amazonaws.services.sns.model.CreatePlatformApplicationRequest;
import com.amazonaws.services.sns.model.Endpoint;
import com.amazonaws.services.sns.model.ListEndpointsByPlatformApplicationRequest;
import com.amazonaws.services.sns.model.ListEndpointsByPlatformApplicationResult;
import com.amazonaws.services.sns.model.ListPlatformApplicationsRequest;
import com.amazonaws.services.sns.model.ListPlatformApplicationsResult;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PlatformApplication;

@Component
public class NotificationService {

	@Autowired
	private AmazonSNS snsClient;
	
	@Autowired
	private NotificationMessagingTemplate snsMessagingTemplate;
	
	
	
	public void sendMessageToTopic() {
		PublishResult result = snsClient.publish("arn:aws:sns:us-east-2:387888624213:news", "Hello Message","From Java");
		System.out.println(result.toString());
	}
	
	public void sendSmsMessage() {
		Map<String, MessageAttributeValue> smsAttributes = new HashMap<>();
		PublishResult result = snsClient.publish(new PublishRequest()
				.withMessage("AWS SMS")
				.withPhoneNumber("+1 717 919 6127")
				//.addMessageAttributesEntry("DefaultSMSType", "Promotional ")
				);
	
	}
	
	public void pushNotificationToAndroid() {
		ListPlatformApplicationsResult listPlatformApplicationsResult = snsClient.listPlatformApplications();
		List<PlatformApplication> platformsList = listPlatformApplicationsResult.getPlatformApplications();
		String platformApplicationArn = platformsList.stream()
																.findFirst()
																.get().getPlatformApplicationArn();
		ListEndpointsByPlatformApplicationRequest endpointsByAppRequest =  new ListEndpointsByPlatformApplicationRequest();
		endpointsByAppRequest.setPlatformApplicationArn(platformApplicationArn);
		ListEndpointsByPlatformApplicationResult endpointsByAppResult = snsClient.listEndpointsByPlatformApplication(endpointsByAppRequest);
		List<Endpoint> endpointsList = endpointsByAppResult.getEndpoints();
		for(Endpoint endpoint : endpointsList) {
			try {
				String endpointArn = endpoint.getEndpointArn();
				System.out.println("--------- : "+endpointArn);
				PublishRequest publishRequest = new PublishRequest();
				publishRequest.setMessageStructure("json");
				publishRequest.setTargetArn(endpointArn);
				//new PublishRequest(topicARN, JSON.toJSON(message), messageType)
				publishRequest.setMessage("{\r\n" + 
						"\"GCM\": \"{ \\\"notification\\\": { \\\"text\\\": \\\"Hello from java spring aws\\\" } }\"\r\n" + 
						"}");
				snsClient.publish(publishRequest);
			}catch(Exception e) {
				e.printStackTrace();
			}

		}
		
		
		
	}
}

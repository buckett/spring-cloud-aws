/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.elasticspring.mail.config.xml;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import org.elasticspring.config.AmazonWebserviceClientConfigurationUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Agim Emruli
 */
public class SimpleEmailServiceBeanDefinitionParserTest {

	@Test
	public void parse_MailSenderWithMinimalConfiguration_createMailSenderWithJavaMail() throws IllegalAccessException, URISyntaxException {
		//Arrange
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-context.xml", getClass());

		//Act
		AmazonSimpleEmailService emailService = context.getBean(AmazonWebserviceClientConfigurationUtils.
				getBeanName(AmazonSimpleEmailServiceClient.class.getName()), AmazonSimpleEmailService.class);

		MailSender mailSender = context.getBean(MailSender.class);

		//Assert
		Field field = ReflectionUtils.findField(AmazonSimpleEmailServiceClient.class, "endpoint");
		ReflectionUtils.makeAccessible(field);
		Assert.assertEquals( new URI("https","email.us-east-1.amazonaws.com",null,null),field.get(emailService));

		Assert.assertTrue(mailSender instanceof JavaMailSender);
	}

	@Test
	public void parse_MailSenderWithRegionConfiguration_createMailSenderWithJavaMailAndRegion() throws IllegalAccessException, URISyntaxException {
		//Arrange
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-region.xml", getClass());

		//Act
		AmazonSimpleEmailService emailService = context.getBean(AmazonWebserviceClientConfigurationUtils.
				getBeanName(AmazonSimpleEmailServiceClient.class.getName()), AmazonSimpleEmailService.class);

		MailSender mailSender = context.getBean(MailSender.class);

		//Assert
		Field field = ReflectionUtils.findField(AmazonSimpleEmailServiceClient.class, "endpoint");
		ReflectionUtils.makeAccessible(field);
		Assert.assertEquals( new URI("https","email.eu-west-1.amazonaws.com",null,null),field.get(emailService));

		Assert.assertTrue(mailSender instanceof JavaMailSender);
	}

	@Test
	public void parse_MailSenderWithRegionProviderConfiguration_createMailSenderWithJavaMailAndRegionFromRegionProvider() throws IllegalAccessException, URISyntaxException {
		//Arrange
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(getClass().getSimpleName() + "-regionProvider.xml", getClass());

		//Act
		AmazonSimpleEmailService emailService = context.getBean(AmazonWebserviceClientConfigurationUtils.
				getBeanName(AmazonSimpleEmailServiceClient.class.getName()), AmazonSimpleEmailService.class);

		MailSender mailSender = context.getBean(MailSender.class);

		//Assert
		Field field = ReflectionUtils.findField(AmazonSimpleEmailServiceClient.class, "endpoint");
		ReflectionUtils.makeAccessible(field);
		Assert.assertEquals( new URI("https","email.ap-southeast-2.amazonaws.com",null,null),field.get(emailService));

		Assert.assertTrue(mailSender instanceof JavaMailSender);
	}

}

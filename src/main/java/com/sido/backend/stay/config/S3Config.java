package com.sido.backend.stay.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
public class S3Config {

	@Value("${app.aws.region:ap-northeast-2}")
	private String region;

	@Value("${AWS_ACCESS_KEY_ID:}")
	private String accessKeyId;

	@Value("${AWS_SECRET_ACCESS_KEY:}")
	private String secretAccessKey;

	private boolean hasKeys() {
		return accessKeyId != null && !accessKeyId.isBlank()
			&& secretAccessKey != null && !secretAccessKey.isBlank();
	}

	@Bean
	public S3Client s3Client() {
		if (hasKeys()) {
			return S3Client.builder()
				.region(Region.of(region))
				.credentialsProvider(
					StaticCredentialsProvider.create(
						AwsBasicCredentials.create(accessKeyId, secretAccessKey)
					)
				)
				.build();
		} else {
			return S3Client.builder()
				.region(Region.of(region))
				.build();
		}
	}

	@Bean
	public S3Presigner s3Presigner() {
		S3Presigner.Builder builder = S3Presigner.builder()
			.region(Region.of(region));

		if (hasKeys()) {
			builder.credentialsProvider(
				StaticCredentialsProvider.create(
					AwsBasicCredentials.create(accessKeyId, secretAccessKey)
				)
			);
		}
		return builder.build();
	}
}
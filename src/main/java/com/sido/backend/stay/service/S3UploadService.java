package com.sido.backend.stay.service;

import java.net.URL;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3UploadService {
	private final S3Presigner presigner;

	@Value("${app.s3.bucket}")
	private String bucket;

	public URL createPresignedPutUrl(String key, String contentType, Duration ttl) {
		PutObjectRequest putObject = PutObjectRequest.builder()
			.bucket(bucket)
			.key(key)
			.contentType(contentType)
			.build();

		PutObjectPresignRequest presign = PutObjectPresignRequest.builder()
			.signatureDuration(ttl)
			.putObjectRequest(putObject)
			.build();

		return presigner.presignPutObject(presign).url();
	}
}
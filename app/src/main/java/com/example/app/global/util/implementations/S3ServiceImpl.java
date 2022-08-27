package com.example.app.global.util.implementations;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.l4box.server.global.exception.ExceptionType;
import com.l4box.server.global.exception.L4BoxException;
import com.l4box.server.global.util.S3Service;
import java.io.IOException;
import java.io.InputStream;
import javax.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

	@Value("${cloud.aws.s3.bucket}")
	private String bucket;

	@Value("${cloud.aws.s3.bucket.apk}")
	private String apkDirectory;

	private final AmazonS3Client amazonS3Client;

	@Override
	public String uploadFile(MultipartFile multipartFile, boolean isApk) {

		String fileName = multipartFile.getOriginalFilename();

		ObjectMetadata objectMetadata = new ObjectMetadata();
		objectMetadata.setContentLength(multipartFile.getSize());
		objectMetadata.setContentType(multipartFile.getContentType());

		try (InputStream is = multipartFile.getInputStream()) {

			amazonS3Client.putObject(new PutObjectRequest(isApk ? apkDirectory : bucket, fileName, is, objectMetadata)
				.withCannedAcl(CannedAccessControlList.PublicRead));

		} catch (IOException e) {

			throw new L4BoxException(ExceptionType.FILE_UPLOAD_FAILED);

		}

		return fileName;

	}

	@Override
	public String getFileUrl(@NotEmpty String fileName, boolean isApk) {

		return amazonS3Client.getResourceUrl(isApk ? apkDirectory : bucket, fileName);

	}

}

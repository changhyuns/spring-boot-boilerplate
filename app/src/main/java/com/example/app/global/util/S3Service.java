package com.example.app.global.util;

import javax.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public interface S3Service {

	String uploadFile(MultipartFile multipartFile, boolean isApk);

	String getFileUrl(@NotEmpty String fileName, boolean isApk);

}

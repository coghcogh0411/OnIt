package com.minho.ownit;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public class FileNameGenerator {
	public static String generator(MultipartFile mf) {
		String fileName = mf.getOriginalFilename();//파일명
		String type = fileName.substring(fileName.lastIndexOf("."));
		fileName = fileName.replace(type, "");
		String uuid = UUID.randomUUID().toString();
		//UUID
		return fileName = fileName+"_"+uuid+ type;
	}
}

package com.minho.ownit;

import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public class FileNameGenerator {
    public static String generator(MultipartFile mf) {
        String originalFilename = mf.getOriginalFilename();

        // (1) 파일명이 null이거나 빈 문자열일 경우 예외 처리
        if (originalFilename == null || originalFilename.isEmpty()) {
            // 아무 이름도 없으면 UUID만으로 파일명 생성
            return UUID.randomUUID().toString();
        }

        // (2) 확장자 추출을 위한 '.' 위치 찾기
        int dotIndex = originalFilename.lastIndexOf(".");
        // 확장자, 확장자를 제외한 파일명
        String ext = "";
        String fileNameWithoutExt = originalFilename;

        if (dotIndex >= 0 && dotIndex < originalFilename.length() - 1) {
            // 점이 있고, 점이 마지막 글자가 아닐 때 → 확장자 추출
            ext = originalFilename.substring(dotIndex);
            fileNameWithoutExt = originalFilename.substring(0, dotIndex);
        } else if (dotIndex == originalFilename.length() - 1) {
            // 점이 맨 끝에만 있는 경우 (예: "filename.")
            // 확장자를 빈 문자열로 처리
            fileNameWithoutExt = originalFilename.substring(0, dotIndex);
            ext = "";
        } 
        // 점이 아예 없으면(ext = ""), 그대로 fileNameWithoutExt = originalFilename

        // (3) UUID 생성
        String uuid = UUID.randomUUID().toString();

        // (4) 최종 파일명 생성
        return fileNameWithoutExt + "_" + uuid + ext;
    }
}



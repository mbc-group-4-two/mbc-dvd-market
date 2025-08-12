package org.group4.dvdshopbackend.models.item.dto.service;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@Log
public class FileService {

    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData) throws Exception{
        UUID uuid = UUID.randomUUID();                                      // 랜덤 파일명 생성
        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 확장자
        String savedFileName = uuid.toString() + extension;                 //  uuid+원래파일명 결합
        String fileUploadFullUrl = uploadPath + "/" + savedFileName;        // 경로 추가
        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);                                                // 파일 저장
        fos.close();                                                        // 닫고
        return savedFileName;                                               // 파일명 리턴
    }

    public void deleteFile(String filePath) throws Exception{  // 파일 삭제 메서드
        File deleteFile = new File(filePath);
        if(deleteFile.exists()) {
            deleteFile.delete();
            log.info("파일을 삭제하였습니다.");
        } else {
            log.info("파일이 존재하지 않습니다.");
        }
    }

}

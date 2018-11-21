package com.ahao.web.module.upload.model;

/**
 * 上传文件的返回体
 */
public class UploadDTO {
    private String filepath;
    private String filename;

    public static UploadDTO create(String filepath, String filename){
        UploadDTO dto = new UploadDTO();
        dto.setFilepath(filepath);
        dto.setFilename(filename);
        return dto;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    @Override
    public String toString() {
        return "UploadDTO{" +
                "filepath='" + filepath + '\'' +
                ", filename='" + filename + '\'' +
                '}';
    }
}

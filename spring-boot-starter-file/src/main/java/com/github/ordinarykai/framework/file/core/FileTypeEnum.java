package com.github.ordinarykai.framework.file.core;

/**
 * 文件类型
 *
 * @author wukai
 * @date 2022/8/12 9:48
 */
public enum FileTypeEnum {

    /********** image **********/
    JPG("jpg"),
    JPEG("jpeg"),
    PNG("png"),
    GIF("gif"),
    ICO("ico"),
    ICON("icon"),
    WEBP("webp"),
    BMP("bmp"),
    PSD("psd"),
    TIFF("tiff"),
    HEIF("heif"),
    SVG("svg"),

    /********** video **********/
    MP4("mp4"),
    AVI("avi"),
    MOV("mov"),
    WMV("wmv"),
    flv("flv"),
    MPEG("mpeg"),
    M4V("m4v"),
    ASF("asf"),
    F4V("f4v"),
    RMVB("rmvb"),
    RM("rm"),
    _3GP("3gp"),
    VOB("vob"),

    /********** audio **********/
    MP3("mp3"),
    WMA("wma"),
    WAV("wav"),
    APE("ape"),
    FLAC("flac"),
    OGG("ogg"),
    AAC("aac"),

    /********** document **********/
    XLS("xls"),
    XLSX("xlsx"),
    PPT("ppt"),
    PPTX("pptx"),
    DOC("doc"),
    DOCX("docx"),
    PDF("pdf"),
    TXT("txt"),

    /********** rar **********/
    ZIP("zip"),
    RAR("rar"),
    TAR("tar");

    FileTypeEnum(String value) {
        this.value = value;
    }

    /**
     * 文件类型
     */
    private final String value;

    public String getValue() {
        return value;
    }

    public static FileTypeEnum getByType(String type) {
        for (FileTypeEnum fileTypeEnum : FileTypeEnum.values()) {
            if (fileTypeEnum.getValue().equalsIgnoreCase(type)) {
                return fileTypeEnum;
            }
        }
        return null;
    }

}

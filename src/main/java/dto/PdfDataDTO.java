package dto;

import lombok.Data;

/**
 * @description: pdf数据对象
 * @FileName: PdfDataDTO
 * @Author: haxi
 * @Date: 2020/09/29 14:51
 **/
@Data
public class PdfDataDTO {

    private String headerTitle;
    private String content;
    private String logoFile;
    private String cachetFile;
}

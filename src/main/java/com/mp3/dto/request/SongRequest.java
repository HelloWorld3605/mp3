package com.mp3.dto.request;

import com.mp3.enums.Genre;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SongRequest {

    @NotBlank(message = "Tiêu đề không được để trống")
    @Size(min = 6, max = 100, message = "Tiêu đề phải từ 6 đến 100 ký tự")
    private String title;

    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    private String description;

    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0 giây")
    private Integer duration; // in seconds

    @NotBlank(message = "File nhạc (url) không được để trống")
    private String url;

    private String coverUrl;


    private String releaseDate;

    private Genre genre;
}

package com.mp3.dto.response;

import com.mp3.enums.Genre;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SongResponse {
    private String id;
    private String title;
    private String description;
    private Integer duration;       // số giây
    private String formattedDuration; // ví dụ: 3:45
    private String verboseDuration;   // ví dụ: 3 phút 45 giây
    private String url;
    private String coverUrl;
    private String releaseDate;     // yyyy-MM-dd
    private Genre genre;
    private Long playCount;
    private Long likeCount;
    private String createdAt;
    private String updatedAt;

    private String userId;   // thông tin cơ bản user
    private String userName; // nếu cần hiển thị tên người upload
}

package com.url.url_shortner_sb.dtos;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClickEventDTO {
    private LocalDateTime clickDate;
    private Long count;

    public LocalDateTime getClickDate() {
        return clickDate;
    }

    public void setClickDate(LocalDate clickDate) {
        this.clickDate = clickDate;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

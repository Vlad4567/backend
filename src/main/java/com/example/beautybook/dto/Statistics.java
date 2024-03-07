package com.example.beautybook.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Statistics {
    private Long count1;
    private Long count2;
    private Long count3;
    private Long count4;
    private Long count5;

    public Statistics(Map<String, Long> stat) {
        this.count1 = stat.get("count1");
        this.count2 = stat.get("count2");
        this.count3 = stat.get("count3");
        this.count4 = stat.get("count4");
        this.count5 = stat.get("count5");
    }
}

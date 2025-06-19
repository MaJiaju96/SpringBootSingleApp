package com.xiaoma.moudle.webService.entry;

import lombok.Data;

@Data
public class Diff {

    private String field;
    private String after;
    private String before;
}

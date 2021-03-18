package ru.sberbank.ckr.sberboard.increment.utils;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Column {
    private String columnName;
    private String type;
    private boolean isPrimaryKey;
}

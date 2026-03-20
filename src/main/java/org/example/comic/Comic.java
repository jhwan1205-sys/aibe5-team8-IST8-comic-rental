package org.example.comic;

import java.time.LocalDate;
import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter
public class Comic {
    private Long id;
    private String title;
    private int volume;
    private String author;
    private boolean isRented;
    private LocalDate regDate;  // yyyy-MM-dd
}

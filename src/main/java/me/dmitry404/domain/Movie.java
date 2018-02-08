package me.dmitry404.domain;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.web.bind.annotation.RequestMapping;

@Document
@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Movie {
  private String id;
  @NonNull
  private String title;
}

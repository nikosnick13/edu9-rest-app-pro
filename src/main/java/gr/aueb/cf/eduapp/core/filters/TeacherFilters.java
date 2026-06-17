package gr.aueb.cf.eduapp.core.filters;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TeacherFilters {
    private UUID uuid;
    private  String amka;
    private  String vat;
    private String lastname;
    private boolean delete;
    private String region;
}

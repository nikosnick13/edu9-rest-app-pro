package gr.aueb.cf.eduapp.dto;

import java.util.UUID;

public record UserReadOnlyDTO(
        String uuid,
        String username,
        String role
) {
}

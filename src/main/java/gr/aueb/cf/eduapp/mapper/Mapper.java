package gr.aueb.cf.eduapp.mapper;

import gr.aueb.cf.eduapp.dto.UserInsertDTO;
import gr.aueb.cf.eduapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.eduapp.model.User;
import org.springframework.stereotype.Component;

@Component
public class Mapper {
    /**
     * Converts a UserInsertDTO into a User entity.
     *
     * @param userInsertDTO DTO containing user input data
     * @return User entity ready for persistence
     */
    public User mapToUserEntity(UserInsertDTO userInsertDTO){
        return new User(userInsertDTO.username(), userInsertDTO.password());
    }

    /**
     * Converts a User entity into a UserReadOnlyDTO.
     *
     * @param user the User entity retrieved from the database
     * @return a UserReadOnlyDTO containing user data for read-only purposes
     */
    public UserReadOnlyDTO mapToUserReadOnlyDTO(User user){
        return new UserReadOnlyDTO(user.getUuid().toString(), user.getUsername(), user.getRole().getName());
    }

}

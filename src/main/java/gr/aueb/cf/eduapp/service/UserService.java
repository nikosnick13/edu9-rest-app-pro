package gr.aueb.cf.eduapp.service;


import gr.aueb.cf.eduapp.core.exeption.EntityAlreadyExistException;
import gr.aueb.cf.eduapp.core.exeption.EntityInvalidArgumentException;
import gr.aueb.cf.eduapp.dto.UserInsertDTO;
import gr.aueb.cf.eduapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.eduapp.mapper.Mapper;
import gr.aueb.cf.eduapp.model.Role;
import gr.aueb.cf.eduapp.model.User;
import gr.aueb.cf.eduapp.repository.RoleRepository;
import gr.aueb.cf.eduapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistException.class,EntityInvalidArgumentException.class})
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws EntityAlreadyExistException, EntityInvalidArgumentException {
        try {

            if(userRepository.findByUsername(userInsertDTO.username()).isPresent()) {
                throw new EntityAlreadyExistException("User", "User with username" + userInsertDTO.username() + " is already exist");
            }

            User user = mapper.mapToUserEntity(userInsertDTO);
            user.setPassword(passwordEncoder.encode(userInsertDTO.password()));

            Role role = roleRepository.findById(userInsertDTO.roleId()).orElseThrow( () ->
                    new EntityInvalidArgumentException("Role", "Role with id:" + userInsertDTO.roleId() + " does not exist"));

            role.addUser(user);

            userRepository.save(user);
            log.info("The user with username: {} is saved successfully", userInsertDTO.username());

            return mapper.mapToUserReadOnlyDTO(user);

        }catch (EntityAlreadyExistException ex){
            log.error("Save failed. The User is with username = {} is already exist.", userInsertDTO.username());
            throw ex;
        }catch (EntityInvalidArgumentException ex){
            log.error("Save failed. Invalid arguments for user with username: {}",userInsertDTO.username());
            throw ex;
        }
    }

    @Override
    public UserReadOnlyDTO getUserByUUID(UUID uuid) {
        return null;
    }

    @Override
    public UserReadOnlyDTO getUserByUUIDDeleteSoft(UUID uuid) {
        return null;
    }
}

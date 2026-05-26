package gr.aueb.cf.eduapp.service;


import gr.aueb.cf.eduapp.core.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.eduapp.core.exceptions.EntityInvalidArgumentException;
import gr.aueb.cf.eduapp.core.exceptions.EntityNotFoundException;
import gr.aueb.cf.eduapp.dto.UserInsertDTO;
import gr.aueb.cf.eduapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.eduapp.mapper.Mapper;
import gr.aueb.cf.eduapp.model.Role;
import gr.aueb.cf.eduapp.model.User;
import gr.aueb.cf.eduapp.repository.RoleRepository;
import gr.aueb.cf.eduapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor // Είναι για το DI
@Slf4j
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Mapper mapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(rollbackFor = {EntityAlreadyExistsException.class,EntityInvalidArgumentException.class}) //Transactional κανοθν τα servises που κάνουν αλλαγές στην βάση πχ insert updaτe, delete κλπ
    public UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO) throws EntityAlreadyExistsException, EntityInvalidArgumentException {
        try {

            if(userRepository.findByUsername(userInsertDTO.username()).isPresent()) {
                throw new EntityAlreadyExistsException("User", "User with username" + userInsertDTO.username() + " is already exist");
            }

            User user = mapper.mapToUserEntity(userInsertDTO);
            user.setPassword(passwordEncoder.encode(userInsertDTO.password()));

            Role role = roleRepository.findById(userInsertDTO.roleId()).orElseThrow( () ->
                    new EntityInvalidArgumentException("Role", "Role with id:" + userInsertDTO.roleId() + " does not exist"));

            role.addUser(user);

            userRepository.save(user);
            log.info("The user with username: {} is saved successfully", userInsertDTO.username());

            return mapper.mapToUserReadOnlyDTO(user);

        }catch (EntityAlreadyExistsException ex){
            log.error("Save failed. The User is with username = {} is already exist.", userInsertDTO.username());
            throw ex;
        }catch (EntityInvalidArgumentException ex){
            log.error("Save failed. Invalid arguments for user with username: {}",userInsertDTO.username());
            throw ex;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUUID(UUID uuid) throws EntityNotFoundException {
        try {
            User user = userRepository.findByUuid(uuid).
                  orElseThrow(() -> new EntityNotFoundException("User","The user with UUID "+uuid+ " does not found"));

            log.debug("The user with UUID={} found successfully", uuid);
           return mapper.mapToUserReadOnlyDTO(user);

        }
        catch (EntityNotFoundException ex){
            log.error("Get failed. User with UUID: {} not found",uuid);
            throw  ex;
        }
    }

    @Override
    @PreAuthorize("hasAuthority('VIEW_USER')")
    @Transactional(readOnly = true)
    public UserReadOnlyDTO getUserByUUIDDeleteSoft(UUID uuid) throws EntityNotFoundException  {
        try {
            User user = userRepository.findByUuidAndDeletedFalse(uuid).
                    orElseThrow(() -> new EntityNotFoundException("User","The user with UUID "+uuid+ " does not found"));

            log.debug("Active user with UUID={} found successfully", uuid);
            return mapper.mapToUserReadOnlyDTO(user);
        }
        catch (EntityNotFoundException ex){
            log.error("Get failed. Active user with UUID: {} not found",uuid);
            throw  ex;
        }
    }
}

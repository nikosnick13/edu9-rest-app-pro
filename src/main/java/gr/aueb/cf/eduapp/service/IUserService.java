package gr.aueb.cf.eduapp.service;

import gr.aueb.cf.eduapp.core.exeption.EntityAlreadyExistException;
import gr.aueb.cf.eduapp.core.exeption.EntityInvalidArgumentException;
import gr.aueb.cf.eduapp.dto.UserInsertDTO;
import gr.aueb.cf.eduapp.dto.UserReadOnlyDTO;

import java.util.UUID;

public interface IUserService {

    UserReadOnlyDTO saveUser(UserInsertDTO userInsertDTO)
            throws EntityAlreadyExistException, EntityInvalidArgumentException;

    UserReadOnlyDTO getUserByUUID(UUID uuid);
    UserReadOnlyDTO getUserByUUIDDeleteSoft(UUID uuid);


}

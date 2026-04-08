package gr.aueb.cf.eduapp.api;

import gr.aueb.cf.eduapp.core.exeption.EntityAlreadyExistException;
import gr.aueb.cf.eduapp.core.exeption.EntityInvalidArgumentException;
import gr.aueb.cf.eduapp.core.exeption.EntityNotFoundException;
import gr.aueb.cf.eduapp.core.exeption.ValidationException;
import gr.aueb.cf.eduapp.dto.UserInsertDTO;
import gr.aueb.cf.eduapp.dto.UserReadOnlyDTO;
import gr.aueb.cf.eduapp.service.IUserService;
import jakarta.servlet.Servlet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping
    public ResponseEntity<UserReadOnlyDTO> registerUsers(@Valid @RequestBody UserInsertDTO userInsertDTO, BindingResult bindingResult)
        throws ValidationException, EntityInvalidArgumentException, EntityAlreadyExistException {

        if(bindingResult.hasErrors()){
            throw new ValidationException("User", "Invalid user data",bindingResult);
        }

        UserReadOnlyDTO userReadOnlyDTO = userService.saveUser(userInsertDTO);
        //URI location = URI.create("/api/v1/users/" + userReadOnlyDTO.uuid());

        //Παραγει το πληρης URL
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(userReadOnlyDTO.uuid())
                .toUri();

        return  ResponseEntity
                .created(location)
                .body(userReadOnlyDTO);

    }

    @GetMapping("/{uuid}")
    public ResponseEntity<UserReadOnlyDTO> getUsersByUUID(@PathVariable UUID uuid)
        throws EntityNotFoundException{

        return ResponseEntity.ok(userService.getUserByUUIDDeleteSoft(uuid));
    }


}

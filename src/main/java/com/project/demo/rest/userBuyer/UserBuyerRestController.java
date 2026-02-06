package com.project.demo.rest.userBuyer;

import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/usersBuyer")
public class UserBuyerRestController {

    @Autowired
    private UserBuyerRepository UserBuyerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public List<UserBuyer> getAllUsers() {
        return UserBuyerRepository.findAll();
    }

    @PostMapping
    public UserBuyer addUser(@RequestBody UserBuyer user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }
        user.setRole(optionalRole.get());

        user.setStatus("Activo");

        return UserBuyerRepository.save(user);
    }

    @GetMapping("/{id}")
    public UserBuyer getUserById(@PathVariable Long id) {
        return UserBuyerRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<UserBuyer> getUserById(@PathVariable String name) {
        return UserBuyerRepository.findUsersWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    public UserBuyer updateUser(@PathVariable Long id, @RequestBody UserBuyer user) {
        return UserBuyerRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setName(user.getName());
                    existingUser.setLastname(user.getLastname());
                    existingUser.setPicture(user.getPicture());
                    existingUser.setGenre(user.getGenre());
                    existingUser.setDeliveryLocation(user.getDeliveryLocation());
                    existingUser.setPhoneNumber(user.getPhoneNumber());
                    return UserBuyerRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return UserBuyerRepository.save(user);
                });
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        UserBuyerRepository.deleteById(id);
    }

    @PutMapping("/deactivate")
    public ResponseEntity<?> deactivateUser(@RequestBody UserBuyer user) {
        Optional<UserBuyer> optionalUser = UserBuyerRepository.findById(user.getId());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado.");
        }

        UserBuyer userResult = optionalUser.get();
        if (!passwordEncoder.matches(user.getPassword(), userResult.getPassword())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña incorrecta.");
        }

        userResult.setStatus("Inactivo");
        UserBuyerRepository.save(userResult);

        return ResponseEntity.ok("Cuenta desactivada con éxito.");
    }


    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserBuyer authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserBuyer) authentication.getPrincipal();
    }
}

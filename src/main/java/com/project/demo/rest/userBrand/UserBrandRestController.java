package com.project.demo.rest.userBrand;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailInfo;
import com.project.demo.logic.entity.email.EmailService;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usersBrand")
public class UserBrandRestController {
    @Autowired
    private UserBrandRepository UserBrandRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private EmailService emailService;

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public List<UserBrand> getAllBrandActive() {
        return UserBrandRepository.findUserBrandByStatusActive();
    }

    @GetMapping("/newRequests")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'USER')")
    public  List<UserBrand> getNewBrandByRequest() {
        return UserBrandRepository.findUserBrandByStatusInactive();
    }

    @PostMapping
    public UserBrand addUser(@RequestBody UserBrand user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER_BRAND);

        if (optionalRole.isEmpty()) {
            return null;
        }
        user.setRole(optionalRole.get());

        user.setStatus("Inactivo");

        return UserBrandRepository.save(user);
    }

    @GetMapping("/{id}")
    public UserBrand getUserById(@PathVariable Long id) {
        return UserBrandRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/filterByName/{name}")
    public List<UserBrand> getUserById(@PathVariable String name) {
        return UserBrandRepository.findUsersWithCharacterInName(name);
    }

    @PutMapping("/{id}")
    public UserBrand updateUser(@PathVariable Long id, @RequestBody UserBrand user) {
        return UserBrandRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setLogoType(user.getLogoType());
                    existingUser.setMainLocationAddress(user.getMainLocationAddress());
                    existingUser.setBrandCategories(user.getBrandCategories());
                    return UserBrandRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return UserBrandRepository.save(user);
                });
    }

    @PutMapping("/upRate/{id}")
    public UserBrand updateRate(@PathVariable Long id, @RequestBody UserBrand user) {
        System.out.println(id);
        UserBrand retrievedUser = getUserById(id);
        String name = retrievedUser.getBrandName();
        return UserBrandRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setRate(user.getRate());
                    return UserBrandRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return UserBrandRepository.save(user);
                });
    }

    @PutMapping("/upStatus/{id}")
    public UserBrand updateStatus(@PathVariable Long id, @RequestBody UserBrand user) {
        System.out.println(id);
        UserBrand retrievedUser = getUserById(id);
        String name = retrievedUser.getBrandName();
        return UserBrandRepository.findById(id)
                .map(existingUser -> {
                    existingUser.setStatus(user.getStatus());

                    if ("Activo".equals(user.getStatus())) {
                        String emailBody = "¡Hola " + name + "!\n\n" +
                                "Tu cuenta ha sido activada correctamente.";

                        sendStatusUpdateEmail(name, emailBody);
                    } else {
                        String emailBody = "¡Hola " + name + "!\n\n" +
                                "Tu cuenta ha sido desactivada.";
                        sendStatusUpdateEmail(name, emailBody);
                    }

                    return UserBrandRepository.save(existingUser);
                })
                .orElseGet(() -> {
                    user.setId(id);
                    return UserBrandRepository.save(user);
                });
    }

    private void sendStatusUpdateEmail(String name, String emailBody) {
        try {
            EmailDetails emailDetails = createEmailDetails(name, emailBody);
            emailService.sendEmail(emailDetails);
            System.out.println("El correo se envio con exito.");
        } catch (IOException e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
    }

    private EmailDetails createEmailDetails(String name, String emailBody) {
        String email = "robertaraya382@gmail.com";
        EmailInfo fromAddress = new EmailInfo("JBart", email);
        EmailInfo toAddress = new EmailInfo(name, email);
        System.out.println(name);
        String subject = "Actualización de Estado";

        return new EmailDetails(fromAddress, toAddress, subject, emailBody);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {

        UserBrand retrievedUser = getUserById(id);
        String name = retrievedUser.getBrandName();

        UserBrandRepository.deleteById(id);

        String emailBody = "¡Hola " + name + "!\n\n" +
                "Lamentablemente, tu solicitud ha sido rechazada.";

        sendStatusUpdateEmail(name, emailBody);

    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public UserBrand authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (UserBrand) authentication.getPrincipal();
    }

}
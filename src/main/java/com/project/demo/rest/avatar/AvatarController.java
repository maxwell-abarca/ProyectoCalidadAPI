package com.project.demo.rest.avatar;

import com.project.demo.logic.entity.avatar.Avatar;
import com.project.demo.logic.entity.avatar.AvatarRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import com.project.demo.rest.userBuyer.UserBuyerRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("avatares")
public class AvatarController {
    @Autowired
    private AvatarRepository avatarRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserBuyerRestController userBuyerRestController;

    @Autowired
    private UserBuyerRepository userBuyerRepository;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','USER','SUPER_ADMIN')")
    public List<Avatar> getAllAvatar() {
        return avatarRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER')")
    public Avatar addAvatar(@RequestBody Avatar avatar) {

        if (avatar.getFace() == null || avatar.getHead() == null || avatar.getPose() == null) {
            throw new IllegalArgumentException("Avatar cannot be null");
        }

        Long buyerId = userBuyerRestController.authenticatedUser().getId();
        UserBuyer userBuyer = userBuyerRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("User Buyer not found with id: " + buyerId));

        Optional<Avatar> existingAvatar = avatarRepository.findByUserBuyer(buyerId);

        if (existingAvatar.isPresent()) {
            throw new IllegalArgumentException("Avatar already exists.");
        } else {
            avatar.setUserBuyer(userBuyer);
            return avatarRepository.save(avatar);
        }
    }

    @GetMapping("/{id}")
    public Avatar getAvatarById(@PathVariable Long id) {
        return avatarRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/AvatarByUser")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public Optional<Avatar> getAvatarByUserBrand() {
        Long buyerId = userBuyerRestController.authenticatedUser().getId();

        return avatarRepository.findByUserBuyer(buyerId);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER')")
    public void deleteAvatar(@PathVariable Long id) {
        avatarRepository.deleteById(id);
    }

}
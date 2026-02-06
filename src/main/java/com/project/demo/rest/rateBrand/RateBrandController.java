package com.project.demo.rest.rateBrand;

import com.project.demo.logic.entity.rateBrand.RateBrand;
import com.project.demo.logic.entity.rateBrand.RateBrandRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import com.project.demo.rest.userBrand.UserBrandRestController;
import com.project.demo.rest.userBuyer.UserBuyerRestController;
import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ratesBrand")
public class RateBrandController {

    @Autowired
    private RateBrandRepository rateBrandRepository;

    @Autowired
    private UserBrandRepository userBrandRepository;

    @Autowired
    private UserBuyerRepository userBuyerRepository;


    @Autowired
    private UserBuyerRestController userBuyerRestController;

    private UserBrandRestController userBrandRestController;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<RateBrand> getAllRatesBrand() {
        return rateBrandRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public RateBrand addRateBrand(@RequestBody RateBrand rateBrand) {

        if (rateBrand == null || rateBrand.getRate() == null || rateBrand.getUserBrand().getId() == null) {
            throw new IllegalArgumentException("RateBrand or users cannot be null");
        }

        Long brandId = rateBrand.getUserBrand().getId();
        Long buyerId = userBuyerRestController.authenticatedUser().getId();

        UserBrand userBrand = userBrandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("User Brand not found with id: " + brandId));


        UserBuyer userBuyer = userBuyerRepository.findById(buyerId)
                .orElseThrow(() -> new IllegalArgumentException("User Buyer not found with id: " + buyerId));

        // Check if the user has already rated this brand
        Optional<RateBrand> existingRating = rateBrandRepository.findByIdBuyerAndIdBrand(buyerId, brandId);

        if (existingRating.isPresent()) {
            throw new IllegalArgumentException("User has already rated this brand.");
        } else {
            rateBrand.setUserBrand(userBrand);
            rateBrand.setUserBuyer(userBuyer);

            RateBrand savedRateBrand = rateBrandRepository.save(rateBrand);
            Integer averageRating = calculateAverageRate(brandId);
            userBrand.setRate(averageRating);
            userBrandRepository.save(userBrand);

            return savedRateBrand;
        }
    }

    private Integer calculateAverageRate(Long brandId) {
        List<RateBrand> rates = rateBrandRepository.findByUserBrandId(brandId);
        if (rates.isEmpty()) {
            return 0;
        }
        int sum = rates.stream().mapToInt(RateBrand::getRate).sum();
        return sum / rates.size();
    }

    @GetMapping("/{id}")
    public RateBrand getRateBrandById(@PathVariable Long id) {
        return rateBrandRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @GetMapping("/rate/{brandId}")
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public Optional<RateBrand> hasRatedBrand(@PathVariable Long brandId) {
        Long userId = userBuyerRestController.authenticatedUser().getId();

        return rateBrandRepository.findByIdBuyerAndIdBrand(userId,brandId);
    }
}

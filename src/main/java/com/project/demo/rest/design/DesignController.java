package com.project.demo.rest.design;

import com.project.demo.logic.entity.design.Design;
import com.project.demo.logic.entity.design.DesignRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/designs")
public class DesignController {

    @Autowired
    private DesignRepository designRepository;

    @GetMapping("/user/{id}")
    public List<Design> getDesignsByUserId(@PathVariable Long id) {
        return designRepository.findByUserId(id);
    }

    @GetMapping("/{id}")
    public Design getDesignById(@PathVariable Long id) {
        return designRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public Design addDesign(@RequestBody Design design) {
        return designRepository.save(design);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public void deleteDesign(@PathVariable Long id) {
        designRepository.deleteById(id);
    }

}

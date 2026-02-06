package com.project.demo.rest.chatbot;

import com.project.demo.logic.entity.category.Category;
import com.project.demo.logic.entity.chatbot.Chatbot;
import com.project.demo.logic.entity.chatbot.ChatbotRepository;
import com.project.demo.logic.entity.rateBrand.RateBrand;
import com.project.demo.logic.entity.rateBrand.RateBrandRepository;
import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import com.project.demo.logic.entity.userBuyer.UserBuyer;
import com.project.demo.logic.entity.userBuyer.UserBuyerRepository;
import com.project.demo.rest.userBrand.UserBrandRestController;
import com.project.demo.rest.userBuyer.UserBuyerRestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotRepository chatbotRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER_BRAND','SUPER_ADMIN', 'USER')")
    public List<Chatbot> getAll() {
        return chatbotRepository.findAll();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER','SUPER_ADMIN')")
    public Chatbot addChatbot(@RequestBody Chatbot chatbot) {

        if (chatbot.getQuestion() == null || chatbot.getAnswer() == null) {
            throw new IllegalArgumentException("Chatbot cannot be null");
        }

            return chatbotRepository.save(chatbot);
    }

    @GetMapping("/{id}")
    public Chatbot getChatbotById(@PathVariable Long id) {
        return chatbotRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public Chatbot updateChatbot(@PathVariable Long id, @RequestBody Chatbot chatbot) {
        return chatbotRepository.findById(id)
                .map(existingChatbot -> {
                    existingChatbot.setQuestion(chatbot.getQuestion());
                    existingChatbot.setAnswer(chatbot.getAnswer());
                    return chatbotRepository.save(existingChatbot);
                })
                .orElseGet(() -> {
                    chatbot.setId(id);
                    return chatbotRepository.save(chatbot);
                });
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN')")
    public void deleteChatbot(@PathVariable Long id) {
        chatbotRepository.deleteById(id);
    }
}

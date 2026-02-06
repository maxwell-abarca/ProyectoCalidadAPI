package com.project.demo.logic.entity.chatbot;

import com.project.demo.logic.entity.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface ChatbotRepository extends JpaRepository<Chatbot, Long> {
}

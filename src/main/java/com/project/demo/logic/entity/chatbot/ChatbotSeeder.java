package com.project.demo.logic.entity.chatbot;

import com.project.demo.logic.entity.category.CategoryRepository;
import com.project.demo.logic.entity.rol.Role;
import com.project.demo.logic.entity.rol.RoleEnum;
import com.project.demo.logic.entity.rol.RoleRepository;
import com.project.demo.logic.entity.userBrand.UserBrand;
import com.project.demo.logic.entity.userBrand.UserBrandRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;


@Component
public class ChatbotSeeder implements ApplicationListener<ContextRefreshedEvent> {
    private final ChatbotRepository chatbotRepository;
    private final PasswordEncoder passwordEncoder;


    public ChatbotSeeder(
            ChatbotRepository chatbotRepository,
            PasswordEncoder passwordEncoder) {
        this.chatbotRepository = chatbotRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        this.createQuestion1();
        this.createQuestion2();
        this.createQuestion3();
        this.createQuestion4();
        this.createQuestion5();
        this.createQuestion6();
        this.createQuestion7();
        this.createQuestion8();

    }

    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }

    private void createQuestion1() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo me registro en la aplicación?");
        chatbot.setAnswer("Para registrarte, haz clic en el botón de Registro en la página principal, completa el formulario con tu información básica y crea tu perfil.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion2() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo creo y personalizo mi avatar en 3D?");
        chatbot.setAnswer("Después de registrarte, serás guiado a la sección de personalización de avatar donde podrás diseñar tu avatar en 3D ajustando características como ropa, accesorios y más.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion3() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo puedo personalizar un producto?");
        chatbot.setAnswer("Ve a la sección de personalización, selecciona el producto que deseas personalizar y usa las herramientas de diseño interactivo para crear tu diseño único.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion4() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo puedo previsualizar mi diseño en 3D?");
        chatbot.setAnswer("Una vez que hayas terminado de personalizar tu producto, haz clic en la opción de previsualización en 3D para ver cómo se verá el producto final antes de realizar la compra.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion5() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo puedo seguir el estado de mi pedido?");
        chatbot.setAnswer("Puedes seguir el estado de tu pedido en tiempo real a través de tu perfil. Recibirás notificaciones en cuatro etapas: Compra realizada, En proceso de producción, Producto listo y Producto enviado.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion6() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Puedo personalizar productos de diferentes marcas?");
        chatbot.setAnswer("Sí, puedes personalizar productos de diversas marcas que están registradas en nuestra plataforma. Cada marca tiene una variedad de productos disponibles para personalización.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion7() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo puedo personalizar la ropa y los accesorios de mi avatar?");
        chatbot.setAnswer("Después de comprar productos personalizados, podrás vestir a tu avatar con esos productos desde la sección de personalización de avatar en tu perfil.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }

    private void createQuestion8() {
        Chatbot chatbot = new Chatbot();
        chatbot.setQuestion("¿Cómo puedo contactar al servicio al cliente?");
        chatbot.setAnswer("Puedes contactar a nuestro servicio al cliente a través del chatbot integrado en la plataforma, o enviando un correo electrónico a soporte@jBart.com.");

        var chat = new Chatbot();
        chat.setQuestion(chatbot.getQuestion());
        chat.setAnswer(chatbot.getAnswer());
        chatbotRepository.save(chat);
    }
}

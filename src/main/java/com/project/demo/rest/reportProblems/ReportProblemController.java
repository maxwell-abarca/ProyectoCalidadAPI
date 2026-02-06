package com.project.demo.rest.reportProblems;

import com.project.demo.logic.entity.email.EmailDetails;
import com.project.demo.logic.entity.email.EmailInfo;
import com.project.demo.logic.entity.email.EmailService;
import com.project.demo.logic.entity.reportProblems.ReportProblems;
import com.project.demo.logic.entity.reportProblems.ReportProblemsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("reportProblems")
public class ReportProblemController {
    @Autowired
    private EmailService emailService;

    @Autowired
    private ReportProblemsRepository reportProblemsRepository;

    @GetMapping("/{id}")
    public Optional<ReportProblems> getReportProblemById(@PathVariable Long id) {
        return reportProblemsRepository.findById(id);
    }

    @GetMapping("/area/{problemArea}")
    public List<ReportProblems> getReportProblemsByProblemArea(@PathVariable String problemArea) {
        return reportProblemsRepository.findByProblemArea(problemArea);
    }

    @GetMapping("/status/{status}")
    public List<ReportProblems> getReportProblemsByStatus(@PathVariable String status) {
        return reportProblemsRepository.findByStatus(status);
    }

    @GetMapping
    public List<ReportProblems> getAllReportProblems() {
        return reportProblemsRepository.findAll();
    }

    @PostMapping("/createReportProblem")
    public ReportProblems createReportProblem(@RequestBody ReportProblems reportProblems) {
        String emailbody = "<p>El área del problema es:<br>" + reportProblems.getProblemArea() + "</p>" +
                "<p>La descripción del problema:<br>" + reportProblems.getDescription() + "</p>" +
                "<p>Estado del problema a resolver:<br>" + reportProblems.getStatus() + "</p>";

        EmailDetails emailDetails = createEmailDetails(emailbody);
        try {
            emailService.sendEmail(emailDetails);
            System.out.println("Correo electrónico con reporte de problema enviado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al enviar el correo electrónico: " + e.getMessage());
        }
        return reportProblemsRepository.save(reportProblems);
    }

    private String buildEmailBody(String name) {
        return String.format("Hola %s,\n\nGracias por mandar tu reporte para hacer el bien en nuestra plataforma. Tu reporte ha sido recibido y será procesado en las próximas horas por un administrador para realizar la corrección del problema.\n\nSaludos,\nEquipo JBart", name);
    }

    private EmailDetails createEmailDetails(String emailBody) {
        EmailInfo fromAddress = new EmailInfo("JBart", "robertaraya382@gmail.com"); // Ajustar según tu configuración
        EmailInfo toAddress = new EmailInfo("Admin", "robertaraya382@gmail.com");
        String subject = "Reporte de Problema";

        return new EmailDetails(fromAddress, toAddress, subject, emailBody);
    }

    @PutMapping("/{id}")
    public ReportProblems updateReportProblem(@PathVariable Long id, @RequestBody ReportProblems reportProblems) {
        Optional<ReportProblems> existingReportProblem = reportProblemsRepository.findById(id);

        if (existingReportProblem.isPresent()) {
            ReportProblems reportToUpdate = existingReportProblem.get();
            reportToUpdate.setProblemArea(reportProblems.getProblemArea());
            reportToUpdate.setDescription(reportProblems.getDescription());
            reportToUpdate.setStatus(reportProblems.getStatus());
            return reportProblemsRepository.save(reportToUpdate);
        } else {
            throw new RuntimeException("Report with ID " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteReportProblem(@PathVariable Long id) {
        reportProblemsRepository.deleteById(id);
    }
}


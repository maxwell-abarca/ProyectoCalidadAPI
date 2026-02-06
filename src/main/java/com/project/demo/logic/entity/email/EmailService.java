package com.project.demo.logic.entity.email;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${key}")
    private String key;

    public String sendEmail(EmailDetails emailDetails) throws IOException {
        EmailInfo fromInfo = emailDetails.getFromAddress();
        Email fromEmail = setEmail(fromInfo.getName(),fromInfo.getEmailAddress());

        EmailInfo toInfo = emailDetails.getToAddress();
        Email toEmail = setEmail(fromInfo.getName(),fromInfo.getEmailAddress());

        String htmlContent = createHtmlContent(emailDetails.getToAddress().getName(), emailDetails.getEmailBody());
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(fromEmail, emailDetails.getSuject(),toEmail,content);

        SendGrid grid = new SendGrid(key);


        Request request = new Request();

        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());


        Response response = grid.api(request);
        return response.getBody();
    }

    private Email setEmail(String name, String emailAddress){
        Email email = new Email();
        email.setEmail(emailAddress);
        email.setName(name);
        return email;
    }

    private String createHtmlContent(String name, String emailBody) {
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset='utf-8'>\n" +
                "    <meta name='viewport' content='width=device-width, initial-scale=1, shrink-to-fit=no'>\n" +
                "    <!-- Bootstrap CSS -->\n" +
                "    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css' rel='stylesheet' integrity='sha384-wEmeIV1mKuiNpC+IOBjI7aAzPcEZeedi5yW5f2yOq55WWLwNGmvvx4Um1vskeMj0' crossorigin='anonymous'>\n" +
                "    <title>Correo de Confirmación</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class= 'container mb-3 rounded' style='background-color: #F4DFC8; text-align: center; width: 38%; height: 750px; color: #282828; margin-top: 20px;'>\n" +
                "        <div class='container mb-3 rounded' style='color: #282828;'>\n" +
                "            <div style='width: 100%; text-align: center; color: #282828;'>\n" +
                "                <img src='https://res.cloudinary.com/dyrj7gds0/image/upload/v1724443748/JBArt2_hgsgmk_owhdlq.png' class='me-0' height='300' alt='' class='d-inline-block align-text-top' style='text-align: center; margin-top: 10px;'/>\n" +
                "            </div>\n" +
                "            <div style='width: 100%; text-align: center; color: #282828;'>\n" +
                "                <h1 style='margin-top: 7px; text-align: center;'> ¡Hola " + " " + name + "!</h1>\n" +
                "                <p style='margin-top: 25px; text-align: center;'>" + emailBody + "</p>\n" +
                "                <p style='margin-top: 25px; text-align: center;'>Atentamente,<br>El equipo de JBart</p>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div class='container mb-5' style='width: 100%; text-align: center; margin-top: 180px;'>\n" +
                "            <div class='container'>\n" +
                "                <div class='container'>\n" +
                "                    &copy; 2023 - JBart\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js' integrity='sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8' crossorigin='anonymous'></script>\n" +
                "</body>\n" +
                "</html>";

    }

}
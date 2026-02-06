package com.project.demo.logic.entity.notificationTemplate;

import com.project.demo.logic.entity.notification.Notification;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "notification_templates")
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "redirectLink")
    private String redirectLink;

    @OneToMany(mappedBy = "notificationTemplate", fetch = FetchType.LAZY)
    private List<Notification> notifications;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public String getRedirectLink() {
        return redirectLink;
    }

    public void setRedirectLink(String redirectLink) {
        this.redirectLink = redirectLink;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

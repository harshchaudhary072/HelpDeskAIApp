package com.substring.helpdesk.entity;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "help_desk_tickets")
public class Ticket implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // changed from primitive long to Long to allow null
    
    @Lob
    private String summary;
    
    @Enumerated(EnumType.STRING)
    private  Priority priority;
    
    private String category;
    
    @Column(length = 1000)
    private String description;
    
    @Column(unique=true)
    private String email;
    
    @Column(unique=true)
    private String username;
    
    @JsonDeserialize(using = LocalDateTimeLenientDeserializer.class)
    private LocalDateTime createdOn;
    @JsonDeserialize(using = LocalDateTimeLenientDeserializer.class)
    private LocalDateTime updatedOn;
    
    @Enumerated(EnumType.STRING)
    private Status status;
    
    // No-arg constructor (replaces @NoArgsConstructor)
    public Ticket() {
    }
    
    // All-args constructor (replaces @AllArgsConstructor)
    public Ticket(Long id, String summary, Priority priority, String category, String description, String email,
            String username, LocalDateTime createdOn, LocalDateTime updatedOn, Status status) {
        this.id = id;
        this.summary = summary;
        this.priority = priority;
        this.category = category;
        this.description = description;
        this.email = email;
        this.username = username;
        this.createdOn = createdOn;
        this.updatedOn = updatedOn;
        this.status = status;
    }

    // Manual builder (replaces @Builder)
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String summary;
        private Priority priority;
        private String category;
        private String description;
        private String email;
        private String username;
        private LocalDateTime createdOn;
        private LocalDateTime updatedOn;
        private Status status;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder summary(String summary) {
            this.summary = summary;
            return this;
        }

        public Builder priority(Priority priority) {
            this.priority = priority;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder createdOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Builder updatedOn(LocalDateTime updatedOn) {
            this.updatedOn = updatedOn;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Ticket build() {
            return new Ticket(id, summary, priority, category, description, email, username, createdOn, updatedOn, status);
        }
    }

    // Explicit getters and setters (replaces @Getter/@Setter)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(LocalDateTime updatedOn) {
        this.updatedOn = updatedOn;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    // Explicit toString implementation (replaces @ToString)
    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", summary='" + summary + '\'' +
                ", priority=" + priority +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", status=" + status +
                '}';
    }
    
    @PrePersist
    void preSave() {
        if(this.createdOn == null) {
            this.createdOn = LocalDateTime.now();
        }
        this.updatedOn = LocalDateTime.now();
    }
    
    void preUpdate() {
        this.updatedOn = LocalDateTime.now();
    }

    // Lenient LocalDateTime deserializer: accepts epoch millis/seconds, space-formatted datetimes and datetimes with zone names like "UTC".
    public static class LocalDateTimeLenientDeserializer extends JsonDeserializer<LocalDateTime> {

        private static final DateTimeFormatter[] LOCAL_FORMATTERS = new DateTimeFormatter[] {
            DateTimeFormatter.ISO_LOCAL_DATE_TIME,
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        };

        // regex to match 'yyyy-MM-dd HH:mm' or with seconds plus a zone token after a space
        private static final Pattern DATETIME_WITH_ZONE = Pattern.compile("^(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}(?::\\d{2})?)\\s+(.+)$");

        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            if (text == null) {
                return null;
            }
            text = text.trim();
            if (text.length() == 0) {
                return null;
            }

            // numeric epoch (seconds or milliseconds)
            if (text.matches("^\\d+$")) {
                try {
                    long v = Long.parseLong(text);
                    // if length == 10, treat as seconds -> millis
                    if (text.length() == 10) {
                        v = v * 1000L;
                    }
                    Instant instant = Instant.ofEpochMilli(v);
                    return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                } catch (NumberFormatException ex) {
                    // fallthrough to string parsing
                }
            }

            // try local formatters
            for (DateTimeFormatter f : LOCAL_FORMATTERS) {
                try {
                    return LocalDateTime.parse(text, f);
                } catch (DateTimeParseException e) {
                    // try next
                }
            }

            // try 'datetime + zone' by splitting datetime part and zone token
            Matcher m = DATETIME_WITH_ZONE.matcher(text);
            if (m.matches()) {
                String datetimePart = m.group(1);
                String zonePart = m.group(2).trim();
                // parse the datetime part using the local formatters
                LocalDateTime ldt = null;
                for (DateTimeFormatter f : LOCAL_FORMATTERS) {
                    try {
                        ldt = LocalDateTime.parse(datetimePart, f);
                        break;
                    } catch (DateTimeParseException e) {
                        // continue
                    }
                }
                if (ldt != null) {
                    try {
                        ZoneId zid = ZoneId.of(zonePart);
                        Instant inst = ldt.atZone(zid).toInstant();
                        return LocalDateTime.ofInstant(inst, ZoneId.systemDefault());
                    } catch (Exception e) {
                        // failed to interpret zonePart as ZoneId - fall through
                    }
                    // handle common token 'UTC' explicitly
                    if ("UTC".equalsIgnoreCase(zonePart)) {
                        Instant inst = ldt.atZone(ZoneId.of("UTC")).toInstant();
                        return LocalDateTime.ofInstant(inst, ZoneId.systemDefault());
                    }
                }
            }

            // try parse as Instant (ISO instant)
            try {
                Instant inst = Instant.parse(text);
                return LocalDateTime.ofInstant(inst, ZoneId.systemDefault());
            } catch (DateTimeParseException e) {
                // give up
            }

            throw new IOException("Unparseable LocalDateTime: '" + text + "'");
        }
    }

}
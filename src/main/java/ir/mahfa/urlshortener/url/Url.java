package ir.mahfa.urlshortener.url;

import com.fasterxml.jackson.annotation.JsonIgnore;
import ir.mahfa.urlshortener.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "urls")
public class Url {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Id
    @Column(name = "url_key", nullable = false, length = 128)
    private String urlKey;


    @Column(name = "destination", unique = true, nullable = false, length = 2048)
    private String destination;

    @Column(name = "views")
    private int views;

    @Column(name = "last_use")
    private LocalDate lastUse;

    @JsonIgnore
    public long addViews() {
        return ++views;
    }
}
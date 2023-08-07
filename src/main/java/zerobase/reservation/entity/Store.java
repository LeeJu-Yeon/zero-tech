package zerobase.reservation.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Manager manager;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    private String phone;

    @Lob
    private String description;

    private Long reviewCount;

    private Double averageRating;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}

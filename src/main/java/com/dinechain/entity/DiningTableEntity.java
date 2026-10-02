package com.dinechain.entity;

import com.dinechain.constant.TableStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "dining_tables", uniqueConstraints = @UniqueConstraint(columnNames = {"area_id", "name"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DiningTableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "area_id", nullable = false)
    private Integer areaId;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private Integer capacity = 4;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TableStatus status = TableStatus.AVAILABLE;

    @Column(name = "qr_token", length = 64, unique = true)
    private String qrToken;
}

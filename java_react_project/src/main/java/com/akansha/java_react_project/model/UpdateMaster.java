package com.akansha.java_react_project.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "update_master")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // ✅ One UpdateMaster → Many UpdateDetails
    @OneToMany(mappedBy = "updateMaster", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UpdateDetails> updateDetailsMaster = new ArrayList<>();

    // ✅ Reference to User who made the update
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    // ✅ Date when updated
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    // ✅ Status: 0 = inactive, 1 = active
    @Column(length = 1)
    private String status = "0";
}

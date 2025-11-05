package com.akansha.java_react_project.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "update_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ✅ Link to parent UpdateMaster
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "update_master_id")
    private UpdateMaster updateMaster;


    // ✅ Store all changed fields
    @ElementCollection
    @CollectionTable(
        name = "update_fields",
        joinColumns = @JoinColumn(name = "update_details_id")
    )
    private List<FieldChange> fieldsUpdated = new ArrayList<>();

    // ✅ When the update happened
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedOn = new Date();

    // ✅ Who made the change
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;

    // --- Inner class for flexible field tracking ---
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldChange {
        private String fieldName;

        @Column(columnDefinition = "TEXT")
        private String oldValue;

        @Column(columnDefinition = "TEXT")
        private String newValue;
    }
}

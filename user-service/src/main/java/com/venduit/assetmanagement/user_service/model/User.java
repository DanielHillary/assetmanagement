package com.venduit.assetmanagement.user_service.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends Auditable<String>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String email;
    @Column(name = "user_id")
    private String userId;
    private String firstName;
    private String lastName;
    @Setter
    private String username;
    private String photos;
    private String phoneNumber;
    private String resetOtp;
    private String userTimeZone;
    private LocalDateTime resetOtpRequestTime;
    private LocalDateTime otpRequestTime;
    private boolean emailVerified;
    private String verificationOtp;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonManagedReference
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;

}

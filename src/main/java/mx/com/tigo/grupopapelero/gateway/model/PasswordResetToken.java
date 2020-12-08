package mx.com.tigo.grupopapelero.gateway.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="password_reset_token")
@Data
public class PasswordResetToken {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_token")
    private Integer id;
  
    @Column(name="token")
    private String token;
    
    @Column(name="uuid")
    private String uuid;
  
    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "id")
    private User usuario;
  
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiracion;
    
}

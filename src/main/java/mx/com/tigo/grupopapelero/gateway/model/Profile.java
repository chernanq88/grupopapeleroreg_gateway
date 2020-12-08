package mx.com.tigo.grupopapelero.gateway.model;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="perfil")
@Entity
public class Profile implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2275843224655544309L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
    private String displayName;
    private String profilePictureUrl;
    private Date birthday;
 
}

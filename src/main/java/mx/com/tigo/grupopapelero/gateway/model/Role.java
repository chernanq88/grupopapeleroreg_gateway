package mx.com.tigo.grupopapelero.gateway.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="role")
@Entity
public class Role implements Serializable{
	
	private static final long serialVersionUID = -5890742261667919775L;

	
	public final static Role USER = new Role(1,"USER");
    public final static Role FACEBOOK_USER = new Role(2,"FACEBOOK_USER");
    public final static Role GOOGLE_USER = new Role(3,"GOOGLE_USER");

    public Role(String string) {
		this.name=string;
	}

    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private int id;
    
    private String name;
}


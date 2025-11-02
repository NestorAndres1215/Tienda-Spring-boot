package evaluacioncontinua.entity;

import evaluacioncontinua.util.Mensajes;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "clientes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = Mensajes.NOMBRE_REQUERIDO)
	@Column(nullable = false)
	private String nombre;

	@NotEmpty(message = Mensajes.APELLIDO_REQUERIDO)
	@Column(nullable = false)
	private String apellido;

	@NotEmpty(message = Mensajes.EMAIL_REQUERIDO)
	@Email(message = Mensajes.EMAIL_INVALIDO)
	@Column(nullable = false)
	private String email;

	@Column(name = "create_at", updatable = false)
	@Temporal(TemporalType.DATE)
	private Date createAt;

	@Column(nullable = false)
	private String foto;

	@PrePersist
	protected void prePersist() {
		this.createAt = new Date();
	}
}
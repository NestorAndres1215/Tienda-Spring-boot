package evaluacioncontinua.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import evaluacioncontinua.util.Mensajes;
import lombok.*;

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
	@Size(min = 2, max = 50, message = Mensajes.NOMBRE_TAMANIO)
	@Column(nullable = false)
	private String nombre;

	@NotEmpty(message = Mensajes.APELLIDO_REQUERIDO)
	@Size(min = 2, max = 50, message = Mensajes.APELLIDO_TAMANIO)
	@Column(nullable = false)
	private String apellido;

	@NotEmpty(message = Mensajes.EMAIL_REQUERIDO)
	@Email(message = Mensajes.EMAIL_INVALIDO)
	@Column(nullable = false, unique = true)
	private String email;

	@Column(name = "create_at", updatable = false)
	@Temporal(TemporalType.DATE)
	private Date createAt;

	@NotEmpty(message = Mensajes.FOTO_REQUERIDA)
	@Column(nullable = false)
	private String foto;

	@PrePersist
	protected void prePersist() {
		this.createAt = new Date();
	}
}

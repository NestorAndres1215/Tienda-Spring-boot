package evaluacioncontinua.entity;

import evaluacioncontinua.util.Mensajes;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message = Mensajes.NOMBRE_PRODUCTO_REQUERIDO)
	@Column(nullable = false, length = 100)
	private String nombre;

	@NotEmpty(message = Mensajes.CATEGORIA_PRODUCTO_REQUERIDA)
	@Column(nullable = false, length = 50)
	private String categoria;

	@Positive(message = Mensajes.PRECIO_PRODUCTO_INVALIDO)
	@Column(nullable = false)
	private double precio;

	@Column(name = "create_at", nullable = false, updatable = false)
	private LocalDate createAt;

	@Column(nullable = false)
	private String foto;

	@PrePersist
	protected void prePersist() {
		this.createAt = LocalDate.now();
	}
}

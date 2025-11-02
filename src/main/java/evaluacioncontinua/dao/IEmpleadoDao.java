package evaluacioncontinua.dao;

import org.springframework.data.jpa.repository.Query;

import evaluacioncontinua.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IEmpleadoDao extends JpaRepository<Empleado, Long> {

    // Buscar por nombre (contenga texto, ignorando mayúsculas)
    List<Empleado> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por apellido
    List<Empleado> findByApellidoContainingIgnoreCase(String apellido);

    // Buscar por email
    List<Empleado> findByEmailContainingIgnoreCase(String email);

    // Buscar por nombre, apellido o email
    @Query("SELECT e FROM Empleado e WHERE " +
            "LOWER(e.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.apellido) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Empleado> buscarPorNombreApellidoEmail(@Param("keyword") String keyword);
}

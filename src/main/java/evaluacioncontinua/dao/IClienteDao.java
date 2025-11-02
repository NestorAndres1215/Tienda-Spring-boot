package evaluacioncontinua.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import evaluacioncontinua.entity.Cliente;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface IClienteDao extends JpaRepository<Cliente, Long> {
 
    boolean existsByEmail(String email);
    // Buscar por nombre
    List<Cliente> findByNombreContainingIgnoreCase(String nombre);

    // Buscar por apellido
    List<Cliente> findByApellidoContainingIgnoreCase(String apellido);

    // Buscar por email
    List<Cliente> findByEmailContainingIgnoreCase(String email);

    // Buscar por nombre, apellido o email
    @Query("SELECT c FROM Cliente c WHERE " +
            "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.apellido) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.email) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Cliente> buscarPorNombreApellidoEmail(@Param("keyword") String keyword);
}

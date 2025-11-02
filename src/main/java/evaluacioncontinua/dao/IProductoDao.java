package evaluacioncontinua.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


import evaluacioncontinua.entity.Producto;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IProductoDao  extends CrudRepository <Producto,Long>{

    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    List<Producto> findByCategoriaIgnoreCase(String categoria);

    @Query("SELECT p FROM Producto p WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.categoria) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Producto> buscarPorNombreOCategoria(@Param("keyword") String keyword);

    List<Producto> findTop10ByOrderByPrecioDesc();

    List<Producto> findTop10ByOrderByPrecioAsc();
}

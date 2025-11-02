package evaluacioncontinua.service;

import java.util.List;

import evaluacioncontinua.entity.Producto;

public interface IProductoService {
    List<Producto> listarProductos();

    Producto crearProducto(Producto producto);

    Producto editarProducto(Long id);

    void eliminarProducto(Long id);
    Producto obtenerPorId(Long id); // ✅ nuevo
    // Buscar productos
    List<Producto> buscarPorNombre(String nombre);

    List<Producto> buscarPorCategoria(String categoria);

    List<Producto> buscarPorNombreOCategoria(String keyword);

    // Top 10 productos
    List<Producto> top10PorPrecioDesc();

    List<Producto> top10PorPrecioAsc();
}

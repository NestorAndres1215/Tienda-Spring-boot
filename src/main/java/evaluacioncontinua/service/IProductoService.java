package evaluacioncontinua.service;

import java.util.List;

import evaluacioncontinua.entity.Producto;

public interface IProductoService {
    List<Producto> listarProductos();

    Producto crearProducto(Producto producto);

    Producto editarProducto(Long id);

    void eliminarProducto(Long id);
}

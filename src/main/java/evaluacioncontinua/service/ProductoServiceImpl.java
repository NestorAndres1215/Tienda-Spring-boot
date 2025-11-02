package evaluacioncontinua.service;

import java.util.List;


import evaluacioncontinua.exception.ResourceNotFoundException;
import evaluacioncontinua.util.Mensajes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import evaluacioncontinua.dao.IProductoDao;
import evaluacioncontinua.entity.Producto;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements IProductoService {

    private final IProductoDao productoDao;

    @Override
    public List<Producto> listarProductos() {
        return (List<Producto>) productoDao.findAll();
    }

    @Override
    @Transactional
    public Producto crearProducto(Producto producto) {
        return productoDao.save(producto);
    }

    @Override
    @Transactional
    public Producto editarProducto(Long id) {
        return productoDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Mensajes.PRODUCTO_NO_ENCONTRADO + id));
    }

    @Override
    @Transactional
    public void eliminarProducto(Long id) {
        if (!productoDao.existsById(id)) {
            throw new ResourceNotFoundException(Mensajes.PRODUCTO_NO_ENCONTRADO + id);
        }
        productoDao.deleteById(id);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        return productoDao.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Producto> buscarPorCategoria(String categoria) {
        return productoDao.findByCategoriaIgnoreCase(categoria);
    }

    @Override
    public List<Producto> buscarPorNombreOCategoria(String keyword) {
        return productoDao.buscarPorNombreOCategoria(keyword);
    }

    @Override
    public List<Producto> top10PorPrecioDesc() {
        return productoDao.findTop10ByOrderByPrecioDesc();
    }

    @Override
    public List<Producto> top10PorPrecioAsc() {
        return productoDao.findTop10ByOrderByPrecioAsc();
    }

    @Override
    public Producto obtenerPorId(Long id) {
        return productoDao.findById(id)
                .orElseThrow(() -> new RuntimeException(Mensajes.PRODUCTO_NO_ENCONTRADO + id));
    }

}
package evaluacioncontinua.service;


import java.util.List;

import evaluacioncontinua.dao.IClienteDao;
import evaluacioncontinua.entity.Cliente;
import evaluacioncontinua.exception.ResourceNotFoundException;
import evaluacioncontinua.util.Mensajes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements IClienteService {

    private final IClienteDao clienteDao;

    @Override
    public List<Cliente> listarClientes() {
        return clienteDao.findAll();
    }

    @Override
    @Transactional
    public Cliente crearCliente(Cliente cliente) {
        return clienteDao.save(cliente);
    }

    @Override
    @Transactional
    public Cliente editarCliente(Long id) {
        return clienteDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(Mensajes.CLIENTE_NO_ENCONTRADO + id));
    }

    @Override
    @Transactional
    public void eliminarCliente(Long id) {
        if (!clienteDao.existsById(id)) {
            throw new ResourceNotFoundException(Mensajes.CLIENTE_NO_ENCONTRADO + id);
        }
        clienteDao.deleteById(id);
    }

    @Override
    public boolean correoExiste(String correo) {
        return clienteDao.existsByEmail(correo);
    }
    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteDao.findByNombreContainingIgnoreCase(nombre);
    }

    @Override
    public List<Cliente> buscarPorApellido(String apellido) {
        return clienteDao.findByApellidoContainingIgnoreCase(apellido);
    }

    @Override
    public List<Cliente> buscarPorEmail(String email) {
        return clienteDao.findByEmailContainingIgnoreCase(email);
    }

    @Override
    public List<Cliente> buscarPorNombreApellidoEmail(String keyword) {
        return clienteDao.buscarPorNombreApellidoEmail(keyword);
    }
}
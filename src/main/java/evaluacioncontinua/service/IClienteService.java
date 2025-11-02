package evaluacioncontinua.service;

import java.util.List;

import evaluacioncontinua.entity.Cliente;


public interface IClienteService {


    List<Cliente> listarClientes();

    Cliente crearCliente(Cliente cliente);

    Cliente editarCliente(Long id);

    void eliminarCliente(Long id);

    boolean correoExiste(String correo);
}

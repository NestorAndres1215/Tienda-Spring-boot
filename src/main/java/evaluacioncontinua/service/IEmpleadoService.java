package evaluacioncontinua.service;

import java.util.List;

import evaluacioncontinua.entity.Empleado;

public interface IEmpleadoService {
    List<Empleado> listarEmpleados();

    Empleado crearEmpleado(Empleado empleado);

    Empleado editarEmpleado(Long id);

    void eliminarEmpleado(Long id);

    // ✅ Métodos de búsqueda
    List<Empleado> buscarPorNombre(String nombre);

    List<Empleado> buscarPorApellido(String apellido);

    List<Empleado> buscarPorEmail(String email);

    List<Empleado> buscarPorNombreApellidoEmail(String keyword);
}

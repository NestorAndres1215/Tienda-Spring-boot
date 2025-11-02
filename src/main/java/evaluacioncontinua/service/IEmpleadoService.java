package evaluacioncontinua.service;

import java.util.List;

import evaluacioncontinua.entity.Empleado;

public interface IEmpleadoService {
    List<Empleado> listarEmpleados();

    Empleado crearEmpleado(Empleado empleado);

    Empleado editarEmpleado(Long id);

    void eliminarEmpleado(Long id);


}

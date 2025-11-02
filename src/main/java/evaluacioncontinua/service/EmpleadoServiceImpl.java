package evaluacioncontinua.service;

import java.util.List;
import evaluacioncontinua.exception.ResourceNotFoundException;
import evaluacioncontinua.util.Mensajes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import evaluacioncontinua.dao.IEmpleadoDao;
import evaluacioncontinua.entity.Empleado;


@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements IEmpleadoService {

	private final IEmpleadoDao empleadoDao;

	@Override
	public List<Empleado> listarEmpleados() {
		return empleadoDao.findAll();
	}

	@Override
	@Transactional
	public Empleado crearEmpleado(Empleado empleado) {
		return empleadoDao.save(empleado);
	}

	@Override
	@Transactional
	public Empleado editarEmpleado(Long id) {
		return empleadoDao.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException(Mensajes.EMPLEADO_NO_ENCONTRADO + id));
	}

	@Override
	@Transactional
	public void eliminarEmpleado(Long id) {
		if (!empleadoDao.existsById(id)) {
			throw new ResourceNotFoundException(Mensajes.EMPLEADO_NO_ENCONTRADO + id);
		}
		empleadoDao.deleteById(id);
	}

	@Override
	public List<Empleado> buscarPorNombre(String nombre) {
		return empleadoDao.findByNombreContainingIgnoreCase(nombre);
	}

	@Override
	public List<Empleado> buscarPorApellido(String apellido) {
		return empleadoDao.findByApellidoContainingIgnoreCase(apellido);
	}

	@Override
	public List<Empleado> buscarPorEmail(String email) {
		return empleadoDao.findByEmailContainingIgnoreCase(email);
	}

	@Override
	public List<Empleado> buscarPorNombreApellidoEmail(String keyword) {
		return empleadoDao.buscarPorNombreApellidoEmail(keyword);
	}
}
	package hn.clinica.data.entity;

import java.io.IOException;

import hn.clinica.data.service.RepositoryClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class CLINICARepositoryImpl {
	
	private static CLINICARepositoryImpl instance;
	private RepositoryClient client;
	
	private CLINICARepositoryImpl (String url, Long timeout) 
	{
		this.client = new RepositoryClient(url, timeout);

	}
	
	//Implementando Patron SINGLETON
	
	public static CLINICARepositoryImpl getInstance(String url, Long timeout) 
	{
		if(instance == null) 
		{
			synchronized (CLINICARepositoryImpl.class) 
			{
				
				if (instance == null) 
				{
					instance = new CLINICARepositoryImpl(url, timeout);
					
				}
				
				
			}
			
		}
		return instance;
		
	}
	
	
	
	//METODO PARA CONSULTAR PACIENTES DE BASE DE DATOS EN VIEW PACIENTES
	public ResponsePacientes getPacientes() throws IOException{
		
		Call <ResponsePacientes> call = client.getDataService().obtenerPacientes();
		Response<ResponsePacientes> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
		if(response.isSuccessful()) {
			return response.body();
		}
		else 
		{
			return null;
			
		}
	}
	
	
	
	
	//METODO PARA CREACION DE PACIENTE VIEW PACIENTES
	public boolean createPaciente(Pacientes nuevo) throws IOException{
		Call <ResponseBody> call = client.getDataService().crearPaciente(nuevo);
		Response<ResponseBody> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
			return response.isSuccessful();		
	}
	
	//METODO PARA MODIFICAR PACIENTES DE BASE DE DATOS EN VIEW PACIENTES
			public boolean updatePacientes(Pacientes actualizar) throws IOException{	
				Call <ResponseBody> call = client.getDataService().modificarPacientes(actualizar);
				Response<ResponseBody> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
					return response.isSuccessful();
				}
			
			//METODO PARA ELIMINAR PACIENTES DE BASE DE DATOS EN VIEW PACIENTES
			public boolean deletePacientes(String identidad) throws IOException{	
				Call <ResponseBody> call = client.getDataService().eliminarPaciente(identidad);
				Response<ResponseBody> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
					return response.isSuccessful();
				}		

	
	
	
	//METODO PARA CONSULTAR CITA DE BASE DE DATOS EN VIEW CITAS
		public ResponseCitas getCita() throws IOException{
			Call <ResponseCitas> call = client.getDataService().obtenerCitas();
			Response<ResponseCitas> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
			if(response.isSuccessful()) {
				return response.body();
			}
			else 
			{return null;	}
	}
		//METODO PARA CREAR CITA DE BASE DE DATOS EN VIEW CITAS
		public boolean createCita(Citas nuevo) throws IOException{	
			Call <ResponseBody> call = client.getDataService().crearCitas(nuevo);
			Response<ResponseBody> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
				return response.isSuccessful();
			}
		
		//METODO PARA MODIFICAR CITA DE BASE DE DATOS EN VIEW CITAS
		public boolean UpdateCita(Citas actualizar) throws IOException{	
			Call <ResponseBody> call = client.getDataService().actualizarCitas(actualizar);
			Response<ResponseBody> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
				return response.isSuccessful();
			}
		//METODO PARA ELIMINAR CITA DE BASE DE DATOS EN VIEW CITAS
		public boolean DeleteCita(String idcita) throws IOException{	
			Call <ResponseBody> call = client.getDataService().eliminarCitas(idcita);
			Response<ResponseBody> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
				return response.isSuccessful();
			}

		//METODO PARA CONSULTAR MEDICAMENTOS DE BASE DE DATOS EN VIEW CITAS
		public ResponseMedicamento getMedicamento() throws IOException{
			
			Call <ResponseMedicamento> call = client.getDataService().obtenerMedicamentos();
			Response<ResponseMedicamento> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
			if(response.isSuccessful()) {
				return response.body();
			}
			else 
			{
				return null;			
			}
		}
		
		public boolean crearMedicamento(Medicamentos nuevo) throws IOException{
			Call<ResponseBody> call = this.client.getDataService().crearMedicamento(nuevo);
			Response<ResponseBody> response = call.execute();
			return response.isSuccessful();
		}
		
		public boolean borrarMedicamento(String idMed) throws IOException{
			Call<ResponseBody> call = this.client.getDataService().eliminarMedicamento(idMed);
			Response<ResponseBody> response = call.execute();
			return response.isSuccessful();
		}
		
		// METODO PARA CONSULTAR LA TABLA CONSULTAS DE BASE DE DATOS EN VIEW CONSULTAS
		// METODO PARA CONSULTAR LA TABLA CONSULTAS DE BASE DE DATOS EN VIEW CONSULTAS
		public ResponseConsulta getConsulta() throws IOException {
			Call<ResponseConsulta> call = client.getDataService().obtenerConsulta();
			Response<ResponseConsulta> response = call.execute(); // AQUI ES DONDE SE CONSULTA A LA URL DE LA BASE DE DATOS.
			if (response.isSuccessful()) {
				return response.body();
			} else {
				return null;

			}
		}
		public boolean updateConsulta(Consulta actualizada) throws IOException {
			Call<ResponseBody> call = this.client.getDataService().actualizarConsulta(actualizada);
			Response<ResponseBody> response = call.execute();
			return response.isSuccessful();
		}

		

		
}

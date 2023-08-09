package hn.clinica.data.service;

import hn.clinica.data.entity.Citas;
import hn.clinica.data.entity.Medicamentos;
import hn.clinica.data.entity.Pacientes;
import hn.clinica.data.entity.ResponseCitas;
import hn.clinica.data.entity.ResponseConsulta;
import hn.clinica.data.entity.ResponseMedicamento;
import hn.clinica.data.entity.ResponsePacientes;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface CLINICARepository {

	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	
	@GET("/pls/apex/wencellr_pav2_98_2/clinica/pacientes/")
	Call<ResponsePacientes> obtenerPacientes();
	
	
	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	
	@POST("/pls/apex/wencellr_pav2_98_2/clinica/pacientes/")
	Call<ResponseBody> crearPaciente(@Body Pacientes nuevo);

	
	// METODO MODIFIAR PACIENTES
		@Headers({
		    "Content-Type: application/json",
		    "Accept-Charset: utf-8",
		    "User-Agent: Retrofit-Sample-App"
		})
		@PUT("/pls/apex/wencellr_pav2_98_2/clinica/pacientes/")
		Call<ResponseBody> modificarPacientes(@Body Pacientes actualizar );
		
	
	// METODO CONSULTA CITA
	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	@GET("/pls/apex/wencellr_pav2_98_2/clinica/citas/")
	Call<ResponseCitas> obtenerCitas();
	
	// METODO CREAR CITA
	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	@POST("/pls/apex/wencellr_pav2_98_2/clinica/citas/")
	Call<ResponseBody> crearCitas(@Body Citas nuevo );
	
	// METODO MODIFIAR CITA
	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	@PUT("/pls/apex/wencellr_pav2_98_2/clinica/citas/")
	Call<ResponseBody> actualizarCitas(@Body Citas actualizar );
	
	// METODO ELMINAR CITA
	@Headers({
		    "Accept-Charset: utf-8",
		    "User-Agent: Retrofit-Sample-App"
		})
	 @DELETE("/pls/apex/wencellr_pav2_98_2/clinica/citas/")
	 Call<ResponseBody> eliminarCitas(@Query("id") String idcita);
	
	
	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	
	@GET("pls/apex/wencellr_pav2_98_2/clinica/medicamentos")
	Call<ResponseMedicamento> obtenerMedicamentos();
	
	
	@Headers({
		"Content-Type: application/json",
		"Accept-Charset: utf-8",
		"User-Agent: Retrofit-Sample-App"
	})
	@POST("pls/apex/wencellr_pav2_98_2/clinica/medicamentos")
	Call<ResponseBody> crearMedicamento(@Body Medicamentos medicamento);
	
	
	@Headers({
		"Accept-Charset: utf-8",
		"User-Agent: Retrofit-Sample-App"
	})
	@DELETE("pls/apex/wencellr_pav2_98_2/clinica/medicamentos")
	Call<ResponseBody> eliminarMedicamento(@Query("codigo") String idMed);	
	
	@Headers({
	    "Content-Type: application/json",
	    "Accept-Charset: utf-8",
	    "User-Agent: Retrofit-Sample-App"
	})
	
	@GET("pls/apex/wencellr_pav2_98_2/clinica/Consulta/")
	Call<ResponseConsulta> obtenerConsulta();

	
	
		
}

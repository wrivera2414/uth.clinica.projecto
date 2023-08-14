package hn.clinica.data.entity;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

	public class PacientesDataReport implements JRDataSource {
		
		private List<Pacientes> pacientes;
		private int counter = -1;
		private int maxCounter = 0;

		public void setData(List<Pacientes> pacientes) {
			this.pacientes = pacientes;
			this.maxCounter = this.pacientes.size() -1;
		}



		public List<Pacientes> getPacientes() {
			return pacientes;
		}



		public void setPacientes(List<Pacientes> pacientes) {
			this.pacientes = pacientes;
		}



		public int getCounter() {
			return counter;
		}



		public void setCounter(int counter) {
			this.counter = counter;
		}



		public int getMaxCounter() {
			return maxCounter;
		}



		public void setMaxCounter(int maxCounter) {
			this.maxCounter = maxCounter;
		}
		
		@Override
		public boolean next() throws JRException {
			if(counter < maxCounter) {
				counter++;
				return true;
			}
			return false;
		}



		@Override
		public Object getFieldValue(JRField jrField) throws JRException {
			if("NOMBRE".equals(jrField.getName())) {
				return pacientes.get(counter).getNombre();
			}else if("IDENTIDAD".equals(jrField.getName())) {
				return pacientes.get(counter).getIdentidad();
			}else if("TELEFONO".equals(jrField.getName())) {
				return pacientes.get(counter).getTelefono();
			}else if("EDAD".equals(jrField.getName())) {
				return pacientes.get(counter).getEdad();
			}else if("SANGRE".equals(jrField.getName())) {
				return pacientes.get(counter).getSangre();
			}else if("PESO".equals(jrField.getName())) {
				return pacientes.get(counter).getPeso();
			}else if("ALTURA".equals(jrField.getName())) {
				return pacientes.get(counter).getAltura();
			}
			return "";
		}



		
		
}

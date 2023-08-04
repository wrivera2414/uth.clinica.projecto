package hn.clinica.data.entity;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

	public class CitasDataReport implements JRDataSource {
		
		private List<Citas> cita;
		private int counter = -1;
		private int maxCounter = 0;

		public void setData(List<Citas> cita) {
			this.cita = cita;
			this.maxCounter = this.cita.size() -1;
		}

		public List<Citas> getCita() {
			return cita;
		}

		public void setCita(List<Citas> cita) {
			this.cita = cita;
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
			if("IDCITA".equals(jrField.getName())) {
				return cita.get(counter).getIdcita();
			}else if("FECHA".equals(jrField.getName())) {
				return cita.get(counter).getFecha();
			}else if("IDENTIDAD".equals(jrField.getName())) {
				return cita.get(counter).getIdentidad();
			}else if("PACIENTE".equals(jrField.getName())) {
				return cita.get(counter).getPaciente();
			}else if("DIRECCION".equals(jrField.getName())) {
				return cita.get(counter).getDireccion();
			}else if("TELEFONO".equals(jrField.getName())) {
				return cita.get(counter).getTelefono();
			}else if("DETALLE".equals(jrField.getName())) {
				return cita.get(counter).getDetalle();
			}
			return "";
		}
		
}

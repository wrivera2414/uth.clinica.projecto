package hn.clinica.data.entity;

import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRException;

public class ConsultaReporte implements JRDataSource{
    
		
		private List<Consulta> consultas;
		private int counter = -1;
		private int maxCounter = 0;

		public void setData(List<Consulta> consultas) {
			this.consultas = consultas;
			this.maxCounter = this.consultas.size() -1;
		}

		public List<Consulta> getConsulta() {
			return consultas;
		}

		public void setConsuta(List<Consulta> consultas) {
			this.consultas = consultas;
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
            if("IDENTIDAD".equals(jrField.getName())) {
                return consultas.get(counter).getIdentidad().toString();
            }else if("PACIENTE".equals(jrField.getName())) {
                return consultas.get(counter).getNombre().toString();
            }else if("TELEFONO".equals(jrField.getName())) {
                return consultas.get(counter).getTelefono().toString();
            }else if("MEDICAMENTO".equals(jrField.getName())) {
                return consultas.get(counter).getMedicamento().toString();
            }else if("STOCK".equals(jrField.getName())) {
                return consultas.get(counter).getStocks().toString();
            }
            return "";
        }

}

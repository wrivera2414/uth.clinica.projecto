package hn.clinica.data.entity;

import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRException;

public class MedicamentosReporte implements JRDataSource{

	private List<Medicamentos> medicamentos;
	private int counter = -1;
	private int maxCounter = 0;

	public void setData(List<Medicamentos> medicame) {
		this.medicamentos = medicame;
		this.maxCounter = this.medicamentos.size() -1;
	}

	public List<Medicamentos> getMedicamentos() {
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamentos> medicamentos) {
		this.medicamentos = medicamentos;
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
        if("CODIGO".equals(jrField.getName())) {
            return medicamentos.get(counter).getCodigo().toString();
        }else if("NOMBRE".equals(jrField.getName())) {
            return medicamentos.get(counter).getNombre().toString();
        }else if("FABRICANTE".equals(jrField.getName())) {
            return medicamentos.get(counter).getFabricante().toString();
        }else if("ACTIVO".equals(jrField.getName())) {
            return medicamentos.get(counter).getPrincipioa().toString();
        }else if("FECHAVEN".equals(jrField.getName())) {
            return medicamentos.get(counter).getFechav().toString();
        }else if("STOCK".equals(jrField.getName())) {
            return medicamentos.get(counter).getStock().toString();
        }
        return "";
    }

	
}

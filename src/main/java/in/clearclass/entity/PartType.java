package in.clearclass.entity;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PartType {
	@JsonProperty("CatalogName")
	private String name;
	@JsonProperty("CaseSize")
	private String size;
	@JsonProperty("Voltage")
	private String volt;
	@JsonProperty("Dielectric")
	private String diel;
	@JsonProperty("Capacitance")
	private String cap;
	@JsonProperty("Tolerance")
	private String tol;
	@JsonProperty("Termination")
	private String term;
		
	@Override
	public String toString() {
		return name + " " + size + "  " + diel + "  " + volt + "  " + cap + " ± " + tol + "  " + term;
	}
	
	public String toSql() {
		String val = getVal();
		String dim = getDim();
		return "INSERT INTO avx VALUES('" 
				+ name + "', '" 
				+ size + "', '" 
				+ diel + "', " 
				+ val  + ", '"  
				+ dim  + "', "
				+ tol  + ", " 
				+ volt + ");";
	}
	
	private String getVal(){
		String[] vd = cap.split(" ");
		Double v = Double.parseDouble(vd[0]);
		String d = vd[1];
		
		if(d.equals("a"))
			return String.format(new Double(v/1000).toString());
		if(d.equals("p") || d.equals("μ"))
			return vd[0];
		if(d.equals("n"))
			return (v<100)? String.valueOf(new Double(v*1000).intValue()) : 
				String.format(new Double(v/1000).toString());
		throw new RuntimeException("Unknown dim symbol: " + d);
	}
	
	private String getDim(){
		String[] vd = cap.split(" ");
		Double v = Double.parseDouble(vd[0]);
		String d = vd[1];
		
		if(d.equals("a") || d.equals("p"))
			return "pF";
		if(d.equals("μ"))
			return "uF";
		if(d.equals("n"))
			return (v<100)? "pF" : "uF";
		throw new RuntimeException("Unknown dim symbol: " + d);
	}
	
	// проверка кодового имени на соответствие фактическим параметрам
	public boolean isCorrect(){
		String dim = getDim();
		Double val = Double.parseDouble(getVal());

		if(dim.equals("pF")) // переводим емкость в pF
			val = val*1;
		else if(dim.equals("uF"))
			val = val*1000*1000;
		else throw new RuntimeException("Unknown dim symbol: " + dim);

		String valCode;
		if(val>=0.5 && val<=9.9)
			valCode = String.format(val.toString()).replace(".", "R"); // пример: 9.1 = 9R1
		else { // представление емкости "2 знака + порядок"
			String valSt = String.valueOf(val.intValue());
			valCode = valSt.substring(0, 2) + valSt.substring(2).length(); //
		}
		
		HashMap<Double, Character> voltCodes = new HashMap<>();
		voltCodes.put(4d,   '4');
		voltCodes.put(6.3,  '6');
		voltCodes.put(10d,  'Z');
		voltCodes.put(16d,  'Y');
		voltCodes.put(25d,  '3');
		voltCodes.put(35d,  'D');
		voltCodes.put(50d,  '5');
		voltCodes.put(100d, '1');
		voltCodes.put(200d, '2');
		voltCodes.put(500d, '7');
		
		HashMap<String, Character> dielCodes = new HashMap<>();
		dielCodes.put("NP0", 'A');
		dielCodes.put("C0G", 'A');
		dielCodes.put("X7R", 'C');
		dielCodes.put("X5R", 'D');
		dielCodes.put("X8R", 'F');
		dielCodes.put("Y5V", 'G');
		dielCodes.put("U",   'U');
		dielCodes.put("X6S", 'W');
		dielCodes.put("X7S", 'Z');
		dielCodes.put("X8L", 'L');
				
		HashMap<Double, Character> tolCodes = new HashMap<>();
		tolCodes.put(0.1,   'B');
		tolCodes.put(0.25,  'C');
		tolCodes.put(0.5,   'D');
		tolCodes.put(1d,    'F');
		tolCodes.put(2d,    'G');
		tolCodes.put(5d,    'J');
		tolCodes.put(10d,   'K');
		tolCodes.put(20d,   'M');
		tolCodes.put(30d,   'N');
		tolCodes.put(8020d, 'Z');
		
		String originName = size + voltCodes.get(Double.parseDouble(volt)) + 
				dielCodes.get(diel) + valCode + tolCodes.get(Double.parseDouble(tol)) + "AT2A";
		
		if(name.equals(originName))
			return true;
		
		return false;
	}
}
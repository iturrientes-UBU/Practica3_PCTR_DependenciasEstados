package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	private int contadorPersonasTotales;
	private int maximoPersonasTotales;
	private Hashtable<String, Integer> contadoresPersonasPuerta;
	
	
	public Parque(int maximoPersonas) {	// TODO
		maximoPersonasTotales = maximoPersonas;
		contadorPersonasTotales = 0;
		contadoresPersonasPuerta = new Hashtable<String, Integer>();
		// TODO
	}


	@Override
	public synchronized void entrarAlParque(String puerta){		
		try {
			comprobarAntesDeEntrar();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Si no hay entradas por esa puerta, inicializamos
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		// Aumentamos el contador total y el individual
		contadorPersonasTotales++;		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)+1);
		
		// Imprimimos el estado del parque
		imprimirInfo(puerta, "Entrada");
		checkInvariante();
		
		
	}
	
	
	@Override
	public void salirDelParque(String puerta) {
		try {
			comprobarAntesDeSalir();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		if (contadoresPersonasPuerta.get(puerta) == null){
			throw new NullPointerException();
        
		}
		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		
		imprimirInfo(puerta,"Salida");
		
		//notify();
		
	}
	
	private void imprimirInfo (String puerta, String movimiento){
		System.out.println(movimiento + " por puerta " + puerta);
		System.out.println("--> Personas en el parque " + contadorPersonasTotales); //+ " tiempo medio de estancia: "  + tmedio);
		
		// Iteramos por todas las puertas e imprimimos sus entradas
		for(String p: contadoresPersonasPuerta.keySet()){
			System.out.println("----> Por puerta " + p + " " + contadoresPersonasPuerta.get(p));
		}
		System.out.println(" ");
	}
	
	private int sumarContadoresPuerta() {
		int sumaContadoresPuerta = 0;
			Enumeration<Integer> iterPuertas = contadoresPersonasPuerta.elements();
			while (iterPuertas.hasMoreElements()) {
				sumaContadoresPuerta += iterPuertas.nextElement();
			}
		return sumaContadoresPuerta;
	}
	
	protected void checkInvariante() {
		assert sumarContadoresPuerta() == contadorPersonasTotales : "INV: La suma de contadores de las puertas debe ser igual al valor del contador del parte";
		// TODO 
		// TODO
	}

	protected void comprobarAntesDeEntrar() throws InterruptedException{	
		while(contadorPersonasTotales==0) wait();
		notify();
	}

	protected void comprobarAntesDeSalir() throws InterruptedException{		
		while(contadorPersonasTotales!=0) wait();
		notify();
	}




}

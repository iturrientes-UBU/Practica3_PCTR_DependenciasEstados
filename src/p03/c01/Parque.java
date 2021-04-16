package src.p03.c01;

import java.util.Enumeration;
import java.util.Hashtable;

public class Parque implements IParque{

	private int contadorPersonasTotales;
	private int maximoPersonasTotales;
	private static int MAX = 20;
	private static int MIN = 0;
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
		notifyAll();
		
		
	}
	
	
	@Override
	public synchronized void salirDelParque(String puerta) {
		try {
			comprobarAntesDeSalir();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		if (contadoresPersonasPuerta.get(puerta) == null){
			contadoresPersonasPuerta.put(puerta, 0);
		}
		
		/*if (contadoresPersonasPuerta.get(puerta) == null){
			Thread.interrupted();
			throw new NullPointerException();
        
		}*/
		
		contadoresPersonasPuerta.put(puerta, contadoresPersonasPuerta.get(puerta)-1);
		contadorPersonasTotales--;	
		imprimirInfo(puerta,"Salida");
		
		notifyAll();
		
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
		assert contadorPersonasTotales >= MIN : "INV: El n�mero de personas totales tiene que ser superior a 0";
		assert  contadorPersonasTotales <= MAX : "INV: El n�mero de personas totales tiene que ser inferior a 20";
	}

	protected synchronized void comprobarAntesDeEntrar() throws InterruptedException{	
		while(contadorPersonasTotales==maximoPersonasTotales) wait();
		
	}

	protected synchronized void comprobarAntesDeSalir() throws InterruptedException{		
		while(contadorPersonasTotales==0) wait();
		
	}




}

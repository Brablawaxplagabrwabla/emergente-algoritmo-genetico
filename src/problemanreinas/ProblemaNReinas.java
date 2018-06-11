/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemanreinas;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author gtroncone
 * Implementación de un algoritmo genético con estrategia de cruce OX (Order 
 * Crossover).
 * 
 * La representación de un individuo en el espacio de soluciones se hace con un
 * vector de N enteros, siendo N el orden del problema. Cada entero del vector
 * representa la columna que ocupa la reina en la fila iésima, puesto que no 
 * puede haber más de una reina por fila.
 * 
 * Para que un individuo se considere válido, su representación no debe contener
 * números repetidos, pues esto implicaría que hay dos reinas en la misma colum-
 * na.
 */
public class ProblemaNReinas {
    
    private int grado;
    private int tamañoPoblacion;
    private double tasaMutacion;
    private double probabilidadApareamiento;
    private double evaluacionOnline;
    private double evaluacionOffline;
    private int epocas;
    
    private final int MINIMO_POR_TORNEO;
    private final int MAXIMO_POR_TORNEO;
    private final int APAREOS_POR_GENERACION;
    private final int NUMERO_SHUFFLES;
    private final int LIMITE_CICLOS;
    private int TIPO_CRUCE;
    
    private ArrayList<Individuo> poblacion;
    
    public ProblemaNReinas(int minTorneo, int maxTorneo, int descGen, int shuffles, int ciclos) {
        Scanner in = new Scanner(System.in);
        do {
            System.out.print("Indique el número de filas/columnas del tablero. (Entre 8 y 100): ");
            grado = in.nextInt();
        } while (!(grado > 7 && grado <= 100));
        do {
            System.out.print("Indique el tamaño mínimo de la población. (Entre 10 y 500): ");
            tamañoPoblacion = in.nextInt();
        } while (!(tamañoPoblacion >= 10 && tamañoPoblacion <= 500));
        do {
            System.out.print("Indique la tasa de mutación. (Entre 0 y 1): ");
            tasaMutacion = in.nextDouble();
        } while (!(tasaMutacion >= 0 && tasaMutacion <= 1));
        do {
            System.out.print("Indique la probabilidad de apareamiento. (Entre 0 y 1): ");
            probabilidadApareamiento = in.nextDouble();
        } while (!(probabilidadApareamiento >=0 && probabilidadApareamiento <= 1));
        do {
            System.out.print("Indique el tipo de cruce, 0 para OX, 1 para MIX: ");
            TIPO_CRUCE = in.nextInt();
        } while (TIPO_CRUCE != 0 && TIPO_CRUCE != 1);
        if (minTorneo <= maxTorneo && maxTorneo <= tamañoPoblacion) {
            MINIMO_POR_TORNEO = minTorneo;
            MAXIMO_POR_TORNEO = maxTorneo;   
        } else {
            MINIMO_POR_TORNEO = tamañoPoblacion;
            MAXIMO_POR_TORNEO = tamañoPoblacion;
        }
        if (descGen < maxTorneo && descGen > 0) {
            APAREOS_POR_GENERACION = descGen;   
        } else {
            APAREOS_POR_GENERACION = minTorneo;
        }
        if (shuffles > grado) {
            NUMERO_SHUFFLES = shuffles;
        } else {
            NUMERO_SHUFFLES = grado;
        }
        if (ciclos > 1000) {
            LIMITE_CICLOS = ciclos;
        } else {
            LIMITE_CICLOS = 1000;
        }
        
        poblacion = new ArrayList<>();
        
        generarPoblacion();
    }
    
    private void generarPoblacion() {
        for (int i = 0; i < tamañoPoblacion; i++) {
            poblacion.add(new Individuo(grado, NUMERO_SHUFFLES));
            poblacion.get(i).calcularFitness();
        }
    }
    
    public void imprimirPoblacion() {
        System.out.println("\nLa población final obtenida es: ");
        for (int i = 0; i < poblacion.size(); i++) {
            imprimirIndividuo(poblacion.get(i));
        }
    }
    
    public void imprimirIndividuo(Individuo jose) {
        int[] genes = jose.getGenes();
        int[][] tablero = new int[grado][grado];
        for (int i = 0; i < genes.length; i++) {
            tablero[i][genes[i]] = 1;
        }
        for (int i = 0; i < tablero.length; i++) {
            System.out.print("|");
            for (int j = 0; j < tablero[0].length; j++) {
                if (tablero[i][j] == 1) {
                    System.out.print("Q|");
                } else {
                    System.out.print(" |");
                }
            }
            System.out.println("");
        }
        System.out.println("Fitness = " + jose.getFitness() + "\n");
    }
    
    public void ordenarPorFitness() {
        ArrayList<Individuo> lista = new ArrayList<>();
        for (int i = 0; i < poblacion.size(); i++) {
            int aux = 0;
            if (poblacion.get(i).getFitness() > aux) {
                aux = poblacion.get(i).getFitness();
            }
            int indice = i;
            for (int j = 0; j < poblacion.size(); j++) {
                if (poblacion.get(j).getFitness() > aux) {
                    aux = poblacion.get(j).getFitness();
                    indice = j;
                }
            }
            lista.add(poblacion.get(indice));
            poblacion.remove(indice);
            i--;
        }
        poblacion = lista;
    }
    
    public void algoritmoGenetico() {
        epocas = 0;
        ordenarPorFitness();
        do {
            epocas++;
            reproduccion();
            ordenarPorFitness();
            seleccionArtificial();
            for (int i = 0; i < poblacion.size(); i++) {
                evaluacionOnline += poblacion.get(i).getFitness();
            }
            evaluacionOffline += poblacion.get(0).getFitness();
            if (epocas > LIMITE_CICLOS) {
                System.out.println("No se encontró ninguna solución dentro del límite de épocas establecido.");
                return;
            }
        } while (poblacion.get(0).getFitness() < grado); // Se puede cambiar aquí el criterio de stop
        evaluacionOnline /= (tamañoPoblacion * epocas);
        evaluacionOffline /= epocas;
    }
    
    public void seleccionArtificial() {
        while (poblacion.size() > tamañoPoblacion) {
            poblacion.remove(poblacion.size() - 1);
        }
    }
    
    public void reproduccion() {
        int numTorneo = (int) Math.floor(Math.random() * (MAXIMO_POR_TORNEO - MINIMO_POR_TORNEO)) + MINIMO_POR_TORNEO;
        Individuo[] torneo = new Individuo[numTorneo];
        for (int i = 0; i < torneo.length; i++) {
            torneo[i] = poblacion.get(i);
        }
        for (int i = 0; i < APAREOS_POR_GENERACION; i++) {
            if (Math.floor(Math.random()) < probabilidadApareamiento) {
                int indicePadreA = (int) Math.floor(Math.random() * torneo.length);
                int indicePadreB = 0;
                do {
                    indicePadreB = (int) Math.floor(Math.random() * torneo.length);
                } while (indicePadreA == indicePadreB);
                Individuo padreA = torneo[indicePadreA];
                Individuo padreB = torneo[indicePadreB];
                Individuo hijo;
                if (TIPO_CRUCE == 0) {
                    hijo = generarCombinacionOX(padreA, padreB);   
                } else {
                    hijo = generarCombinacionMIX(padreA, padreB);
                }
                if (Math.floor(Math.random()) < tasaMutacion) {
                    hijo = mutarIndividuo(hijo);
                }
                poblacion.add(hijo);
            }
        }
    }
    
    public Individuo mutarIndividuo(Individuo jose) {
        int[] cromosomaJose = jose.getGenes();
        int[] cromosomaMutado = new int[cromosomaJose.length];
        for (int i = 0; i < cromosomaJose.length; i++) {
            cromosomaMutado[i] = cromosomaJose[i];
        }
        int indiceMutacionA = (int) Math.floor(Math.random() * cromosomaJose.length);
        int indiceMutacionB = 0;
        do {
            indiceMutacionB = (int) Math.floor(Math.random() * cromosomaJose.length);
        } while (indiceMutacionA == indiceMutacionB);
        int aux = cromosomaMutado[indiceMutacionA];
        cromosomaMutado[indiceMutacionA] = cromosomaMutado[indiceMutacionB];
        cromosomaMutado[indiceMutacionB] = aux;
        Individuo hijoMutado = new Individuo(cromosomaMutado);
        hijoMutado.calcularFitness();
        double lanzamientoMoneda = Math.random();
        if (lanzamientoMoneda <= 0.5) {
            hijoMutado = mutarIndividuo(hijoMutado);
        }
        return hijoMutado;
    }
    
    public Individuo generarCombinacionOX(Individuo padreA, Individuo padreB) {
        int[] cromosomaA = padreA.getGenes();
        int[] cromosomaB = padreB.getGenes();
        int[] cromosomaHijo = new int[cromosomaA.length];
        int aleloA1 = (int) Math.floor(Math.random() * cromosomaA.length);
        int aleloA2 = (int) Math.floor(Math.random() * (cromosomaA.length - aleloA1)) + aleloA1;
        for (int i = aleloA1; i <= aleloA2; i++) {
            cromosomaHijo[i] = cromosomaA[i];
        }
        int index = 0;
        for (int i = 0; i < cromosomaB.length; i++) {
            if (index == aleloA1) {
                if (aleloA2 == cromosomaA.length - 1) {
                    break;
                } else {
                    index = aleloA2 + 1;
                }
            }
            boolean agregar = true;
            for (int j = aleloA1; j <= aleloA2; j++) {
                if (cromosomaB[i] == cromosomaHijo[j]) {
                    agregar = false;
                }
            }
            if (agregar) {
                cromosomaHijo[index] = cromosomaB[i];
                index++;
            }
        }
        Individuo hijo = new Individuo(cromosomaHijo);
        hijo.calcularFitness();
        return hijo;
    }
    
    public Individuo generarCombinacionMIX(Individuo padreA, Individuo padreB) {
        int[] cromosomaA = padreA.getGenes();
        int[] cromosomaB = padreB.getGenes();
        int[] cromosomaHijo = new int[cromosomaA.length];
        for (int i = 0; i < cromosomaHijo.length; i++) {
            cromosomaHijo[i] = -1;
        }
        ArrayList<Integer> posicionesImpares = new ArrayList<>();
        int random = (int) Math.floor(Math.random() * 2) + 1;
        for (int i = 0; i < cromosomaA.length; i++) {
            if (cromosomaA[i] % 2 == 0) {
                cromosomaHijo[i] = cromosomaA[i];
            }
            if (cromosomaB[i] % 2 == 1) {
                posicionesImpares.add(i);
            }
        }
        for (int i = 0; i < cromosomaHijo.length; i++) {
            if (cromosomaHijo[i] == -1) {
                cromosomaHijo[i] = cromosomaB[posicionesImpares.get(0)];
                posicionesImpares.remove(0);
            }
        }
        Individuo hijo = new Individuo(cromosomaHijo);
        hijo.calcularFitness();
        return hijo;
    }
    
    public void imprimirEvaluaciones() {
        System.out.println("La evaluación online es: " + evaluacionOnline);
        System.out.println("La evaluación offline es: " + evaluacionOffline);
        System.out.println("\nEl número de épocas requeridos para alcanzar la solución es: " + epocas);
    }
    
    /**
    * @param args the command line arguments
    */
    
    public static void main(String[] args) {
        ProblemaNReinas resolver = new ProblemaNReinas(10, 30, 15, 10000, 10000);
        resolver.algoritmoGenetico();
        resolver.imprimirPoblacion();
        resolver.imprimirEvaluaciones();
    }   
}

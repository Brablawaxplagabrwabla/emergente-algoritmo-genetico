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

    /**
     * @param args the command line arguments
     */
    
    private int grado;
    private int tamañoMinPoblacion;
    private double tasaMutacion;
    private double probabilidadApareamiento;
    
    private final int MINIMO_POR_TORNEO;
    private final int MAXIMO_POR_TORNEO;
    private final int DESCENDIENTES_POR_GENERACION;
    private final int NUMERO_SHUFFLES;
    public final int LIMITE_CICLOS;
    
    private ArrayList<Individuo> poblacion;
    
    public ProblemaNReinas(int minTorneo, int maxTorneo, int descGen, int shuffles, int ciclos) {
        Scanner in = new Scanner(System.in);
        do {
            System.out.print("Indique el número de filas/columnas del tablero. (Entre 8 y 1000): ");
            grado = in.nextInt();
        } while (!(grado > 7 && grado <= 1000));
        do {
            System.out.print("Indique el tamaño mínimo de la población. (Entre 100 y 10000): ");
            tamañoMinPoblacion = in.nextInt();
        } while (!(tamañoMinPoblacion >= 100 && tamañoMinPoblacion <= 10000));
        do {
            System.out.print("Indique la tasa de mutación. (Entre 0 y 1): ");
            tasaMutacion = in.nextDouble();
        } while (!(tasaMutacion >= 0 && tasaMutacion <= 1));
        do {
            System.out.print("Indique la probabilidad de apareamiento. (Entre 0 y 1): ");
            probabilidadApareamiento = in.nextDouble();
        } while (!(probabilidadApareamiento >=0 && probabilidadApareamiento <= 1));
        
        MINIMO_POR_TORNEO = minTorneo;
        MAXIMO_POR_TORNEO = maxTorneo;
        if (descGen < maxTorneo && descGen > 0) {
            DESCENDIENTES_POR_GENERACION = descGen;   
        } else {
            DESCENDIENTES_POR_GENERACION = minTorneo;
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
        for (int i = 0; i < tamañoMinPoblacion; i++) {
            poblacion.add(new Individuo(grado, NUMERO_SHUFFLES));
            poblacion.get(i).calcularFitness();
        }
    }
    
    public void imprimirPoblacion() {
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
        long epocas = 0;
        do {
            epocas++;
        } while (poblacion.get(0).getFitness() < grado && epocas < LIMITE_CICLOS);
        ordenarPorFitness();
        System.out.println("El número de épocas requeridos para alcanzar la solución es: " + epocas);
    }
    
    public static void main(String[] args) {
        ProblemaNReinas resolver = new ProblemaNReinas(10, 30, 15, 10000, 10000);
        resolver.algoritmoGenetico();
        resolver.imprimirPoblacion();
    }   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problemanreinas;

/**
 *
 * @author gtroncone
 */
public class Individuo {
    
    private int[] cromosoma;
    private int fitness;
    
    public Individuo(int[] genes) {
        cromosoma = genes;
        fitness = 0;
    }
    
    public Individuo(int n, int shuffles) {
        cromosoma = new int[n];
        for (int i = 0; i < n; i++) {
            cromosoma[i] = i;
        }
        
        for (int i = 0; i < shuffles; i++) {
            int index1 = (int) Math.floor(Math.random() * n);
            int index2 = (int) Math.floor(Math.random() * n);
            
            int aux1 = cromosoma[index1];
            int aux2 = cromosoma[index2];
            
            cromosoma[index1] = aux2;
            cromosoma[index2] = aux1;
        }        
    }
    
    public void calcularFitness() {
        int[] reinasVivasV = new int[cromosoma.length];
        int reinasVivas = cromosoma.length;
        for (int i = 0; i < cromosoma.length; i++) {
            reinasVivasV[i] = 1;
        }
        for (int i = 0; i < cromosoma.length; i++) {
            for (int aux = 1; i - aux > 0; aux++) { // Buscamos en las diagonales superiores;
                if (cromosoma[i - aux] == cromosoma[i] - aux || // Superior izquierda
                        cromosoma[i - aux] == cromosoma[i] + aux) { // Superior derecha
                    if (reinasVivasV[i - aux] == 1) {
                        reinasVivasV[i - aux] = 0;
                        reinasVivas--;
                        if (reinasVivasV[i] == 1) {
                            reinasVivasV[i] = 0;
                            reinasVivas--;
                        }
                    }
                }
            }
            for (int aux = 1; i + aux < cromosoma.length; aux++) { // Buscamos en las diagonales inferiores
                if (cromosoma[i + aux] == cromosoma[i] - aux || // Inferior izquierda
                        cromosoma[i + aux] == cromosoma[i] + aux) { // Inferior derecha
                    if (reinasVivasV[i + aux] == 1) {
                        reinasVivasV[i + aux] = 0;
                        reinasVivas--;
                        if (reinasVivasV[i] == 1) {
                            reinasVivasV[i] = 0;
                            reinasVivas--;
                        }
                    }
                }
            }
        }
        fitness = reinasVivas;
    }

    public int[] getGenes() {
        return cromosoma;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}

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
    
    private int[] genes;
    private int fitness;
    
    public Individuo(int n, int shuffles) {
        genes = new int[n];
        for (int i = 0; i < n; i++) {
            genes[i] = i;
        }
        
        for (int i = 0; i < shuffles; i++) {
            int index1 = (int) Math.floor(Math.random() * n);
            int index2 = (int) Math.floor(Math.random() * n);
            
            int aux1 = genes[index1];
            int aux2 = genes[index2];
            
            genes[index1] = aux2;
            genes[index2] = aux1;
        }        
    }
    
    public void calcularFitness() {
        int[] reinasVivasV = new int[genes.length];
        int reinasVivas = genes.length;
        for (int i = 0; i < genes.length; i++) {
            reinasVivasV[i] = 1;
        }
        for (int i = 0; i < genes.length; i++) {
            for (int aux = 1; i - aux > 0; aux++) { // Buscamos en las diagonales superiores;
                if (genes[i - aux] == genes[i] - aux || // Superior izquierda
                        genes[i - aux] == genes[i] + aux) { // Superior derecha
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
            for (int aux = 1; i + aux < genes.length; aux++) { // Buscamos en las diagonales inferiores
                if (genes[i + aux] == genes[i] - aux || // Inferior izquierda
                        genes[i + aux] == genes[i] + aux) { // Inferior derecha
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
        return genes;
    }

    public int getFitness() {
        return fitness;
    }

    public void setFitness(int fitness) {
        this.fitness = fitness;
    }
}

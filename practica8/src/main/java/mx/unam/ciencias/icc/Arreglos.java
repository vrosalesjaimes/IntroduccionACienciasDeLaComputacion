package mx.unam.ciencias.icc;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i = 0; i < arreglo.length; i++){
	    int m = i;
	    for(int j = i+1; j < arreglo.length; j++){
		if(comparador.compare(arreglo[j], arreglo[m]) < 0){
		    m = j;
		}
	    }
	    intercambia(arreglo, i, m);
	}
    }

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando QuickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        QuickSort(arreglo, comparador, 0,  arreglo.length-1);
    }

    private static <T> void QuickSort(T[] arreglo,Comparator<T> comparador, int a, int b){
	if(b <= a) return;
	int i = a + 1;
	int j = b;
	while(i < j){
	    if(comparador.compare(arreglo[i], arreglo[a]) > 0 && comparador.compare(arreglo[j], arreglo[a]) <= 0 ){
		intercambia(arreglo, i++, j--);
	    }
	    else if(comparador.compare(arreglo[i], arreglo[a]) <= 0)
		i++;
	    else
		j--;
	}
	if(comparador.compare(arreglo[i], arreglo[a]) > 0)
	    i--;
	intercambia(arreglo, i, a);
	QuickSort(arreglo,comparador, a, i-1);
	QuickSort(arreglo,comparador, i+1, b);
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        int indice = 0;
	int longitud = arreglo.length;
	while(indice < longitud){
	    if(comparador.compare(arreglo[indice],elemento) == 0) return indice;
	    indice++;
	}
	return -1;
    }

    private static <T> void intercambia(T[] array,int index_1, int index_2){
	T elmt_1 = array[index_1];
	T elmt_2 = array[index_2];
	array[index_1] = elmt_2;
	array[index_2] = elmt_1;
    }
}

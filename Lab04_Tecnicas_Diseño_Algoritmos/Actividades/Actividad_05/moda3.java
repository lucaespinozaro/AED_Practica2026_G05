public static int moda3(int[] array, int prim, int ult) {
 
    SetVectors homogeneo   = new SetVectors();
    SetVectors heterogeneo = new SetVectors();
 
    // Al inicio todo el arreglo es heterogéneo
    heterogeneo.insertar(new Limits(array, prim, ult));
 
    // Seguimos dividiendo mientras el trozo heterogéneo más largo
    // sea mayor que el trozo homogéneo más largo
    while (heterogeneo.longMayor() > homogeneo.longMayor()) {
 
        Limits p = heterogeneo.mayor();
 
        int mediana = p.array[(p.first + p.last) / 2];
 
        int[] izq = new int[1];
        int[] der = new int[1];
        pivote2(p.array, mediana, p.first, p.last, izq, der);
 
        Limits p1 = new Limits(p.array, p.first,  izq[0] - 1); 
        Limits p2 = new Limits(p.array, izq[0],   der[0] - 1); 
        Limits p3 = new Limits(p.array, der[0],   p.last);     
 
        if (p1.first <= p1.last && p1.length() > 1)
            heterogeneo.insertar(p1);
 
        if (p3.first <= p3.last && p3.length() > 1)
            heterogeneo.insertar(p3);
 
        if (p2.first <= p2.last)
            homogeneo.insertar(p2);
    }
 
    // Si no hubo ningún homogéneo, todos los elementos son distintos
    // y la moda es cualquier elemento (devolvemos el primero)
    if (homogeneo.esVacio()) {
        return array[prim];
    }
 
    Limits resultado = homogeneo.mayor();
    int moda = resultado.array[resultado.first];
 
    homogeneo.destruir();
    heterogeneo.destruir();
 
    return moda;
}

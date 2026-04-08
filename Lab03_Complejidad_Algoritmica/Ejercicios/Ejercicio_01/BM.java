FUNCIÓN BM (v : VECTOR(ENTERO), n : ENTERO) : ENTERO
    m := v[0]                    // O(1)
    PARA i DESDE 1 HASTA n-1     // O(n)
        SI v[i] > m                 // O(1)
            m := v[i]                   // O(1)
        FINSI                       // O(1)
    FINPARA                      // O(1)
    DEVOLVER m                   // O(1)
FINFUNCIÓN

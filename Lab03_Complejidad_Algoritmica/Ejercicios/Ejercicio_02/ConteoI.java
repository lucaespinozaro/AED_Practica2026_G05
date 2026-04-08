FUNCIÓN ConteoI (v : VECTOR(ENTERO), n : ENTERO) : ENTERO
    conteo := 0                         // O(1)
    PARA i DESDE 0 HASTA n-2            // O(n) -> O(n) * O(n) = O(n^2)
        PARA j DESDE i+1 HASTA n-1          // O(n)
            SI v[i] = v[j]                      // O(1)
                conteo := conteo + 1                // O(1)
            FINSI                               // O(1)
        FINPARA                             // O(1)
    FINPARA                             // O(1)
    DEVOLVER conteo                     // O(1)
FINFUNCIÓN

====================================================
  CONTROL DE ACCESO DE EMPLEADOS — Tablas Hash
  Estructuras de Datos · 3er Año Ing. Sistemas
====================================================

CONCEPTO:
  Sistema de control de acceso de una empresa donde cada empleado
  tiene una tarjeta de identificación con su DNI. El DNI se usa como
  CLAVE de una tabla Hash para verificar accesos en tiempo O(1)
  en el caso promedio, exactamente como funcionan los sistemas reales
  de control de acceso biométrico o por tarjeta RFID.

ARCHIVOS:
  ListLinked.java       → Lista enlazada genérica (usada por HashO)
  Register.java          → Par (clave, valor) almacenado en las tablas
  Empleado.java          → Entidad del dominio (DNI, nombre, área, turno, estado)
  HashO.java             → Tabla Hash ABIERTA: colisiones por encadenamiento
  HashC.java             → Tabla Hash CERRADA: colisiones por sondeo lineal/cuadrático
  HashOPanel.java        → Visualización gráfica de HashO (cadenas)
  HashCPanel.java        → Visualización gráfica de HashC (grilla + animación de sondeo)
  ControlAccesoApp.java  → Ventana principal + main()

COMPILAR Y EJECUTAR:
  cd src/
  javac *.java
  java ControlAccesoApp

REQUISITOS:
  JDK 8 o superior. Sin librerías externas.

FUNCIONALIDADES:
  ✅ Registrar empleado → se inserta SIMULTÁNEAMENTE en ambas tablas (HashO y HashC)
  ✅ Eliminar empleado → se elimina de ambas tablas
  ✅ Buscar por DNI ("verificar acceso") → muestra si la tarjeta está activa/desactivada
  ✅ Pestaña Hash Abierto: visualiza las cadenas de colisión por slot
  ✅ Pestaña Hash Cerrado: visualiza la grilla y ANIMA el sondeo paso a paso
  ✅ Selector de estrategia: Sondeo Lineal vs Sondeo Cuadrático
  ✅ Activar / Desactivar tarjeta (simula bloqueo de acceso sin eliminar el registro)
  ✅ Cambiar tamaño inicial de las tablas y ver cómo cambia la distribución
  ✅ Factor de carga visible en tiempo real (rehash automático si supera 0.75)
  ✅ 7 empleados de ejemplo precargados

POR QUÉ HASHING AQUÍ:
  Los sistemas reales de control de acceso (tarjetas RFID, lectores
  biométricos, sistemas de marcación) necesitan verificar el DNI/ID de
  un empleado en tiempo casi constante, sin importar cuántos empleados
  tenga la empresa. Una tabla Hash logra esto mapeando cada DNI a una
  posición de memoria mediante una función hash.

DIFERENCIA ENTRE LAS DOS TABLAS:
  HashO (Hash Abierto / Encadenamiento):
    Cada posición de la tabla es una lista enlazada. Si dos DNIs
    generan el mismo índice, ambos coexisten en la misma cadena.

  HashC (Hash Cerrado / Direccionamiento Abierto):
    Cada posición almacena como máximo UN registro. Si hay colisión,
    se "sondea" la siguiente posición disponible:
      - Sondeo Lineal:    índice, índice+1, índice+2, ...
      - Sondeo Cuadrático: índice, índice+1², índice+2², índice+3², ...

LEYENDA DE COLORES:
  Azul        → Slot ocupado
  Gris        → Slot vacío
  Rojo oscuro → Slot eliminado (marca de "tumba", solo en HashC)
  Naranja     → Posición visitada durante el sondeo (animación)
  Amarillo    → Posición final encontrada / insertada
  Texto rojo  → Tarjeta de empleado desactivada
====================================================

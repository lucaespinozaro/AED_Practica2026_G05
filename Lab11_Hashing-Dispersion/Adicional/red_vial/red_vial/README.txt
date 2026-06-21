====================================================
  RED VIAL INTERACTIVA — Grafos con Dijkstra/BFS/DFS
  Estructuras de Datos · 3er Año Ing. Sistemas
====================================================

ESTRUCTURA DEL PROYECTO:
  src/
  ├── RedVialApp.java         ← Ventana principal + main()
  ├── graph/
  │   ├── Vertex.java         ← Vértice genérico
  │   ├── Edge.java           ← Arista ponderada
  │   ├── AdjList.java        ← Lista de adyacencia
  │   ├── GraphLink.java      ← Grafo no dirigido ponderado
  │   └── GraphPanel.java     ← Visualización gráfica (Swing)
  └── listlinked/
      ├── Node.java
      ├── ListLinked.java     ← Lista enlazada genérica
      └── QueueLink.java      ← Cola enlazada (usada en BFS)

COMPILAR Y EJECUTAR:
  cd src/
  javac -sourcepath . RedVialApp.java
  java RedVialApp

REQUISITOS:
  JDK 8 o superior. Sin librerías externas.

FUNCIONALIDADES:
  ✅ Agregar / eliminar ciudades (vértices)
  ✅ Agregar / eliminar carreteras con distancia en km (aristas ponderadas)
  ✅ Dijkstra: ruta más corta entre dos ciudades — animado paso a paso
  ✅ BFS: recorrido en anchura desde una ciudad — animado con orden de visita
  ✅ DFS: recorrido en profundidad desde una ciudad — animado con orden de visita
  ✅ Verificar si el grafo es conexo
  ✅ Arrastrar ciudades para reorganizar el mapa visual
  ✅ Demo precargado con ciudades del sur del Perú
  ✅ Tabla de carreteras registradas
  ✅ Leyenda de colores

LEYENDA DE COLORES:
  Azul oscuro  → Ciudad normal
  Verde        → Ciudad de origen
  Rojo         → Ciudad de destino
  Amarillo     → Ciudad en la ruta más corta / visitada en BFS/DFS
  Línea dorada → Carretera en la ruta Dijkstra
  Línea verde  → Carretera recorrida en BFS/DFS
====================================================

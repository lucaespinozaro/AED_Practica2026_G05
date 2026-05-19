package Adicional;

import java.util.List;

public class Controlador {
    private AVLTree<Contacto> arbol;

    public Controlador(AVLTree<Contacto> arbol) {
        this.arbol = arbol;
    }

    public AVLTree<Contacto> getArbol() { return arbol; }

    public boolean insertar(Contacto c) {
        try {
            arbol.insert(c);
            return true;
        } catch (ItemDuplicated e) {
            return false;
        }
    }

    public boolean eliminar(String nombre) {
        Contacto clave = new Contacto(nombre, "", "", "");
        Contacto encontrado = arbol.buscar(clave);
        if (encontrado == null) return false;
        arbol.remove(encontrado);
        return true;
    }

    public Contacto buscar(String nombre) {
        return arbol.buscar(new Contacto(nombre, "", "", ""));
    }

    public List<String> buscarConPasos(String nombre) {
        return arbol.buscarConPasos(new Contacto(nombre, "", "", ""));
    }

    public boolean marcarFavorito(String nombre) {
        Contacto c = buscar(nombre);
        if (c == null) return false;
        c.setFavorito(!c.isFavorito());
        return true;
    }
}
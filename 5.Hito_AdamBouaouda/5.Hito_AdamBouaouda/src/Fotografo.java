public class Fotografo {
    private int id;
    private String nombre;
    private boolean premiado;

    public Fotografo(int id, String nombre, boolean premiado) {
        this.id = id;
        this.nombre = nombre;
        this.premiado = premiado;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isPremiado() {
        return premiado;
    }

    public String toString() {
        return nombre;
    }
}
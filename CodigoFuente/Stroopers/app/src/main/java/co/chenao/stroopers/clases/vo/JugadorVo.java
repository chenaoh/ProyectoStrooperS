package co.chenao.stroopers.clases.vo;

public class JugadorVo {

    private int id;
    private String nombre;
    private String genero;
    private int avatar;

    public JugadorVo(int id, String nombre, String genero, int avatar) {
        this.id = id;
        this.nombre = nombre;
        this.genero = genero;
        this.avatar = avatar;
    }

    public JugadorVo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}

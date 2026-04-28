class Cancion
{
  private String titulo;
  private String artista;
  private int duracionSeg;

  public Cancion(String titulo, String artista, int duracionSeg)
  {
    this.titulo = titulo;
    this.artista = artista;
    this.duracionSeg = duracionSeg;
  }

  public String getTitulo() { return titulo; }
  public String getArtista() { return artista; }
  public int getDuracionSeg() { return duracionSeg; }

  @Override
  public String toString()
  {
    return titulo + " - " + artista + " (" + duracionSeg + "s)";
  }
}

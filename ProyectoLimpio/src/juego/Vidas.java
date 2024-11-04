package juego;


	import java.awt.Image;
	import entorno.Entorno;
	import entorno.Herramientas;

	public class Vidas {
		double x, y, escala, alto, ancho;
		
		Image sprite;
		

		public Vidas(double x, double y) {
			this.x = x;
			this.y = y;						
			sprite= Herramientas.cargarImagen("vida.png");				
			escala = 2;
			alto = sprite.getHeight(null) * escala;
			ancho = sprite.getWidth(null) * escala;
			
		}
		
		public void mostrar(Entorno e) {			
			e.dibujarImagen(sprite, x, y, 0, escala);

	}
}
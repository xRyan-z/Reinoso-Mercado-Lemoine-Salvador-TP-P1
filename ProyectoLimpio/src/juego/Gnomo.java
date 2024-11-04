package juego;

import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;
import entorno.Entorno;

public class Gnomo {
    private double x, y;
    private double velocidadX = 1.2;
    private double velocidadY = 4;
    private boolean cayendo = true;
    private Random random;
    private Isla islaActual;
    private int direccion; // 1 para derecha, -1 para izquierda
    private double ancho;
    private double alto;
    
    // Imágenes del gnomo
    private Image imagenDerecha;
    private Image imagenIzquierda;

    public Gnomo(double x, double y) {
        this.x = x;
        this.y = y;
        this.random = new Random();
        this.direccion = random.nextBoolean() ? 1 : -1;
        this.alto = 20;
        this.ancho = 10;
        
        // Cargar las imágenes del gnomo
        this.imagenDerecha = new ImageIcon("imagenes/gnomoder.png").getImage();
        this.imagenIzquierda = new ImageIcon("imagenes/gnomoizq.png").getImage();
    }

    public void moverse(Isla[] islas) {
        if (cayendo) {
            y += velocidadY;

            // Verifica si el gnomo ha colisionado con alguna isla
            for (Isla isla : islas) {
                if (isla != null && isla.colisionaConGnomo(x, y, 20, 20)) {
                    cayendo = false;
                    velocidadY = 0;
                    y = isla.getY() - isla.getAlto() / 2 - 10;
                    islaActual = isla;
                    cambiarDireccionAleatoria();
                    break;
                }
            }
        } else {
            x += velocidadX * direccion;

            if (islaActual != null) {
                double limiteIzquierdo = islaActual.getX() - islaActual.getAncho() / 1.63;
                double limiteDerecho = islaActual.getX() + islaActual.getAncho() / 1.63;

                if (x < limiteIzquierdo || x > limiteDerecho) {
                    iniciarCaida();
                }
            }
        }
    }

    private void iniciarCaida() {
        cayendo = true;
        velocidadY = 4;
        islaActual = null;
    }

    private void cambiarDireccionAleatoria() {
        direccion = random.nextBoolean() ? 1 : -1;
    }
    
    public void respawn(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getAlto() {
        return this.alto;
    }

    public double getAncho() {
        return this.ancho;
    }
    
    public void dibujarse(Entorno entorno) {
        // Selecciona la imagen correcta según la dirección del gnomo
        Image imagenActual = (direccion == 1) ? imagenDerecha : imagenIzquierda;
        
        // Dibuja la imagen del gnomo en lugar de un rectángulo
        entorno.dibujarImagen(imagenActual, this.x, this.y, 0);
    }
}

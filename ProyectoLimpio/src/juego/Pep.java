package juego;

import java.awt.Image;
import javax.swing.ImageIcon;
import entorno.Entorno;

public class Pep {
    private double x, y;
    private double velocidadX; 
    private double velocidadY;
    private boolean saltando;
    public int cantVidas;
    private static final double GRAVEDAD = 0.2;
    private static final double VELOCIDAD_SALTO = -90;

    // Imágenes de Pep
    private Image imagenQuietoDerecha;
    private Image imagenQuietoIzquierda;
    private Image imagenIzquierda;
    private Image imagenDerecha;
    private Image imagenSaltoIzquierda;
    private Image imagenSaltoDerecha;

    // Para recordar la última dirección en la que se movió
    private boolean mirandoDerecha;
    private boolean mirandoIzquierda;

    // Posición de reinicio
    private static final double POSICION_INICIAL_X = 400; // Cambia a la posición deseada
    private static final double POSICION_INICIAL_Y = 350; // Cambia a la posición deseada
    private static final double ALTURA_VENTANA = 600; // Cambia esto a la altura real de tu venta

    public Pep(double x, double y) {
        this.x = x;
        this.y = y;
        this.velocidadX = 4;  
        this.velocidadY = 1;   
        cantVidas = 3;
        this.saltando = false;

        // Cargar imágenes
        this.imagenQuietoDerecha = new ImageIcon("imagenes/pepnormalder.png").getImage(); 
        this.imagenQuietoIzquierda = new ImageIcon("imagenes/pepnormal.png").getImage(); 
        this.imagenIzquierda = new ImageIcon("imagenes/pepizq.png").getImage();
        this.imagenDerecha = new ImageIcon("imagenes/pepder.png").getImage();
        this.imagenSaltoIzquierda = new ImageIcon("imagenes/pepsaltandoder.png").getImage();
        this.imagenSaltoDerecha = new ImageIcon("imagenes/pepsaltandoizq.png").getImage();

        this.mirandoDerecha = true;
        this.mirandoIzquierda = false;
    }
    public Disparo disparar() {
        int direccionDisparo = mirandoDerecha ? -1 : 1; // 1 para derecha, -1 para izquierda
        return new Disparo(this.x, this.y, direccionDisparo);
    }

    public void moverse(Entorno entorno, Isla[] islas) {
        this.velocidadX = 0; 

        if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
            this.velocidadX = -4; 
            this.mirandoIzquierda = true; 
            this.mirandoDerecha = false;
        }
        if (entorno.estaPresionada(entorno.TECLA_DERECHA)) {
            this.velocidadX = 4; 
            this.mirandoDerecha = true; 
            this.mirandoIzquierda = false;
        }

        this.x += this.velocidadX;

        boolean enIsla = false;
        for (Isla isla : islas) {
            if (isla != null && this.estaSobreIsla(isla)) {
                enIsla = true;
                break;
            }
        }

        if (entorno.sePresiono(entorno.TECLA_ARRIBA) && enIsla) {
            this.saltando = true;
            this.velocidadY = VELOCIDAD_SALTO;
        }

        if (!enIsla || saltando) {
            this.y += this.velocidadY;
            this.velocidadY += GRAVEDAD;  // Aumenta la velocidad de caída por gravedad
        } else {
            // Si está en una isla, detén la caída
            this.velocidadY = 0;
            this.saltando = false;  // Resetea el estado de salto
        }

        if (enIsla && this.saltando) {
            this.saltando = false; 
            this.velocidadY = 0; 
        }

        // Verificar si el personaje ha caído fuera de los límites de la pantalla
        if (this.y > ALTURA_VENTANA) {
            reiniciar();
        }
    }

    private void reiniciar() {
        this.x = POSICION_INICIAL_X;
        this.y = POSICION_INICIAL_Y;
        this.velocidadY = 0; // Resetear velocidad vertical al reiniciar
        this.saltando = false; // Reiniciar el estado de salto
    }

    public void dibujarse(Entorno entorno) {
        Image imagenActual;

        if (saltando) {
            if (this.velocidadX > 0) {
                imagenActual = imagenSaltoDerecha;
            } else {
                imagenActual = imagenSaltoIzquierda;
            }
        } else if (this.velocidadX > 0) {
            imagenActual = imagenDerecha;
        } else if (this.velocidadX < 0) {
            imagenActual = imagenIzquierda;
        } else {
            if (mirandoDerecha) {
                imagenActual = imagenQuietoDerecha;
            } else if (mirandoIzquierda) {
                imagenActual = imagenQuietoIzquierda;
            } else {
                imagenActual = imagenQuietoDerecha; 
            }
        }

        entorno.dibujarImagen(imagenActual, this.x, this.y, 0);
    }

    private boolean estaSobreIsla(Isla isla) {
        return this.x >= isla.getX() - isla.ancho / 2 && this.x <= isla.getX() + isla.ancho / 2 &&
               this.y + 30 >= isla.getY() - isla.alto / 2 && this.y + 30 <= isla.getY() + isla.alto / 2;
    }
    
    public boolean pepColisionaConGnomo(double gnomoX, double gnomoY, double gnomoAncho, double gnomoAlto) {
        // Verifica si Pep y el Gnomo están en la misma zona (o casi) para considerarlo una "colisión"
        return gnomoY + gnomoAlto / 2 >= this.y - 25 && gnomoY - gnomoAlto / 2 <= this.y + 25 &&
               gnomoX + gnomoAncho / 2 >= this.x - 15 && gnomoX - gnomoAncho / 2 <= this.x + 15;
    }
    public boolean pepColisionaConTortuga(double tortugaX, double tortugaY, double tortugaAncho, double tortugaAlto) {
        // Verifica si Pep y el Gnomo están en la misma zona (o casi) para considerarlo una "colisión"
        return tortugaY + tortugaAlto / 2 >= this.y - 25 && tortugaY - tortugaAlto / 2 <= this.y + 25 &&
               tortugaX + tortugaAncho / 2 >= this.x - 25 && tortugaX - tortugaAncho / 2 <= this.x + 15;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}

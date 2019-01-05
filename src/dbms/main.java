package dbms;


import GUI.GUI;
import GUI.MantenimientoGUI;
import conexion.Servidor;
import java.awt.EventQueue;


/**
 *
 * @author eddycastro
 */
public class main {
    private static int PuertoServidor = 4545;
    
    /**
     * Imprime el modo de uso del programa en la consola.
     */
    private static void imprimirModoUso(){
        System.out.println("Modo de uso: java -jar DBMS [Parámetros]");
        System.out.println("\tSin parámetros \t- Modo cliente directo.");
        System.out.println("\t-s [PUERTO] \t- Modo servidor utilizando el puerto dado (o el " + PuertoServidor + " si no se especifica).");
    }
    
    /**
     * Punto de entrada del programa.
     * @param args 
     */
    public static void main(String[] args) {
        try {
            if (args.length == 0) { // Forma básica del programa sin ningún argumento.

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        GUI ventana = new GUI();
                        ventana.setVisible(true);
                    }
                });
            } else if (args[0].equals("-s")) {  // Forma de servidor: -s [PUERTO]
                // Lee el puerto
                if (args.length > 1) {
                    PuertoServidor = Integer.parseInt(args[1]);
                }
                Servidor servidor = new Servidor(PuertoServidor);
                servidor.iniciar();
            } else if (args[0].equals("-C")){
                // Abrir el cliente
                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            MantenimientoGUI ventanaMantenimiento = new MantenimientoGUI();
                            ventanaMantenimiento.setVisible(true);
                        } catch( Exception ex ) {
                            System.out.println(ex.getMessage());
                        }
                    }
                });
            }
            else { // ERROR
                imprimirModoUso();
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
            imprimirModoUso();
        }
    }
}

package conection;

import interfazUsuario.ImpresorServidor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor básico de respuestas para queries.
 * @author Jorge
 */
public class Server {
    private final int puerto;
    
    private Thread thConsola;
    private Thread thClientes;

    /**
     * Constructor con puerto.
     * @param puerto 
     */
    public Server(int puerto) {
        this.puerto = puerto;
    }
    
    /**
     * Inicia el servidor.
     */
    public void init() throws IOException{
        // Thread para manejo de comandos directos al servidor
        thConsola = new Thread(){
            //<editor-fold defaultstate="collapsed" desc="Manejo consola">
            @Override
            public void run() {
                try {
                    BufferedReader stdIn = new BufferedReader( new InputStreamReader(System.in) );
                    while( !isInterrupted() ){
                        if( stdIn.ready() ){
                            String comando = stdIn.readLine();
                            manejarComandos(comando);
                        }
                    }
                } catch (IOException ex) {
                    System.out.println("ERROR: " + ex.getMessage());
                }
            }
            //</editor-fold>
        };
        thConsola.start();
        
        
        // Thread para manejo de clientes
        thClientes = new Thread(){
            //<editor-fold defaultstate="collapsed" desc="Manejo de clientes">
            @Override
            public void run() {
                try{
                    ServerSocket socketServidor = new ServerSocket(puerto);
                    socketServidor.setSoTimeout(1000);
                    
                    // Verifica la conexión de clientes
                    while( !isInterrupted() ){
                        try{
                            try (
                            Socket cliente = socketServidor.accept();
                            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter(cliente.getOutputStream()) );
                            BufferedReader br = new BufferedReader( new InputStreamReader( cliente.getInputStream()) );
                            ImpresorServidor impresor = new ImpresorServidor(bw) ) {
                                
                                // Maneja al cliente actual
                                ManejadorCliente manejador = new ManejadorCliente(br, bw);
                                manejador.manejarCliente();
                                bw.flush();
                            }
                            
                        }catch( InterruptedIOException ex ){}
                    }
                }catch(Exception ex){
                    System.out.println("ERROR EN SERVIDOR: " + ex.getMessage());
                }
                
                cerrarServidor();
            }
            //</editor-fold>
        };
        thClientes.start();
    }
    
    /**
     * Cierra todos los recursos utilizados por el servidor.
     */
    synchronized private void cerrarServidor(){
        thConsola.interrupt();
        thClientes.interrupt();
    }
    
    
    /**
     * Maneja un comando directo del servidor.
     * @param comando Comando a ejecutar.
     * @return true si el servidor debe de seguir ejecutándose
     */
    synchronized
    private void manejarComandos( String comando ){
        String comandoUpper = comando.toUpperCase();
        
        if( comandoUpper.equals("CERRAR") ){
            System.out.println("<Cerrando el servidor>");
            cerrarServidor();
        } else {
            System.out.println("ERROR: No se reconoció el comando: " + comando);
        }
        
    }
}

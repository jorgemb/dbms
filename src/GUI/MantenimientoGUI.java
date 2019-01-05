package GUI;


import JSON.Mensaje;
import JSON.Tabla;
import com.google.gson.Gson;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author Jorge
 */
public class MantenimientoGUI extends JFrame{
    // Componentes
    private JButton btnInsertar;
    private JButton btnActualizar;
    private JButton btnEliminar;
   
    private JButton btnAgregarColumna; 
    
    private JTable tblTabla;   
    
    // Diálogos
    private JDialog dialogoColumna;
    private JDialog dialogoInsertar;
    private JDialog dialogoActualizar;    

    ArrayList<JTextField> textos;
    Tabla tabla;
    
    /**
     * Constructor.
     * @throws HeadlessException 
     */
    public MantenimientoGUI() throws HeadlessException, Exception {
        super("Mantenimiento BD");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize( 1200, 500 );
        
        inicializarComponentes();
        this.pack();
    }
    
    /**
     * Inicializa todos los componentes.
     */
    private void inicializarComponentes() throws Exception{
        
        // Panes de botones y resultados
        JPanel pnlPrincipal = new JPanel( new GridBagLayout() );
        //JPanel pnlResultados = new JPanel( new GridBagLayout() );
            
        // Botón de Insertar
        GBC layout = new GBC(1,2,1,1).setFill(GBC.HORIZONTAL);
        btnInsertar = new JButton("Insertar");
        btnInsertar.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    insertarDato();
                } catch (Exception ex) {
                    //TODO Manejar excepción
                }
            }
            
        });
        pnlPrincipal.add(btnInsertar, layout);
        
        // Botón de Actualizar
        layout = new GBC(1,3,1,1).setFill(GBC.HORIZONTAL);
        btnActualizar = new JButton("Actualizar");
        btnActualizar.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    
                    if (tblTabla.getSelectedRow() != -1){
                        Object indice = tblTabla.getValueAt(tblTabla.getSelectedRow(), 0);
                        actualizarDato((int)indice, tblTabla.getSelectedRow());
 
                    }
                    
                } catch (Exception ex) {
                    //TODO Manejar excepción
                }
            }
            
        });
        pnlPrincipal.add(btnActualizar, layout);
        
        // Botón de Eliminar
        layout = new GBC(1,4,1,1).setFill(GBC.HORIZONTAL);
        btnEliminar = new JButton("Eliminar");
        btnEliminar.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    eliminarDato();
                } catch (Exception ex) {
                    //TODO Manejar excepción
                }
            }
            
        });
        pnlPrincipal.add(btnEliminar, layout);
        
        // Botón de Agregar Columna
        layout = new GBC(0,0,1,1).setAnchor(GBC.WEST);
        btnAgregarColumna = new JButton("Agregar Columna");
        btnAgregarColumna.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                agregarColumna();
            }
            
        });
        pnlPrincipal.add(btnAgregarColumna, layout);
        
        // Cargar los datos a la tabla
        String jsonResultado = conexion("SELECT * FROM cliente;");
        Gson gson = new Gson();
        tabla = gson.fromJson(jsonResultado, Tabla.class);
        
        Object[][] datosTabla = convertirModelo( tabla.obtenerDatos(), tabla.obtenerEsquema().ObtenerTipos() );
        DefaultTableModel modeloTabla = new DefaultTableModel(datosTabla, tabla.obtenerEsquema().ObtenerNombres());
        
        // Tabla de resultados
        layout = new GBC(0,1,1,6).setWeight(1.0, 1.0).setFill(GBC.BOTH).setInsets(10);
        tblTabla = new JTable(modeloTabla);
        
        // Agregar la tabla al pane
        JScrollPane panelResultados = new JScrollPane(tblTabla);
        tblTabla.setFillsViewportHeight(true);
        
        // Quitar el espacio en blanco que se coloca abajo de la tabla
        Dimension dimension = tblTabla.getPreferredSize();
        tblTabla.setPreferredScrollableViewportSize(dimension);
        
        pnlPrincipal.add(panelResultados, layout);
        
        // Actualización de datos por medio de f5
        pnlPrincipal.addKeyListener(new KeyAdapter() {
        @Override
        public void keyPressed(KeyEvent ke) {
            if( ke.getKeyCode() == KeyEvent.VK_F5 )
                try {
                    System.out.println("asdfasd");
                    actualizarDatos();
                    System.out.println("oasdf");
            } catch (Exception ex) {
                //TODO debería manejar la excepcion                
            }
        }
            
        });
        
        // Agrega el panel de botones
        this.setLayout( new GridBagLayout() );
        this.add(pnlPrincipal, new GBC(0, 0).setFill(GBC.BOTH).setWeight(1.0, 1.0).setAnchor(GBC.CENTER) );
        
        // Agregar el panel de resultados
        //this.add(pnlResultados, BorderLayout.WEST );

    }

/**
     * Método para insertar datos en la tabla
     * @throws Exception 
     */
    private void insertarDato() throws Exception{  

        // Arreglo que contiene los componentes
        textos = new ArrayList<>();
        
        String jsonResultado = conexion("SELECT * FROM cliente;");
        
        Gson gson = new Gson();
        tabla = gson.fromJson(jsonResultado, Tabla.class);
        
        // Diálogo
        dialogoInsertar = new JDialog();
        dialogoInsertar.setLayout( new GridBagLayout() );
        
        for (int i = 0; i < tabla.obtenerEsquema().ObtenerNombres().length; i++){
            
            // Agregar los componentes
            JLabel lblNombre = new JLabel(tabla.obtenerEsquema().ObtenerNombres()[i]+": ");
            GBC layout = new GBC(0,i,6,1).setAnchor(GBC.WEST);
            dialogoInsertar.add(lblNombre, layout);
            final JTextField txtDato = new JTextField();
            layout = new GBC(7,i,6,1).setAnchor(GBC.WEST);
            txtDato.setColumns(30);
            dialogoInsertar.add(txtDato, layout);
            textos.add(txtDato);
            
        }
        btnInsertar = new JButton("Insertar fila");
        GBC layout = new GBC(0,tabla.obtenerEsquema().ObtenerNombres().length,6,1).setAnchor(GBC.WEST);
        dialogoInsertar.add(btnInsertar, layout);
        
        // Mostrar diálogo
        dialogoInsertar.setVisible(true);
        dialogoInsertar.pack();
        
        btnInsertar.addActionListener( new ActionListener(){

            String[] esquema = tabla.obtenerEsquema().ObtenerTipos();
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                String valores = "";
                for (int i = 0; i<textos.size(); i++){
                    
                    System.out.println(esquema[i]);
                    
                    if (i < textos.size()-1){
                        if ((esquema[i].equals("INT")) || (esquema[i].equals("FLOAT")) ){
                            valores += String.format("%s,", textos.get(i).getText());
                        } else{
                            valores += String.format("'%s',", textos.get(i).getText());
                        }
                        
                    } else{
                        if ((esquema[i].equals("INT")) || (esquema[i].equals("FLOAT")) ){
                            valores += String.format("%s", textos.get(i).getText());
                        } else{
                            valores += String.format("'%s'", textos.get(i).getText());
                        }
                    }
                    
                }
                
                String insert = String.format("INSERT INTO cliente VALUES (%s);", valores);
                //System.out.println(insert);
                try {
                    conexion(insert);
                    actualizarDatos();
                } catch (Exception ex) {
                    //TODO Manejar la excepcion
                }
                
                dialogoInsertar.setVisible(false);

            }
            
        });
        
    }
    
    /**
     * Método utilizado para actualizar datos
     * @param idActualizar Id de la fila a actualizar
     * @param filaSeleccionada Índice de la fila seleccionada
     * @throws Exception 
     */
    private void actualizarDato(final int idActualizar, int filaSeleccionada) throws Exception{
        
        // Arreglo que contiene los componentes
        textos = new ArrayList<>();
        
        String jsonResultado = conexion("SELECT * FROM cliente;");
        
        Gson gson = new Gson();
        tabla = gson.fromJson(jsonResultado, Tabla.class);
        
        // Diálogo
        dialogoActualizar = new JDialog();
        dialogoActualizar.setLayout( new GridBagLayout() );
        
        for (int i = 0; i < tabla.obtenerEsquema().ObtenerNombres().length; i++){
            
            // Agregar los componentes
            JLabel lblNombre = new JLabel(tabla.obtenerEsquema().ObtenerNombres()[i]+": ");
            GBC layout = new GBC(0,i,6,1).setAnchor(GBC.WEST);
            dialogoActualizar.add(lblNombre, layout);
            final JTextField txtDato = new JTextField();
            layout = new GBC(7,i,6,1).setAnchor(GBC.WEST);
            txtDato.setColumns(30);
            dialogoActualizar.add(txtDato, layout);
            textos.add(txtDato);
            
        }
        btnActualizar = new JButton("Actualizar fila");
        GBC layout = new GBC(0,tabla.obtenerEsquema().ObtenerNombres().length,6,1).setAnchor(GBC.WEST);
        dialogoActualizar.add(btnActualizar, layout);
        
        // Cargar valores en los textos
        int columna = 0;
        for (JTextField Jtext : textos){
            //System.out.println(this.tblTabla.getValueAt(filaSeleccionada, columna).toString());
            Jtext.setText(this.tblTabla.getValueAt(filaSeleccionada, columna).toString());
            columna++;
        }
        
        
        // Mostrar diálogo
        dialogoActualizar.setVisible(true);
        dialogoActualizar.pack();
        
        btnActualizar.addActionListener( new ActionListener(){

            String[] tipos = tabla.obtenerEsquema().ObtenerTipos();
            String[] nombres = tabla.obtenerEsquema().ObtenerNombres();
            
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                String valores = "";
                for (int i = 0; i<textos.size(); i++){
                    
                    //System.out.println(tipos[i]);
                    
                    //System.out.println(nombres[i]);
                    String[] nombre = nombres[i].split("\\.");

                    
                    if (i < textos.size()-1){
                        if ((tipos[i].equals("INT")) || (tipos[i].equals("FLOAT")) ){
                            valores += String.format("%s = %s,", nombre[1], textos.get(i).getText());
                        } else{
                            valores += String.format("%s = '%s',", nombre[1], textos.get(i).getText());
                        }
                        
                    } else{
                        if ((tipos[i].equals("INT")) || (tipos[i].equals("FLOAT")) ){
                            valores += String.format("%s = %s", nombre[1], textos.get(i).getText());
                        } else{
                            valores += String.format("%s = '%s'", nombre[1], textos.get(i).getText());
                        }
                    }
                    
                }
                
                String update = String.format("UPDATE cliente SET %s WHERE Id = %s;", valores, idActualizar);
                //System.out.println(update);
                try {
                    conexion(update);
                    actualizarDatos();
                } catch (Exception ex) {
                    //TODO Manejar la excepcion
                }
                
                dialogoActualizar.setVisible(false);

            }
            
        });
        
    }
    
    /**
     * Método que elimina la fila seleccionada
     * Solicita confirmación
     * @throws Exception 
     */
    private void eliminarDato() throws Exception{
        
        if (this.tblTabla.getSelectedRow() != -1){
        Object indice = this.tblTabla.getValueAt(this.tblTabla.getSelectedRow(), 0);
        
            if (obtenerConfirmacion(String.format("¿Desea eliminar el registro con índice %s?", indice))){

                String delete = String.format("DELETE FROM cliente WHERE Id = %s;", indice);
                //System.out.println(delete);
                conexion(delete);
                actualizarDatos();

            }
        }
        
    }

    /**
     * Método que agrega una nueva columna a la tabla
     */
    private void agregarColumna(){
        
        // Tipos de columna
        ArrayList<String> tiposColumna = new ArrayList<>(Arrays.asList("INT", "FLOAT", "DATE", "CHAR"));
        
        // Componentes del diálogo
        final JTextField txtNombreColumna = new JTextField();
        final JComboBox cmbTipoColumna = new JComboBox(tiposColumna.toArray());
        JLabel lblNombreColumna = new JLabel("Nombre de columna: ");
        JLabel lblTipoColumna = new JLabel("Tipo de columna: ");
        JButton btnAgregar = new JButton("Agregar columna");
        
        
        
        // Diálogo
        dialogoColumna = new JDialog();
        dialogoColumna.setLayout( new GridBagLayout() );
        
        // Agregar los componentes
        GBC layout = new GBC(0,1,6,1).setAnchor(GBC.WEST);
        dialogoColumna.add(lblNombreColumna, layout);
        layout = new GBC(0,2,6,1).setAnchor(GBC.WEST);
        txtNombreColumna.setColumns(30);
        dialogoColumna.add(txtNombreColumna, layout);
        layout = new GBC(0,3,6,1).setAnchor(GBC.WEST);
        dialogoColumna.add(lblTipoColumna, layout);
        layout = new GBC(0,4,6,1).setAnchor(GBC.WEST);
        dialogoColumna.add(cmbTipoColumna, layout);
        layout = new GBC(0,5,6,1).setAnchor(GBC.WEST);
        dialogoColumna.add(btnAgregar, layout);
        
        // Mostrar diálogo
        dialogoColumna.setVisible(true);
        dialogoColumna.pack();
        
        
        btnAgregar.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                
                String alterTable = String.format("ALTER TABLE cliente ADD COLUMN %s %s", txtNombreColumna.getText(), cmbTipoColumna.getSelectedItem().toString());
                try {
                    conexion(alterTable);
                    actualizarDatos();
                } catch (Exception ex) {
                    //TODO Manejar la excepcion
                }
                
                dialogoColumna.setVisible(false);

            }
            
        });
        
    }
    
    /**
     * Método que actualiza los datos de la tabla
     * @throws Exception 
     */
    private void actualizarDatos() throws Exception{
        
        String jsonResultado = conexion("SELECT * FROM cliente;");
        
        Gson gson = new Gson();
        tabla = gson.fromJson(jsonResultado, Tabla.class);
        
        Object[][] datosTabla = convertirModelo(tabla.obtenerDatos(), tabla.obtenerEsquema().ObtenerTipos());
        DefaultTableModel modeloTabla = new DefaultTableModel(datosTabla, tabla.obtenerEsquema().ObtenerNombres());
        this.tblTabla.setModel(modeloTabla);
        
        
//        System.out.println(tabla.esError());
//        System.out.println(tabla.obtenerTipo());
//        System.out.println(tabla.obtenerDatos());
        
    }
    
    /**
     * Convierte el modelo para seguir el tipos dado.
     * @param inicial
     * @param tipos
     * @return 
     */
    private Object[][] convertirModelo( Object[][] inicial, String[] tipos ){
        Object [][] retorno = new Object[inicial.length][];
        
        HashSet<Integer> valoresInt = new HashSet<>();
        // Busca los objetos integer
        for (int i = 0; i < tipos.length; i++) {
            if( tipos[i].equals("INT") )
                valoresInt.add(i);
        }
        
        if( valoresInt.isEmpty() ) return inicial;
        
        // Convierte los valores int
        for (int i = 0; i < inicial.length; i++) {
            Object[] fila = new Object[tipos.length];
            for (int j = 0; j < tipos.length; j++) {
                if( valoresInt.contains(j) )
                    fila[j] = ((Number)inicial[i][j]).intValue();
                else
                    fila[j] = inicial[i][j];
            }
            
            retorno[i] = fila;
        }
        
        return retorno;
    }
    
    /**
     * Diálogo de confirmación para eliminaciones
     * @param mensaje
     * @return 
     */
    public boolean obtenerConfirmacion(String mensaje) {
        int resultado = 
                JOptionPane.showConfirmDialog(MantenimientoGUI.this, mensaje, "Confirmar operación",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        return resultado == JOptionPane.YES_OPTION;
    }
    
    /**
     * Método utilizado para crear una conexión a la base de datos 
     * @param query Query que se desea enviar a la base de datos
     * @return Resultado de la base de datos
     * @throws Exception 
     */
    public String conexion(String query) throws Exception{
        
        StringBuilder resultado = new StringBuilder();
        Socket kkSocket = new Socket("localhost", 4545);
        
        String lineaRetorno;
        try (
        PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader( new InputStreamReader(kkSocket.getInputStream()))) {
            out.println("USE DATABASE proyecto;");
            out.println(query);
            out.println("<FIN>");
            Gson gson = new Gson();
            
            while( (lineaRetorno = in.readLine()) != null ){
                if (!(lineaRetorno.contains("Mensaje"))){
                    resultado.append(lineaRetorno);
                }else{
                    Mensaje mensajeServidor = gson.fromJson(lineaRetorno, Mensaje.class);
                    if( mensajeServidor.isError() )
                        JOptionPane.showMessageDialog(this, mensajeServidor.getContenido(), "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        // Retorno del JSON
        return resultado.toString();
        
    }

}

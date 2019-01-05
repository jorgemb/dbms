package GUI;


import excepciones.ExcepcionBaseDatos;
import excepciones.ExcepcionDBMS;
import excepciones.ExcepcionTabla;
import gramatica.GramaticaSQLLexer;
import gramatica.GramaticaSQLParser;
import gramatica.GramaticaSQLVisitor;
import interfazUsuario.Impresor;
import interfazUsuario.ImpresorMensajes;
import interfazUsuario.VerboseListener;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import motor.Dato;
import motor.TipoDato;
import motor.relacion.Esquema;
import motor.relacion.Fila;
import motor.relacion.Relacion;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitante.VisitanteSQL;

/**
 *
 * @author Jorge
 */
public class GUI extends JFrame{
    private JTextArea txtCodigo;
    private JButton btnEjecutar;
    private JTextPane txtResultados;
    private StringBuilder textoResultados = new StringBuilder();

    /**
     * Constructor.
     * @throws HeadlessException 
     */
    public GUI() throws HeadlessException {
        super("DBMS");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize( 600, 600 );
        
        ImpresorMensajes.registrarImpresor( new ImpresorGUI() );
        
        inicializarComponentes();
//        this.pack();
    }
    
    /**
     * Inicializa todos los componentes.
     */
    private void inicializarComponentes(){
        JPanel pnlPrincipal = new JPanel( new GridBagLayout() );
        
        // Código de ingreso
        GBC layout = new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(1.0f, 0.5f);
        txtCodigo = new JTextArea();
        JScrollPane scrollCodigo = new JScrollPane( txtCodigo );
        scrollCodigo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCodigo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollCodigo.setBorder(BorderFactory.createTitledBorder("Código"));
        
        pnlPrincipal.add(scrollCodigo, layout);
        
        // Botón de ejecutar
        layout = new GBC(0, 1, 1, 1).setAnchor(GBC.WEST);
        btnEjecutar = new JButton("Ejecutar");
        btnEjecutar.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                onButtonPress();
            }
            
        });
        pnlPrincipal.add(btnEjecutar, layout);
        
        txtCodigo.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ke) {
                if( ke.getKeyCode() == KeyEvent.VK_F5 )
                    onButtonPress();
            }
            
        });
        
        // Botón de salidas
        layout = new GBC(0, 2, 2, 1).setFill(GBC.BOTH).setWeight(1.0f, 0.4f);
        txtResultados = new JTextPane();
        txtResultados.setContentType("text/html");
        txtResultados.setEditable(false);
        
        JScrollPane panelResultados = new JScrollPane(txtResultados);
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        pnlPrincipal.add(panelResultados, layout);
        
        // Pone el panel
        this.setLayout( new BorderLayout() );
        this.add(pnlPrincipal, BorderLayout.CENTER );
    }

    private void onButtonPress(){
        String entrada = txtCodigo.getText();
        textoResultados.setLength(0);
        txtResultados.setText("");
        
        try{
            // ANTLR
            ANTLRInputStream input = new ANTLRInputStream(entrada);
            GramaticaSQLLexer lexer = new GramaticaSQLLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            GramaticaSQLParser parser = new GramaticaSQLParser(tokens);

            // Manejo de errores
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

            // Ejecución de parser
            ParseTree arbol = parser.program();

            // Visitar
            GramaticaSQLVisitor visitante = new VisitanteSQL();
            visitante.visit(arbol);
        } catch (ExcepcionBaseDatos excepcionBD){
            ImpresorMensajes.imprimirMensajeError(excepcionBD.getMessage());
        } catch (ExcepcionTabla excepcionTabla){
            ImpresorMensajes.imprimirMensajeError(excepcionTabla.getMessage());
        } catch( ExcepcionDBMS ex ){
            ImpresorMensajes.imprimirMensajeError(ex.getMessage());
        }
    }

    
    /**
     * Impresor de mensajes
     */
    private class ImpresorGUI implements Impresor{

        @Override
        public void imprimirMensaje(String txtMensaje) {
//            textoResultados.append("<p>").append(txtMensaje).append( "</p>");
            textoResultados.append(txtMensaje).append("<br/>");
            txtResultados.setText(textoResultados.toString());
        }

        @Override
        public void imprimirError(String txtError) {
//            textoResultados.append( "<p><font color='red'>" + txtError + "</font></p>" );
            textoResultados.append("<font color='red'>").append(txtError).append("</font><br/>");
            txtResultados.setText(textoResultados.toString());
        }

        @Override
        public void imprimirRelacion(Relacion relacion) {
            textoResultados.append("<table border=\"1\">");
            
            // Imprime el esquema
            textoResultados.append("<tr>");
            Esquema esquema = relacion.obtenerEsquema();
            TipoDato[] tipos = esquema.obtenerTipos();
            for (int i = 0; i < esquema.obtenerTamaño(); i++) {
                textoResultados.append(String.format("<th>%s(%s)</th>", 
                        relacion.obtenerNombreCalificado(i), tipos[i]) );
            }
            textoResultados.append("</tr>");
            
            // Imprime las líneas
            int cantidad = 0;
            for (Fila filaActual : relacion) {
                textoResultados.append("<tr>");
                for( Dato dato: filaActual ){
                    textoResultados.append(String.format("<td>%s</td>", dato.representacion()));
                }
                textoResultados.append("</tr>");
                ++cantidad;
            }
            
            textoResultados.append("</table>");
            textoResultados.append("Se encontraron ").append(cantidad).append(" filas.</br>");
            
            txtResultados.setText(textoResultados.toString());
        }

        @Override
        public boolean obtenerConfirmacion(String mensaje) {
            int resultado = 
                    JOptionPane.showConfirmDialog(GUI.this, mensaje, "Confirmar operación",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            return resultado == JOptionPane.YES_OPTION;
        }
        
    }
}

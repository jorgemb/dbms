package GUI;


import excepciones.ExcepcionBaseDatos;
import excepciones.ExcepcionDBMS;
import excepciones.ExcepcionTabla;
import grammar.SQLGrammarLexer;
import grammar.SQLGrammarParser;
import grammar.SQLGrammarVisitor;
import interfazUsuario.Impresor;
import interfazUsuario.MessagePrinter;
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
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitante.VisitanteSQL;

/**
 * Basic GUI for interacting with the DBMS.
 * @author Jorge
 */
public class GUI extends JFrame{
    private JTextArea txtCode;
    private JButton btnExecute;
    private JTextPane txtResults;
    private StringBuilder resultsText = new StringBuilder();

    /**
     * Constructor.
     * @throws HeadlessException 
     */
    public GUI() throws HeadlessException {
        super("DBMS");
        
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationByPlatform(true);
        this.setSize( 600, 600 );
        
        MessagePrinter.registerPrinter( new ImpresorGUI() );
        
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
        txtCode = new JTextArea();
        JScrollPane scrollCodigo = new JScrollPane( txtCode );
        scrollCodigo.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCodigo.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollCodigo.setBorder(BorderFactory.createTitledBorder("Código"));
        
        pnlPrincipal.add(scrollCodigo, layout);
        
        // Botón de ejecutar
        layout = new GBC(0, 1, 1, 1).setAnchor(GBC.WEST);
        btnExecute = new JButton("Ejecutar");
        btnExecute.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                onButtonPress();
            }
            
        });
        pnlPrincipal.add(btnExecute, layout);
        
        txtCode.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ke) {
                if( ke.getKeyCode() == KeyEvent.VK_F5 )
                    onButtonPress();
            }
            
        });
        
        // Botón de salidas
        layout = new GBC(0, 2, 2, 1).setFill(GBC.BOTH).setWeight(1.0f, 0.4f);
        txtResults = new JTextPane();
        txtResults.setContentType("text/html");
        txtResults.setEditable(false);
        
        JScrollPane panelResultados = new JScrollPane(txtResults);
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        pnlPrincipal.add(panelResultados, layout);
        
        // Pone el panel
        this.setLayout( new BorderLayout() );
        this.add(pnlPrincipal, BorderLayout.CENTER );
    }

    private void onButtonPress(){
        String entrada = txtCode.getText();
        resultsText.setLength(0);
        txtResults.setText("");
        
        try{
            // ANTLR
            CharStream input = CharStreams.fromString(entrada);
            SQLGrammarLexer lexer = new SQLGrammarLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SQLGrammarParser parser = new SQLGrammarParser(tokens);

            // Manejo de errores
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

            // Ejecución de parser
            ParseTree arbol = parser.program();

            // Visitar
            SQLGrammarVisitor visitante = new VisitanteSQL();
            visitante.visit(arbol);
        } catch (ExcepcionBaseDatos excepcionBD){
            MessagePrinter.imprimirMensajeError(excepcionBD.getMessage());
        } catch (ExcepcionTabla excepcionTabla){
            MessagePrinter.imprimirMensajeError(excepcionTabla.getMessage());
        } catch( ExcepcionDBMS ex ){
            MessagePrinter.imprimirMensajeError(ex.getMessage());
        }
    }

    
    /**
     * Impresor de mensajes
     */
    private class ImpresorGUI implements Impresor{

        @Override
        public void imprimirMensaje(String txtMensaje) {
//            textoResultados.append("<p>").append(txtMensaje).append( "</p>");
            resultsText.append(txtMensaje).append("<br/>");
            txtResults.setText(resultsText.toString());
        }

        @Override
        public void imprimirError(String txtError) {
//            textoResultados.append( "<p><font color='red'>" + txtError + "</font></p>" );
            resultsText.append("<font color='red'>").append(txtError).append("</font><br/>");
            txtResults.setText(resultsText.toString());
        }

        @Override
        public void imprimirRelacion(Relacion relacion) {
            resultsText.append("<table border=\"1\">");
            
            // Imprime el esquema
            resultsText.append("<tr>");
            Esquema esquema = relacion.obtenerEsquema();
            TipoDato[] tipos = esquema.obtenerTipos();
            for (int i = 0; i < esquema.obtenerTamaño(); i++) {
                resultsText.append(String.format("<th>%s(%s)</th>", 
                        relacion.obtenerNombreCalificado(i), tipos[i]) );
            }
            resultsText.append("</tr>");
            
            // Imprime las líneas
            int cantidad = 0;
            for (Fila filaActual : relacion) {
                resultsText.append("<tr>");
                for( Dato dato: filaActual ){
                    resultsText.append(String.format("<td>%s</td>", dato.representacion()));
                }
                resultsText.append("</tr>");
                ++cantidad;
            }
            
            resultsText.append("</table>");
            resultsText.append("Se encontraron ").append(cantidad).append(" filas.</br>");
            
            txtResults.setText(resultsText.toString());
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

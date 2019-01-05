package GUI;


import excepciones.DatabaseException;
import excepciones.DBMSException;
import excepciones.TableException;
import grammar.SQLGrammarLexer;
import grammar.SQLGrammarParser;
import grammar.SQLGrammarVisitor;
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
import motor.Data;
import motor.DataType;
import motor.relacion.Schema;
import motor.relacion.Row;
import motor.relacion.Relation;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import visitante.SQLVisitor;
import interfazUsuario.Printer;

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
        
        MessagePrinter.registerPrinter( new GUIPrinter() );
        
        initComponents();
//        this.pack();
    }
    
    /**
     * Initializes all components
     */
    private void initComponents(){
        JPanel pnlMain = new JPanel( new GridBagLayout() );
        
        // Input code
        GBC layout = new GBC(0, 0, 2, 1).setFill(GBC.BOTH).setWeight(1.0f, 0.5f);
        txtCode = new JTextArea();
        JScrollPane scrollCode = new JScrollPane( txtCode );
        scrollCode.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollCode.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        scrollCode.setBorder(BorderFactory.createTitledBorder("Code to Execute"));
        
        pnlMain.add(scrollCode, layout);
        
        // Execute button
        layout = new GBC(0, 1, 1, 1).setAnchor(GBC.WEST);
        btnExecute = new JButton("Execute");
        btnExecute.addActionListener( new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
                onButtonPress();
            }
            
        });
        pnlMain.add(btnExecute, layout);
        
        txtCode.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent ke) {
                if( ke.getKeyCode() == KeyEvent.VK_F5 )
                    onButtonPress();
            }
            
        });
        
        // Results
        layout = new GBC(0, 2, 2, 1).setFill(GBC.BOTH).setWeight(1.0f, 0.4f);
        txtResults = new JTextPane();
        txtResults.setContentType("text/html");
        txtResults.setEditable(false);
        
        JScrollPane panelResults = new JScrollPane(txtResults);
        panelResults.setBorder(BorderFactory.createTitledBorder("Output"));
        pnlMain.add(panelResults, layout);
        
        // Add panel
        this.setLayout( new BorderLayout() );
        this.add(pnlMain, BorderLayout.CENTER );
    }

    private void onButtonPress(){
        String input = txtCode.getText();
        resultsText.setLength(0);
        txtResults.setText("");
        
        try{
            // ANTLR
            CharStream inputStream = CharStreams.fromString(input);
            SQLGrammarLexer lexer = new SQLGrammarLexer(inputStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SQLGrammarParser parser = new SQLGrammarParser(tokens);

            // Error management
            parser.removeErrorListeners();
            parser.addErrorListener(new VerboseListener());

            // Parser execution
            ParseTree tree = parser.program();

            // Start visitor
            SQLGrammarVisitor visitor = new SQLVisitor();
            visitor.visit(tree);
        } catch (DatabaseException databaseException){
            MessagePrinter.printErrorMessage(databaseException.getMessage());
        } catch (TableException tableException){
            MessagePrinter.printErrorMessage(tableException.getMessage());
        } catch( DBMSException ex ){
            MessagePrinter.printErrorMessage(ex.getMessage());
        }
    }

    
    /**
     * Message printer
     */
    private class GUIPrinter implements Printer{

        @Override
        public void printMessage(String message) {
            resultsText.append(message).append("<br/>");
            txtResults.setText(resultsText.toString());
        }

        @Override
        public void printError(String errorMessage) {
            resultsText.append("<font color='red'>").append(errorMessage).append("</font><br/>");
            txtResults.setText(resultsText.toString());
        }

        @Override
        public void printRelation(Relation relation) {
            resultsText.append("<table border=\"1\">");
            
            // Prints schema
            resultsText.append("<tr>");
            Schema schema = relation.getSchema();
            DataType[] types = schema.getTypes();
            for (int i = 0; i < schema.getSize(); i++) {
                resultsText.append(String.format("<th>%s(%s)</th>", 
                        relation.getQualifiedName(i), types[i]) );
            }
            resultsText.append("</tr>");
            
            // Prints lines
            int amount = 0;
            for (Row currentRow : relation) {
                resultsText.append("<tr>");
                for( Data datum: currentRow ){
                    resultsText.append(String.format("<td>%s</td>", datum.representation()));
                }
                resultsText.append("</tr>");
                ++amount;
            }
            
            resultsText.append("</table>");
            resultsText.append(amount).append(" rows were found.</br>");
            
            txtResults.setText(resultsText.toString());
        }

        @Override
        public boolean getConfirmation(String message) {
            int result = 
                    JOptionPane.showConfirmDialog(GUI.this, message, "Confirm operation",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            
            return result == JOptionPane.YES_OPTION;
        }
        
    }
}

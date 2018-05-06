package br.com.cfg.controlflowgrapher.controller;

import br.com.cfg.controlflowgrapher.kernel.SimpleScanner;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import static guru.nidi.graphviz.model.Factory.*;
import guru.nidi.graphviz.model.Graph;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.Subscription;

public class FXMLController implements Initializable {
    
    private static final String[] KEYWORDS = new String[] {
        "abstract", "assert", "boolean", "break", "byte",
        "case", "catch", "char", "class", "const",
        "continue", "default", "do", "double", "else",
        "enum", "extends", "final", "finally", "float",
        "for", "goto", "if", "implements", "import",
        "instanceof", "int", "interface", "long", "native",
        "new", "package", "private", "protected", "public",
        "return", "short", "static", "strictfp", "super",
        "switch", "synchronized", "this", "throw", "throws",
        "transient", "try", "void", "volatile", "while", "null"
    };
    
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
    
    private static final Pattern PATTERN = Pattern.compile(
            "(?<KEYWORD>" + KEYWORD_PATTERN + ")"
                    + "|(?<PAREN>" + PAREN_PATTERN + ")"
                            + "|(?<BRACE>" + BRACE_PATTERN + ")"
                                    + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
                                            + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
                                                    + "|(?<STRING>" + STRING_PATTERN + ")"
                                                            + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
    );
    
    private static final String sampleCode = String.join("\n", new String[] {
        "class Geometria {\n" +
        "   public static void main (String[ ] ID){}\n" +
        "   public int Triangulo(String args[ ]){\n" +
        "       int a;\n" +
        "       int b;\n" +
        "       int c;\n" +
        "       String resp = null;\n" +
        "       a = Integer.parseInt(args[0]);\n" +
        "       b = Integer.parseInt(args[1]);\n" +
        "       c = Integer.parseInt(args[2]);\n" +
        "       if ((a==b)&&(b==c))\n" +
        "           resp = \"equilatero\";\n" +
        "       if (((a==b)&&(b!=c))||((b==c)&&(a!=b))||((a==c)&&(c!=b)))\n" +
        "           resp = \"isoceles\";\n" +
        "       if ((a!=b)&&(b!=c))\n" +
        "           resp = \"escaleno\";\n" +
        "       System.out.println(\"Tipo de triangulo:\"+ resp);\n" +
        "   }\n" +
        "}"
    });
    
    @FXML
    private StackPane stackPane;
    
    @FXML
    private BorderPane borderPane;
    
    private CodeArea codeArea;
    
    private File graph;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        codeArea = new CodeArea();
        
        // add line numbers to the left of area
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        
        // recompute the syntax highlighting 500 ms after user stops editing area
        Subscription cleanupWhenNoLongerNeedIt = codeArea
                
                // plain changes = ignore style changes that are emitted when syntax highlighting is reapplied
                // multi plain changes = save computation by not rerunning the code multiple times
                //   when making multiple changes (e.g. renaming a method at multiple parts in file)
                .multiPlainChanges()
                
                // do not emit an event until 500 ms have passed since the last emission of previous stream
                .successionEnds(Duration.ofMillis(500))
                
                // run the following code block when previous stream emits an event
                .subscribe(ignore -> codeArea.setStyleSpans(0, computeHighlighting(codeArea.getText())));
        
        // when no longer need syntax highlighting and wish to clean up memory leaks
        // run: `cleanupWhenNoLongerNeedIt.unsubscribe();`
        
        codeArea.replaceText(0, 0, sampleCode);
        
        stackPane.getChildren().add(new VirtualizedScrollPane<>(codeArea));
        codeArea.setBackground(new Background(new BackgroundFill(Paint.valueOf("#2B2B2B"), CornerRadii.EMPTY, Insets.EMPTY)));
        
        try {
            generateGraph();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                    matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */ assert styleClass != null;
                    spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
                    spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
                    lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }
    
    private void loadGraphPNG(Image image) {
        
        ImageView img = new ImageView(image);
        
        img.setPreserveRatio(true);
        if(image.getHeight() > image.getWidth()) {
            img.fitHeightProperty().bind(borderPane.heightProperty());
        } else {
            img.fitWidthProperty().bind(borderPane.widthProperty());
        }
        
        borderPane.setCenter(img);
    }
    
    private void generateGraph() throws IOException {
        graph = new File("graphs/ex19.png");
        Graph g = graph("example19").directed().with(node("a").link(node("b").link(node("c"))).link(node("d").link(node("e").link(node("f").link("System.err.println(\"Warning: empty string as argument\");")))));
        Graphviz.fromGraph(g).width(2000).render(Format.PNG).toFile(graph);
    }
    
    public void handleCompile(ActionEvent event) throws MalformedURLException {
        SimpleScanner scanner = new SimpleScanner(codeArea.getText());
        
        //PreParser preParser = new PreParser(codeArea.getText());
        //try {
            //Parser parser = new Parser(codeArea.getText(), preParser.execute());
            //System.out.println(parser.execute(preParser.execute()));
        //} catch (CompilerException e) {
        //    System.err.println(e.getMessage());
        //}
        /*
        try{
            Image image = new Image(graph.toURI().toURL().toExternalForm());
            loadGraphPNG(image);
        } catch (IllegalArgumentException e){
            //e.printStackTrace();
            System.out.println("File not found!");
        }
        */
    }
    
}

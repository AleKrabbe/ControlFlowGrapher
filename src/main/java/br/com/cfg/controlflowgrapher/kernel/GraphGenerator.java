package br.com.cfg.controlflowgrapher.kernel;

import br.com.cfg.controlflowgrapher.model.*;

/**
 * @author Alexandre
 */
public class GraphGenerator {    
    
    private final Scanner scanner;
    private Token lToken;
    
    public GraphGenerator(String input) {
        scanner = new Scanner(input);
    }
    
    public String generate(){    
        lToken = scanner.nextToken();
        String dot = "digraph G {\n";
        
        LocalNode rootNode = new LocalNode();
        if(lToken.name != EnumToken.EOF) {
            rootNode.setInitialLine(lToken.lineNumber);
            do {
                lToken = scanner.nextToken();
                //if(lToken.value == EnumToken.IF || lToken.value == EnumToken.FOR || lToken.value == EnumToken.WHILE){
                //}
            } while(lToken.name != EnumToken.EOF);
        }
        dot += "}\n";
        return dot;
    }
}

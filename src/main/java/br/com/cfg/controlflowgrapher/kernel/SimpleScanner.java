package br.com.cfg.controlflowgrapher.kernel;

import br.com.cfg.controlflowgrapher.model.*;

/**
 * @author Alexandre
 */
public class SimpleScanner {    
    
    private final String input;
    private final SymbolTable globalST;
    private final Scanner scanner;
    private Token lToken;
    
    public SimpleScanner(String input) {
        this.input = input;
        globalST = new SymbolTable();
        initSymbolTable();
        scanner = new Scanner(globalST, this.input);
        execute();
    }
    
    private void execute(){    
        lToken = scanner.nextToken();
        while(lToken.name != EnumToken.EOF){
            System.out.println(lToken.name);
            lToken = scanner.nextToken();
        }
    }
    
    private void initSymbolTable() {
        STEntry entry = new STEntry(new Token(EnumToken.BOOLEAN), "boolean", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.CLASS), "class", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.ELSE), "else", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.EXTENDS), "extends", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.FALSE), "false", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.IF), "if", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.INT), "int", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.LENGTH), "length", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.MAIN), "main", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.NEW), "new", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.PUBLIC), "public", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.RETURN), "return", true);
        globalST.add(entry);        
        entry = new STEntry(new Token(EnumToken.STATIC), "static", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.STRING), "String", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.SOPRINTLN), "System.out.println", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.THIS), "this", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.TRUE), "true", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.VOID), "void", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.WHILE), "while", true);
        globalST.add(entry);
        entry = new STEntry(new Token(EnumToken.FOR), "for", true);
        globalST.add(entry);
    }    
}

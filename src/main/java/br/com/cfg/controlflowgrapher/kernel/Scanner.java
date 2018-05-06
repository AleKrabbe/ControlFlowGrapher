package br.com.cfg.controlflowgrapher.kernel;

import java.text.StringCharacterIterator;
import br.com.cfg.controlflowgrapher.model.EnumToken;
import br.com.cfg.controlflowgrapher.model.STEntry;
import br.com.cfg.controlflowgrapher.model.SymbolTable;
import br.com.cfg.controlflowgrapher.model.Token;

/**
 * @author alekrabbe
 * @author Bianca
 */
public class Scanner {
    
    private static String input;
    private final StringCharacterIterator inputIt;
    private final SymbolTable st;
    private int lineNumber;
    
    public Scanner(String inputStream) {
        st = new SymbolTable();
        initSymbolTable();
        input = inputStream;
        inputIt = new StringCharacterIterator(input);
        lineNumber = 1;
    }
    
    public Token nextToken() {
        Token tok = new Token(EnumToken.UNDEF);
        STEntry entry = null;
        int begin = 0, end = 0;
        String lexema, text;
        char ch = inputIt.current();
        boolean flag = false;
        boolean system = false;
        boolean comment = false;
        
        while (true) {
            //Consome espaços em branco e volta para o estado inicial
            if (inputIt.current() == '\n' || inputIt.current() == ' '
                    || inputIt.current() == '\t' || inputIt.current() == '\r'
                    || inputIt.current() == '\f') {
                if (inputIt.current() == '\n' || inputIt.current() == '\r') {
                    tok.attribute = EnumToken.SYSATTR;
                    tok.lineNumber = lineNumber;
                    tok.name = EnumToken.NEWLINE;
                    lineNumber += 1;
                    inputIt.next();
                    return tok;
                }
                inputIt.next();
            } else if (Character.isLetter(inputIt.current())) {
                begin = inputIt.getIndex();
                tok.attribute = EnumToken.RESERVEDWORD;
                tok.lineNumber = lineNumber;
                lexema = "";
                do {
                    lexema += inputIt.current();
                    inputIt.next();
                } while (Character.isLetter(inputIt.current()) || Character.isDigit(inputIt.current()) || inputIt.current() == '_');
                end = inputIt.getIndex();
                
                if(lexema.equals("System")){
                    system = true;
                    do {
                        lexema += inputIt.current();
                        inputIt.next();
                    } while (Character.isLetter(inputIt.current()) || inputIt.current() == '.');
                }
                
                entry = st.get(lexema);
                tok.value = lexema;
                if(entry != null && entry.reserved){
                    tok.name = entry.token.name;
                } else {
                    tok.name = EnumToken.ID;
                    if(system){
                        system = false;
                        String[] lexemas = lexema.split("\\.");
                        int size = lexemas.length - 1;
                        for (String palavra : lexemas) {
                            if (!palavra.equals("System")) {
                                size += palavra.length();
                            }
                        }
                        inputIt.setIndex(inputIt.getIndex() - size);
                    }
                }
                
                return tok;
            } else if (Character.isDigit(inputIt.current())) {
                tok.attribute = EnumToken.INTEGER_LITERAL;
                tok.name = EnumToken.NUMBER;
                tok.lineNumber = lineNumber;
                do {
                    inputIt.next();
                } while (Character.isDigit(inputIt.current()));
                return tok;
            } //Operadores aritméticos e Ignora comentários
            else if (inputIt.current() == '+' || inputIt.current() == '-'
                    || inputIt.current() == '*' || inputIt.current() == '/') {
                tok.attribute = EnumToken.ARITHOP;
                
                switch (inputIt.current()) {
                    case '+':
                        tok.name = EnumToken.PLUS;
                        break;
                    case '-':
                        tok.name = EnumToken.MINUS;
                        break;
                    case '*':
                        tok.name = EnumToken.MULT;
                        break;
                    case '/':
                        comment = true;
                        if (input.charAt(inputIt.getIndex() + 1) == '/') {
                            tok.attribute = EnumToken.COMMENTARY;
                            tok.name = EnumToken.LINECOMMENT;
                            do {
                                inputIt.next();
                            } while (inputIt.current() != '\n');
                            lineNumber += 1;
                        } else if (input.charAt(inputIt.getIndex() + 1) == '*') {
                            tok.attribute = EnumToken.COMMENTARY;
                            tok.name = EnumToken.BLOCKCOMMENT;
                            inputIt.next();
                            while (input.charAt(inputIt.getIndex() + 1) != '/' || !flag) {
                                inputIt.next();
                                flag = false;
                                if(inputIt.current() == '\n'){
                                    lineNumber += 1;
                                }
                                if (inputIt.current() == '*' && input.charAt(inputIt.getIndex() + 1) == '/') {
                                    flag = true;
                                }
                            }
                            inputIt.next();
                        } else {
                            comment = false;
                            tok.name = EnumToken.DIV;
                            inputIt.next();
                        }
                        
                        break;
                }
                
                inputIt.next();
                tok.lineNumber = lineNumber;
                
                if(!comment){
                    return tok;
                }
                
            } else if (inputIt.current() == '>' || inputIt.current() == '<'
                    || inputIt.current() == '=' || inputIt.current() == '!'
                    || inputIt.current() == '&' || inputIt.current() == '|') {
                tok.attribute = EnumToken.RELOP;
                
                switch (inputIt.current()) {
                    case '>':
                        tok.name = EnumToken.GT;
                        break;
                    case '<':
                        tok.name = EnumToken.LT;
                        break;
                    case '=':
                        if (input.charAt(inputIt.getIndex() + 1) != '=') {
                            tok.attribute = EnumToken.ASSGNOP;
                            tok.name = EnumToken.ATTRIB;
                        } else {
                            tok.name = EnumToken.EQ;
                            inputIt.next();
                        }
                        break;
                    case '!':
                        if (input.charAt(inputIt.getIndex() + 1) != '=') {
                            tok.name = EnumToken.NOT;
                        } else {
                            tok.name = EnumToken.NE;
                            inputIt.next();
                        }
                        break;
                    case '&':
                        if (input.charAt(inputIt.getIndex() + 1) != '&') {
                            tok.name = EnumToken.BITAND;
                        } else {
                            tok.name = EnumToken.AND;
                            inputIt.next();
                        }
                        break;
                    case '|':
                        if (input.charAt(inputIt.getIndex() + 1) != '|') {
                            tok.name = EnumToken.BITOR;
                        } else {
                            tok.name = EnumToken.OR;
                            inputIt.next();
                        }
                        break;
                }
                
                inputIt.next();
                
                tok.lineNumber = lineNumber;
                return tok;
            } else if (inputIt.current() == '(' || inputIt.current() == ')'
                    || inputIt.current() == '[' || inputIt.current() == ']'
                    || inputIt.current() == '{' || inputIt.current() == '}'
                    || inputIt.current() == ';' || inputIt.current() == '.'
                    || inputIt.current() == ',') {
                
                tok.attribute = EnumToken.SEPARATOR;
                
                switch (inputIt.current()) {
                    case '(':
                        tok.name = EnumToken.LPARENTHESE;
                        break;
                    case ')':
                        tok.name = EnumToken.RPARENTHESE;
                        break;
                    case '[':
                        tok.name = EnumToken.LBRACKET;
                        break;
                    case ']':
                        tok.name = EnumToken.RBRACKET;
                        break;
                    case '{':
                        tok.name = EnumToken.LBRACE;
                        break;
                    case '}':
                        tok.name = EnumToken.RBRACE;
                        break;
                    case ';':
                        tok.name = EnumToken.SEMICOLON;
                        break;
                    case '.':
                        tok.name = EnumToken.PERIOD;
                        break;
                    case ',':
                        tok.name = EnumToken.COMMA;
                        break;
                }
                
                inputIt.next();
                
                tok.lineNumber = lineNumber;
                return tok;
                
            } else if (inputIt.current() == '"') {
                inputIt.next();
                tok.name = EnumToken.TEXT;
                text = "";
                tok.lineNumber = lineNumber;
                if(inputIt.current() != '"'){
                    do {
                        text += inputIt.current();
                        inputIt.next();
                    } while (inputIt.current() != '"');
                }
                inputIt.next();
                tok.value = text;
                return tok;
                
            } else {
                tok.name = EnumToken.EOF;
                tok.lineNumber = lineNumber;
                return tok;
            }
        }
    }
    
    private void initSymbolTable() {
        STEntry entry = new STEntry(new Token(EnumToken.BOOLEAN), "boolean", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.CLASS), "class", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.ELSE), "else", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.EXTENDS), "extends", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.FALSE), "false", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.IF), "if", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.INT), "int", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.LENGTH), "length", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.MAIN), "main", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.NEW), "new", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.PUBLIC), "public", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.RETURN), "return", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.STATIC), "static", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.STRING), "String", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.SOPRINTLN), "System.out.println", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.THIS), "this", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.TRUE), "true", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.VOID), "void", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.WHILE), "while", true);
        st.add(entry);
        entry = new STEntry(new Token(EnumToken.FOR), "for", true);
        st.add(entry);
    }
    
}

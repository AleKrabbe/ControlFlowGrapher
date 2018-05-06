package br.com.cfg.controlflowgrapher.model;

import java.util.ArrayList;

/**
 * @author facom
 */
public class STEntry 
{
    public Token token;
    public String lexeme;
    public String tipo;
    public String nomeClasseIfMetodo;
    public boolean reserved;
    public boolean classe;
    public boolean metodo;
    public boolean variable;
    public String returnType;
    public ArrayList<Parametro> parameters;
    
    public STEntry()
    {}
    
    public STEntry(Token tok, String lex)
    {
        token = tok;
        lexeme = lex;
        reserved = false;
        classe = false;
        metodo = false;
        variable = false;
        tipo = EnumToken.UNDEF.toString();
        nomeClasseIfMetodo = "";
        returnType = "";
        parameters = new ArrayList<Parametro>();
        //double var = 2.e+24;
    }
    
    public STEntry(Token tok, String lex, boolean res)
    {
        token = tok;
        lexeme = lex;
        reserved = res;
    }
    
    public int getNumOfParams(){
        return parameters.size();
    }
    
    public ArrayList<Parametro> getParamList(){
        return parameters;
    }
}

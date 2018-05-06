package br.com.cfg.controlflowgrapher.model;

/**
 * @author alekrabbe
 */
public class Parametro {
    
    public String tipo;
    public String nome;
    public int index;
    
    public Parametro(String type, String name, int order){
        this.tipo = type;
        this.nome = name;
        this.index = order;
    }
    
}

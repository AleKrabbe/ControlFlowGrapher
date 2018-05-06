package br.com.cfg.controlflowgrapher.model;

import java.util.ArrayList;

/**
 * @author Alexandre
 */
public class LocalNode {
    
    private String name;
    private ArrayList<Integer> lines;
    
    public LocalNode() {
        lines = new ArrayList<>();
    }
    
    public void addToLines(int n){
        if (!lines.contains(n)) {
            lines.add(n);
        }
    }
    
    public String getName() {
        String name = "";
        if (!lines.isEmpty()) {
            name = String.valueOf(lines.get(0));
            for(int i = 1; i < lines.size(); i++){
                name += ", " + lines.get(i);
            }
        }
        return name;
    }
    
    public void setInitialLine (int n) {
        lines.add(n);
    }
}

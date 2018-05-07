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
        if (lines.isEmpty()) {
            lines.add(n);
        } else {
            if (!lines.contains(n)) {
                lines.add(n);
            }
        }
    }
    
    public String getName() {
        String name = "\"";
        if (lines.size() > 2) {
            name += lines.get(0) + "..." + lines.get(lines.size()-1);
        } else {
        if (!lines.isEmpty()) {
            name += String.valueOf(lines.get(0));
            for(int i = 1; i < lines.size(); i++){
                name += ", " + lines.get(i);
            }
        }
        }
        name += "\"";
        return name;
    }
}

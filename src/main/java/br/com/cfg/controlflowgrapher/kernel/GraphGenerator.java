package br.com.cfg.controlflowgrapher.kernel;

import br.com.cfg.controlflowgrapher.model.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Alexandre
 */
public class GraphGenerator {
    
    private final Scanner scanner;
    private Token lToken;
    private HashMap<LocalNode, LocalNode> links;
    private HashMap<EnumToken, ArrayList<Integer>> branchLocations;
    
    public GraphGenerator(String input) {
        scanner = new Scanner(input);
    }
    
    public String generate(){
        lToken = scanner.nextToken();
        String dot = "digraph G {\n";
        branchLocations = new HashMap();
        ArrayList<Integer> lines;
        int origin, jumpTo;
        Boolean newLineFlag = true;
        
        while(lToken.name != EnumToken.EOF){
            if(lToken.name == EnumToken.IF || lToken.name == EnumToken.FOR || lToken.name == EnumToken.WHILE || lToken.name == EnumToken.ELSE) {
                if (branchLocations.containsKey(lToken.name)) {
                    branchLocations.get(lToken.name).add(lToken.lineNumber);
                } else {
                    lines = new ArrayList<>();
                    lines.add(lToken.lineNumber);
                    branchLocations.put(lToken.name, lines );
                }
            }
            lToken = scanner.nextToken();
        }
        
        //Como implementar isso??
//        if (!mapIsEmpty(branchLocations)){
//            origin = 1;
//            jumpTo = lookForNextBranch(branchLocations);
//            dot += "\"" + origin + "..." + jumpTo + "\" -> ";
//            while (!mapIsEmpty(branchLocations)) {                
//                origin = jumpTo + 1;
//                jumpTo = lookForNextBranch(branchLocations);
//                dot += "\"" + origin + "..." + jumpTo + "\"\n";
//                if (!mapIsEmpty(branchLocations)) {
//                    dot += "\"" + origin + "..." + jumpTo + "\" -> ";
//                }              
//            }
//        }
        
        dot += "}\n";
        return dot;
    }
    
    private int lookForNextBranch(HashMap<EnumToken, ArrayList<Integer>> map){
        int line = Integer.MAX_VALUE;
        int indexOfElementToBeRemoved = Integer.MAX_VALUE;
        ArrayList<Integer> listWithNextBranch = new ArrayList<>();
        for (EnumToken e : map.keySet()){
            ArrayList<Integer> list = map.get(e);
            for (int j = 0; j < list.size(); j++){
                if (list.get(j) < line) {
                    line = list.get(j);
                    indexOfElementToBeRemoved = j;
                    listWithNextBranch = list;
                }
            }
        }
        
        if(indexOfElementToBeRemoved != Integer.MAX_VALUE){
            listWithNextBranch.remove(indexOfElementToBeRemoved);
        }
        
        return line;
    }
    
    private boolean mapIsEmpty (HashMap<EnumToken, ArrayList<Integer>> map) {
        for (EnumToken e : map.keySet()){
            ArrayList<Integer> list = map.get(e);
            if (!list.isEmpty()){
                return false;
            }
        }
        return true;
    }
    
}

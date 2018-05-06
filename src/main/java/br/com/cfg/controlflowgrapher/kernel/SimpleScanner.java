package br.com.cfg.controlflowgrapher.kernel;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @author Alexandre
 */
public class SimpleScanner {    
    
    private HashSet<String> keyWords = new HashSet<String>(
            Arrays.asList(new String[] {"while", "if", "else", "for"}));    
    
    private String input;
    
    public SimpleScanner(String input) {
        this.input = input;
        execute();
    }
    
    private void execute(){    
        String[] lines = input.split("\\r?\\n");
        String newLine = System.getProperty("line.separator");
//        while ((currentLine = readFile.readLine()) != null) {
//            String[] tokens= currentLine.split(" ");
//            for (String token : tokens) {
//                if (keyWords.contains(token)) {
//                    jTextArea1.append(token + newLine);
//                } else if (dataTypes.contains(token)) {
//                    jTextArea2.append(token + newLine);
//                }
//            }
//        }
    }
    
}

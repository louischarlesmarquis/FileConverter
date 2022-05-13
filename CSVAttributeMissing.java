
package fileconverter;

public class CSVAttributeMissing extends Exception{
    
    public CSVAttributeMissing(){
        super("Error: Input row cannot be parsed due to missing information");
    }
    public CSVAttributeMissing(String message){
        super(message);
    }
}

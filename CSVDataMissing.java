
package fileconverter;

public class CSVDataMissing extends Exception{
    
    public CSVDataMissing(){
        super("Error: Input row cannot be parsed due to missing information");
    }
    public CSVDataMissing(String message){
        super(message);
    }
}

package godial.dialstructure;

import java.util.ArrayList;

/**
 * Created by zhouyi on 16-10-23.
 */
public class DialStructure {
    private ArrayList<DialElement> dialElements;

    public ArrayList<DialElement> getDialElements(){
        return dialElements;
    }

    public DialStructure(ArrayList<DialElement> dialElements){
        this.dialElements=dialElements;
    }
}

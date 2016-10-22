package dugu9sword.godial;

import java.util.ArrayList;

/**
 * Created by Yi Zhou on 2016/10/22.
 */
public class Kernel {
    private ArrayList<Converter> converters;
    private ArrayList<Generator> generators;
    private ArrayList<Context> contexts;

    public void registerConverter(Converter converter){
        converters.add(converter);
    }

    public void registerGenerator(Generator generator){
        generators.add(generator);
    }

    public Context select(){
        return null;
    }
}

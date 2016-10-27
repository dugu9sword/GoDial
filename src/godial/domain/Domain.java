package godial.domain;

import godial.act.SystemAct;
import godial.act.UserAct;
import godial.context.Context;
import godial.dialstructure.ConfigLoader;
import godial.dialstructure.DialStructure;
import godial.domain.converter.AbstractConverter;
import godial.domain.converter.DefaultConverter;
import godial.domain.executor.AbstractExecutor;
import godial.domain.executor.DefaultExecutor;
import godial.domain.generator.AbstractGenerator;
import godial.domain.generator.DefaultGenerator;
import godial.kernel.Kernel;

import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-23.
 */
public class Domain implements IDomain {

    private String name;

    private DialStructure dialStructure;
    private AbstractConverter converter;
    private AbstractGenerator generator;
    private AbstractExecutor executor;
    private Kernel kernel;

    public void setConverter(AbstractConverter converter) {
        this.converter = converter;
        converter.setDomain(this);
    }

    public void setGenerator(AbstractGenerator generator) {
        this.generator = generator;
        generator.setDomain(this);
    }

    public void setExecutor(AbstractExecutor executor){
        this.executor=executor;
        executor.setDomain(this);
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    public Context correspondingContext(){
        return kernel.getContextByDomain(this);
    }

    public boolean hasCorrespondingContext(){
        return correspondingContext()!=null?true:false;
    }

    private void loadDialStructure(String path) {
        this.dialStructure = ConfigLoader.loadConfig(path);
    }

    public UserAct convert(String utterance) {
        return converter.convert(utterance);
    }

    public String generate(HashMap map) {
        return generator.generate(map);
    }

    public HashMap execute(SystemAct systemAct) {
        return executor.execute(systemAct);
    }

    public DialStructure getDialStructure() {
        return dialStructure;
    }

    public Domain(String path) {
        loadDialStructure(path);
        setConverter(new DefaultConverter());
        setGenerator(new DefaultGenerator());
        setExecutor(new DefaultExecutor());
        name=path;
    }

    private Domain(){}

    public String toString(){
        return name;
    }

    public static final Domain SYSTEM=new Domain();
}
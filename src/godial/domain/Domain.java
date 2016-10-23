package godial.domain;

import godial.act.SystemAct;
import godial.act.UserAct;
import godial.dialstructure.DialStructure;
import godial.dialstructure.ConfigLoader;
import godial.domain.converter.DefaultConverter;
import godial.domain.converter.IConverter;
import godial.domain.generator.DefaultGenerator;
import godial.domain.generator.IGenerator;

/**
 * Created by zhouyi on 16-10-23.
 */
public class Domain implements IDomain {

    private DialStructure dialStructure;
    private IConverter converter;
    private IGenerator generator;

    public void setConverter(IConverter converter){
        this.converter=converter;
    }

    public void setGenerator(IGenerator generator){
        this.generator=generator;
    }

    private void loadDialStructure(String path){
        this.dialStructure=ConfigLoader.loadConfig(path);
    }

    public UserAct convert(String utterance){
        return converter.convert(utterance);
    }

    public String generate(SystemAct systemAct){
        return generator.generate(systemAct);
    }

    public Domain(String path){
        loadDialStructure(path);
        setConverter(new DefaultConverter());
        setGenerator(new DefaultGenerator());
    }
}
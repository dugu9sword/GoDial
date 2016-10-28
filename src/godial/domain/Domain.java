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
import godial.utils.RegexUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

/**
 * Created by zhouyi on 16-10-23.
 */
public class Domain implements IDomain {

    public static final Domain SYSTEM = new Domain();
    static Log log = LogFactory.getLog(Domain.class);
    private DialStructure dialStructure;
    private AbstractConverter converter;
    private AbstractGenerator generator;
    private AbstractExecutor executor;
    private Kernel kernel;

    private Domain() {
    }

    public static Domain newInstance(String path) {
        try {
            DialStructure dialStructure = ConfigLoader.loadConfig(path);
            Domain domain = new Domain();
            domain.setDialStructure(dialStructure);
            domain.setConverter(new DefaultConverter());
            domain.setGenerator(new DefaultGenerator());
            domain.setExecutor(new DefaultExecutor());
            log.info("Domain " + path + " is successfully loaded");
            return domain;
        } catch (FileNotFoundException e1) {
            log.error("Config file is not found:\t" + path);
        } catch (IOException e2) {
            log.error("The format of config file is illegal:\t" + path);
        }
        return null;
    }

    public boolean isTriggered(String utterance) {
        boolean ret = RegexUtil.containsPattern(utterance, getDialStructure().getTrigger());
        log.info("[Domain]\tThe domain " + getDialStructure().getTask() + (ret ? " is " : " is not ") + "triggered");
        return ret;
    }

    public void setConverter(AbstractConverter converter) {
        this.converter = converter;
        converter.setDomain(this);
    }

    public void setGenerator(AbstractGenerator generator) {
        this.generator = generator;
        generator.setDomain(this);
    }

    public void setExecutor(AbstractExecutor executor) {
        this.executor = executor;
        executor.setDomain(this);
    }

    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    public Context correspondingContext() {
        return kernel.getContextByDomain(this);
    }

    public boolean hasCorrespondingContext() {
        return correspondingContext() != null ? true : false;
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

    public void setDialStructure(DialStructure dialStructure) {
        this.dialStructure = dialStructure;
    }

    public String toString() {
        return getDialStructure().getTask();
    }
}
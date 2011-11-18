package tv.notube.profiler;

import tv.notube.commons.storage.kvs.KVStore;
import tv.notube.commons.storage.kvs.mybatis.MyBatisKVStore;
import tv.notube.commons.storage.model.fields.serialization.SerializationManager;
import tv.notube.profiler.configuration.ProfilerConfiguration;
import tv.notube.profiler.configuration.ProfilingLineDescription;
import tv.notube.profiler.configuration.ProfilingLineItemDescription;
import tv.notube.profiler.container.DefaultProfilingLineContainer;
import tv.notube.profiler.container.ProfilingLineContainer;
import tv.notube.profiler.container.ProfilingLineContainerException;
import tv.notube.profiler.data.DataManager;
import tv.notube.profiler.data.DataManagerException;
import tv.notube.profiler.data.ModularDataManager;
import tv.notube.profiler.line.ProfilingLine;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.storage.KVProfileStoreImpl;
import tv.notube.profiler.storage.ProfileStore;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultProfilerFactory {

    private static DefaultProfilerFactory instance;

    public static DefaultProfilerFactory getInstance(ProfilerConfiguration configuration) {
        if (instance == null) {
            instance = new DefaultProfilerFactory(configuration);
        }
        return instance;
    }

    private ProfilerConfiguration configuration;

    private DefaultProfilerFactory(ProfilerConfiguration configuration) {
        this.configuration = configuration;
    }

    public Profiler build() {
        DataManager dm;
        try {
            dm =
                    new ModularDataManager(
                            configuration.getDataManagerConfiguration()
                    );
        } catch (DataManagerException e) {
            throw new RuntimeException("", e);
        }
        ProfilingLineContainer plc = new DefaultProfilingLineContainer();
        for(ProfilingLineDescription profilingLineDescription : configuration.getPlDescriptions()) {
            String plName = profilingLineDescription.getName();
            String plDescription = profilingLineDescription.getDescription();
            Class<? extends ProfilingLine> clazz;
            try {
                clazz = (Class<? extends ProfilingLine>) Class.forName(profilingLineDescription.getClazz());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("", e);
            }
            Constructor c;
            try {
                c = clazz.getConstructor(String.class, String.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException("", e);
            }
            ProfilingLine profilingLine;
            try {
                profilingLine = (ProfilingLine) c.newInstance(plName, plDescription);
            } catch (InstantiationException e) {
                throw new RuntimeException("", e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("", e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException("", e);
            }
            for (ProfilingLineItemDescription profilingLineItemDescription :
                    profilingLineDescription.getPliDescriptions()) {
                String pliName = profilingLineItemDescription.getName();
                String pliDescr = profilingLineItemDescription.getDescription();
                Class<? extends ProfilingLineItem> pliClazz;
                try {
                    pliClazz = (Class<? extends ProfilingLineItem>) Class.forName(profilingLineItemDescription.getClazz());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException("", e);
                }
                Constructor pliC;
                try {
                     pliC = pliClazz.getConstructor(String.class, String.class);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("", e);
                }
                ProfilingLineItem profilingLineItem;
                try {
                    profilingLineItem = (ProfilingLineItem)
                            pliC.newInstance(pliName, pliDescr);
                } catch (InstantiationException e) {
                    throw new RuntimeException("", e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("", e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException("", e);
                }
                profilingLine.enqueueProfilingLineItem(profilingLineItem);
            }
            try {
                plc.addProfilingLine(profilingLine);
            } catch (ProfilingLineContainerException e) {
                throw new RuntimeException("", e);
            }
        }
        KVStore kvs = new MyBatisKVStore(
                configuration.getProfileStoreConfiguration().asProperties(),
                new SerializationManager()
        );
        ProfileStore ps = new KVProfileStoreImpl(kvs);
        Profiler profiler = new Profiler(dm, plc, ps, configuration);
        return profiler;
    }



}

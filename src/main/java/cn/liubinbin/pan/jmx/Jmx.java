package cn.liubinbin.pan.jmx;

import cn.liubinbin.pan.conf.Config;
import cn.liubinbin.pan.metrics.ServerLoad;
import com.sun.jdmk.comm.HtmlAdaptorServer;
import org.apache.commons.configuration2.ex.ConfigurationException;

import javax.management.*;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.rmi.registry.LocateRegistry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by bin on 2019/5/20.
 */
public class Jmx {

    private ServerLoad serverLoad;
    private final String DOMAIN_NAME = "pan";
    private int rmiPort;
    private Qps qps;
    private GetLantency getLantency;
    private PutLantency putLantency;
    private DeleteLatency deleteLatency;
    private AllLatency allLatency;

    public Jmx(Config cacheConfig, ServerLoad serverLoad) {
        this.rmiPort = cacheConfig.getRmiPort();
        this.serverLoad = serverLoad;
        this.qps = new Qps();
        this.getLantency = new GetLantency();
        this.putLantency = new PutLantency();
        this.deleteLatency = new DeleteLatency();
        this.allLatency = new AllLatency();
    }

    class MetricsToMBeanTask implements Runnable {

        @Override
        public void run() {
            // qps
            qps.setGetQps(serverLoad.getGetQps());
            qps.setPutQps(serverLoad.getPutQps());
            qps.setDeleteQps(serverLoad.getDeleteQps());
            qps.setAllQps(serverLoad.getAllQps());

            // getLatency
            getLantency.setGetLatency50th(serverLoad.getGetLatency50th());
            getLantency.setGetLatency90th(serverLoad.getGetLatency90th());
            getLantency.setGetLatency95th(serverLoad.getGetLatency95th());
            getLantency.setGetLatency99th(serverLoad.getGetLatency99th());
            getLantency.setGetLatency999th(serverLoad.getGetLatency999th());

            // putLatency
            putLantency.setPutLatency50th(serverLoad.getPutLatency50th());
            putLantency.setPutLatency90th(serverLoad.getPutLatency90th());
            putLantency.setPutLatency95th(serverLoad.getPutLatency95th());
            putLantency.setPutLatency99th(serverLoad.getPutLatency99th());
            putLantency.setPutLatency999th(serverLoad.getPutLatency999th());

            // deleteLatency
            deleteLatency.setDeleteLatency50th(serverLoad.getDeleteLatency50th());
            deleteLatency.setDeleteLatency90th(serverLoad.getDeleteLatency90th());
            deleteLatency.setDeleteLatency95th(serverLoad.getDeleteLatency95th());
            deleteLatency.setDeleteLatency99th(serverLoad.getDeleteLatency99th());
            deleteLatency.setDeleteLatency999th(serverLoad.getDeleteLatency999th());

            // allLatency
            allLatency.setAllLatency50th(serverLoad.getAllLatency50th());
            allLatency.setAllLatency90th(serverLoad.getAllLatency90th());
            allLatency.setAllLatency95th(serverLoad.getAllLatency95th());
            allLatency.setAllLatency99th(serverLoad.getAllLatency99th());
            allLatency.setAllLatency999th(serverLoad.getAllLatency999th());
        }
    }

    public void start() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, IOException {
        ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        MetricsToMBeanTask metricsShowTask = new MetricsToMBeanTask();
        scheduledExecutorService.scheduleAtFixedRate(metricsShowTask, 1, 5, TimeUnit.SECONDS);

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName qpsObjName = new ObjectName(DOMAIN_NAME + ":name=qps");
        mbs.registerMBean(qps, qpsObjName);

        ObjectName adapterName = new ObjectName(DOMAIN_NAME + ":name=htmladapter,port=8082");
        HtmlAdaptorServer adapter = new HtmlAdaptorServer();
        adapter.start();
        mbs.registerMBean(adapter, adapterName);
        LocateRegistry.createRegistry(rmiPort);

        JMXServiceURL url = new JMXServiceURL("service:jmx:rmi:///jndi/rmi://localhost:" + rmiPort + "/" + DOMAIN_NAME);
        JMXConnectorServer jmxConnector = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
        jmxConnector.start();
    }

    public static void main(String[] args) throws IOException, ConfigurationException, MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException {
        Jmx jmx = new Jmx(new Config(), new ServerLoad());
        jmx.start();
    }
}

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

/**
 * Created by bin on 2019/5/20.
 */
public class Jmx {

    private ServerLoad serverLoad;
    private final String DOMAIN_NAME = "pan";
    private int rmiPort;

    public Jmx(Config cacheConfig, ServerLoad serverLoad) {
        this.rmiPort = cacheConfig.getRmiPort();
        this.serverLoad = serverLoad;
    }

    public void start() throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, IOException {

        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();

        ObjectName qpsObjName = new ObjectName(DOMAIN_NAME + ":name=qps");
        mbs.registerMBean(serverLoad.getQps(), qpsObjName);

        ObjectName putLatencyObjName = new ObjectName(DOMAIN_NAME + ":name=putLatency");
        mbs.registerMBean(serverLoad.getPutLatency(), putLatencyObjName);

        ObjectName getLatencyObjName = new ObjectName(DOMAIN_NAME + ":name=getLatency");
        mbs.registerMBean(serverLoad.getGetLatency(), getLatencyObjName);

        ObjectName deleteLatencyObjName = new ObjectName(DOMAIN_NAME + ":name=deleteLatency");
        mbs.registerMBean(serverLoad.getDeleteLatency(), deleteLatencyObjName);

        ObjectName allLatencyObjName = new ObjectName(DOMAIN_NAME + ":name=allLatency");
        mbs.registerMBean(serverLoad.getAllLatency(), allLatencyObjName);

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

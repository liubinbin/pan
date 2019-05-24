package cn.liubinbin.pan.client;

import org.apache.http.*;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.*;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParser;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.io.HttpMessageParser;
import org.apache.http.io.HttpMessageParserFactory;
import org.apache.http.io.HttpMessageWriterFactory;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicLineParser;
import org.apache.http.message.LineParser;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author liubinbin
 */
public class Client {

    private String cacheServerHost;
    private int cacheServerPort;
    private final String bucketName = "default";
    CloseableHttpClient httpclient;

    public Client(String cacheServerHost, String cacheServerPort) {
        this.cacheServerHost = cacheServerHost;
        this.cacheServerPort = Integer.parseInt(cacheServerPort);
    }

    public void open() {
        // Use custom message parser / writer to customize the way HTTP
        // messages are parsed from and written out to the data stream.
        HttpMessageParserFactory<HttpResponse> responseParserFactory = new DefaultHttpResponseParserFactory() {

            @Override
            public HttpMessageParser<HttpResponse> create(
                    SessionInputBuffer buffer, MessageConstraints constraints) {
                LineParser lineParser = new BasicLineParser() {

                    @Override
                    public Header parseHeader(final CharArrayBuffer buffer) {
                        try {
                            return super.parseHeader(buffer);
                        } catch (ParseException ex) {
                            return new BasicHeader(buffer.toString(), null);
                        }
                    }

                };
                return new DefaultHttpResponseParser(
                        buffer, lineParser, DefaultHttpResponseFactory.INSTANCE, constraints) {

                    @Override
                    protected boolean reject(final CharArrayBuffer line, int count) {
                        // try to ignore all garbage preceding a status line infinitely
                        return false;
                    }

                };
            }

        };
        HttpMessageWriterFactory<HttpRequest> requestWriterFactory = new DefaultHttpRequestWriterFactory();

        // Use a custom connection factory to customize the process of
        // initialization of outgoing HTTP connections. Beside standard connection
        // configuration parameters HTTP connection factory can define message
        // parser / writer routines to be employed by individual connections.
        HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory = new ManagedHttpClientConnectionFactory(
                requestWriterFactory, responseParserFactory);

        // Create a registry of custom connection socket factories for supported
        // protocol schemes.
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .build();

        // Create a connection manager with custom configuration.
        PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(
                socketFactoryRegistry, connFactory);

        // Create socket configuration
        SocketConfig socketConfig = SocketConfig.custom()
                .setTcpNoDelay(true)
                .build();
        // Configure the connection manager to use socket configuration either
        // by default or for a specific host.
        connManager.setDefaultSocketConfig(socketConfig);
        connManager.setSocketConfig(new HttpHost(cacheServerHost, cacheServerPort), socketConfig);

        // Create message constraints
        MessageConstraints messageConstraints = MessageConstraints.custom()
                .setMaxHeaderCount(200)
                .setMaxLineLength(2000)
                .build();
        // Create connection configuration
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints)
                .build();
        // Configure the connection manager to use connection configuration either
        // by default or for a specific host.
        connManager.setDefaultConnectionConfig(connectionConfig);
        connManager.setConnectionConfig(new HttpHost(cacheServerHost, cacheServerPort), ConnectionConfig.DEFAULT);

        // Configure total max or per route limits for persistent connections
        // that can be kept in the pool or leased by the connection manager.
        connManager.setMaxTotal(100);
//        connManager.setDefaultMaxPerRoute(10);
//        connManager.setMaxPerRoute(new HttpRoute(new HttpHost("somehost", 80)), 20);

        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD)
                .setExpectContinueEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();

        // Create an HttpClient with the given custom dependencies and configuration.
        this.httpclient = HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(defaultRequestConfig)
                .build();

        // get a address to connect
    }

    public void close() throws IOException {
        httpclient.close();
    }

    public ArrayList<String> listObject() {
        return null;
    }

    public void getObject(String key, File destFile) throws IOException {
        HttpGet httpPut = new HttpGet("http://localhost:50503/" + key);

        // Execution context can be customized locally.
        HttpClientContext context = HttpClientContext.create();

        CloseableHttpResponse response = httpclient.execute(httpPut, context);
        try {
//            System.out.println("----------------------------------------");
//            System.out.println(response.getStatusLine());
//            System.out.println(EntityUtils.toString(response.getEntity()));
//            System.out.println(response.getAllHeaders());

//            System.out.println(response.getEntity().getContent());
//            System.out.println("----------------------------------------");
//            byte[] ret = null;
//            ret = ResourceUtil.readStream(inputStream);
            InputStream inputStream = response.getEntity().getContent();
            OutputStream outputStream = new FileOutputStream(destFile);
            byte[] bytes = new byte[1024];
            int byteCount = 0;
            while ( (byteCount = inputStream.read(bytes)) != -1 ){
                outputStream.write(bytes, 0, byteCount);
            }
            outputStream.flush();
            outputStream.close();
        } finally {
            response.close();
        }

    }

    public void putOBject(String key, File file) throws IOException {
        putOBject("default", key, file);
    }

    public void putOBject(String bucketName, String key, File file) throws IOException {
        HttpPut httpPut = new HttpPut("http://localhost:50503/" + key);
        httpPut.setEntity(new FileEntity(file, "application/octet-stream"));
        CloseableHttpResponse response = httpclient.execute(httpPut);
        System.out.println(response.getStatusLine());
        response.close();
    }

    public void putOBject( String key, String content) throws IOException {
        putOBject("default", key, content);
    }

    public void putOBject(String bucketName, String key, String content) throws IOException {
        HttpPut httpPut = new HttpPut("http://localhost:50503/" + key);
        httpPut.setEntity(new StringEntity(content));
        CloseableHttpResponse response = httpclient.execute(httpPut);
        System.out.println(response.getStatusLine());
        response.close();
    }

    public void deleteObject(String bucketName, String key) throws IOException {
        HttpDelete httpDelete = new HttpDelete("http://localhost:50503/" + key);
        CloseableHttpResponse response = httpclient.execute(httpDelete);
        System.out.println(response.getStatusLine());
        response.close();
    }
}

package com.example.springframe.iot.client;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;

/**
 * @author tarzan
 */
public class OpcUaClientTest {
    public static void main(String[] args) throws Exception {
        String endPointUrl="opc.tcp://xxxxxx:xxxx";
        OpcUaClient opcUaClient = ClientGen.createClient(endPointUrl, "xxxx", "xxxx");
        ClientGen.browse(null,opcUaClient);
        assert opcUaClient != null;
        ClientGen.disconnect(opcUaClient);
        Thread.sleep(Integer.MAX_VALUE);
    }
}

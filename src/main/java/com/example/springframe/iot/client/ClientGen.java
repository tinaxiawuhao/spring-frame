package com.example.springframe.iot.client;


import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @author yugo
 *
 */
@Slf4j
@Component
public class ClientGen {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 方法描述: 创建客户端
     *
     * @param endPointUrl
     * @param username
     * @param password
     * @return {@link OpcUaClient}
     * @throws
     */
    public static OpcUaClient createClient(String endPointUrl,String username,String password){
        try {
            Path securityTempDir = Paths.get(System.getProperty("java.io.tmpdir"), "security");
            Files.createDirectories(securityTempDir);
            if (!Files.exists(securityTempDir)) {
                throw new Exception("没有创建安全目录: " + securityTempDir);
            }
            log.info("安全目录: {}", securityTempDir.toAbsolutePath());

            //加载秘钥
            KeyStoreLoader loader = new KeyStoreLoader().load(securityTempDir);
            log.info("开始连接 URL: {}", endPointUrl);
            //安全策略 None、Basic256、Basic128Rsa15、Basic256Sha256
            SecurityPolicy securityPolicy = SecurityPolicy.Basic128Rsa15;
            //获取安全策略
            List<EndpointDescription> endpointDescription = DiscoveryClient.getEndpoints(endPointUrl).get();
            //过滤出一个自己需要的安全策略
            EndpointDescription endpoint = endpointDescription.stream()
                    .filter(e -> e.getSecurityPolicyUri().equals(securityPolicy.getUri()))
                    .findFirst().orElseThrow(() -> new Exception("没有连接上端点"));
            IdentityProvider identityProvider=new AnonymousProvider();
            if(StringUtils.hasLength(username)||StringUtils.hasLength(password)){
                identityProvider=new UsernameProvider(username,password);
            }
            log.info("使用端点: {} [{}/{}]", endpoint.getEndpointUrl(), securityPolicy, endpoint.getSecurityMode());
            // 设置配置信息
            OpcUaClientConfig config = OpcUaClientConfig.builder()
                    // opc ua 自定义的名称
                    .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
                    // 地址
                    .setApplicationUri("urn:eclipse:milo:examples:client")
                    // 安全策略等配置
                    .setEndpoint(endpoint)
                    .setIdentityProvider(identityProvider)
                    .setCertificate(loader.getClientCertificate())
                    .setKeyPair(loader.getClientKeyPair())
                    //等待时间
                    .setRequestTimeout(UInteger.valueOf(5000))
                    .build();
            // 准备连接
            OpcUaClient opcClient =OpcUaClient.create(config);
            //开启连接
            opcClient.connect().get();
            log.info("连接成功。。。success");
            return opcClient;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("======== opc connection fail ========");
        }
        return null;
    }


    /**
     * 方法描述: 查询所有节点
     *
     * @param client
     * @return {@link Set<String>}
     * @throws
     * @author liwenbin
     * @date 2023年07月03日 13:05:55
     */
    public static Set<String> getAllKeys(OpcUaClient client) throws Exception {
        return browse(null,client);
    }


    /**
     * 方法描述: 查询某个节点下的所有节点
     *
     * @param identifier
     * @param client
     * @return
     * @throws
     */
    public static Set<String> browse(String identifier, OpcUaClient client) throws Exception {
        Set<String> keys=new HashSet<>(500);
        browse(identifier,keys,client);
        return keys;
    }

    /**
     * 方法描述: 查询某个节点下的所有节点
     *
     * @param identifier
     * @param client
     * @return
     * @throws
     */
    private static Set<String> browse(String identifier, Set<String> keys, OpcUaClient client) throws Exception {
        NodeId nodeId = Identifiers.ObjectsFolder;
        if(!StringUtils.isEmpty(identifier)){
            nodeId = new NodeId(2, identifier);
        }
        BrowseDescription browse = new BrowseDescription(
                nodeId,
                BrowseDirection.Forward,
                Identifiers.References,
                true,
                UInteger.valueOf(NodeClass.Object.getValue() | NodeClass.Variable.getValue()),
                UInteger.valueOf(BrowseResultMask.All.getValue())
        );
        BrowseResult browseResult = client.browse(browse).get();
        ReferenceDescription[] references = browseResult.getReferences();
        for (ReferenceDescription reference : references) {
            System.out.println(reference.getNodeId().getIdentifier().toString()+" "+reference.getNodeClass().getValue());
            keys.add(identifier);
            if(reference.getNodeClass().getValue()==NodeClass.Object.getValue()){
                browse(reference.getNodeId().getIdentifier().toString(),keys,client);
            }
        }
        return keys;
    }


    /**
     * 方法描述: 读取单个节点值
     *
     * @param identifier
     * @param client
     * @return {@link Object}
     * @throws
     */
    public static Object readValue(String identifier, OpcUaClient client){
        NodeId nodeId = new NodeId(2, identifier);
        DataValue value = null;
        try {
            client.readValue(0.0, TimestampsToReturn.Both,nodeId);
            value = client.readValue(0.0, TimestampsToReturn.Both, nodeId).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        if(Objects.nonNull(value)&&Objects.nonNull(value.getValue())&&Objects.nonNull(value.getValue().getValue())) {
            return value.getValue().getValue();
        }
        return null;
    }

    /**
     * 方法描述: 读取多个点位的值
     *
     * @param keys 点位集合
     * @param client 客户端
     * @return {@link List<DataValue>}
     * @throws
     */
    public static List<DataValue> readValues(Set<String> keys, OpcUaClient client){
        List<NodeId> nodeIdList=new ArrayList<>(500);
        keys.forEach(e->{
            NodeId nodeId = new NodeId(2, e);
            nodeIdList.add(nodeId);
        });
        try {
            List<DataValue> dataValues=client.readValues(0.0, TimestampsToReturn.Both,nodeIdList).get();
            return dataValues;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 写入值
     *
     * @param identifier
     * @param value
     * @throws Exception
     */
    public static void writeValue(String identifier, Object value,OpcUaClient client) throws Exception {
        //创建变量节点
        NodeId nodeId = new NodeId(2, identifier);
        //创建Variant对象和DataValue对象
        Variant v = new Variant(value);
        DataValue dataValue = new DataValue(v, null, null);
        StatusCode statusCode = client.writeValue(nodeId, dataValue).get();
        if(statusCode.isGood()){
            log.info("数据写入成功");
        }else {
            log.error("数据写入失败");
        }
    }


    /**
     * 方法描述:  写入多个节点的值
     *
     * @param keys  节点集合
     * @param values  值集合
     * @param client  客户端
     * @return {@link Object}
     * @throws
     */
    public static Object writeValues(Set<String> keys,List<Object> values,OpcUaClient client){
        List<NodeId> nodeIs=new ArrayList<>(keys.size());
        keys.forEach(e->{
            NodeId nodeId = new NodeId(2, e);
            nodeIs.add(nodeId);
        });
        List<DataValue> dataValues=new ArrayList<>(values.size());
        values.forEach(e->{
            Variant value=new Variant(Double.parseDouble(e.toString()));
            DataValue dataValue=new DataValue(value);
            dataValues.add(dataValue);
        });
        try {
            client.writeValues(nodeIs,dataValues).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 方法描述: 断开连接
     *
     * @param client
     * @return
     */
    public static void disconnect(OpcUaClient client){
        try {
            client.disconnect().get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}

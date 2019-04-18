package cn.mldn.http.server.serious;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

public class MarshallingCodeFactory {
    //编码器
    public static MarshallingEncoder builderMarshallingEncoder(){
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial"); //获取原始的JDK序列化
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        DefaultMarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }

    //解码器
    public static MarshallingDecoder builderMarshallingDecoder(){
        MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial"); //获取原始的JDK序列化
        MarshallingConfiguration configuration = new MarshallingConfiguration();
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        int maxSize = 1024 << 2; //设置单个对象的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider, maxSize);
        return decoder;

    }
}

package io.ocnet.ocmrpc.core;

import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class TunnelFactory {

    public static Tunnel wrap(SocketChannel channel, Selector selector, int role) {
        return new RawTunnel(channel, selector, role);
    }

    public static Tunnel createTunnelByConfig(InetSocketAddress destAddress, Selector selector, int role) throws Exception {
        if (destAddress.isUnresolved()) {
            if (ProxyConfig.Instance.enableRtcTunnel) {
                DatagramTunnel datagramTunnel = new DatagramTunnel(role);
                if (datagramTunnel.isPeerConnected()) {
                    return datagramTunnel;
                }
            }

            Config config = ProxyConfig.Instance.getDefaultTunnelConfig(destAddress);
            if (config instanceof HttpConnectConfig) {
                return new HttpConnectTunnel((HttpConnectConfig) config, selector, role);
            } else if (config instanceof ShadowsocksConfig) {
                return new ShadowsocksTunnel((ShadowsocksConfig) config, selector, role);
            }
            throw new Exception("The config is unknow.");
        } else {
            return new RawTunnel(destAddress, selector, role);
        }
    }

}

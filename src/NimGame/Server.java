package NimGame;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    ExecutorService executorService;
    ServerSocketChannel serverSocketChannel;
    List<Client> connections = new Vector<Client>();

    void startServer() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(true);
            serverSocketChannel.bind(new InetSocketAddress(5001));
        } catch (Exception e) {
            if(serverSocketChannel.isOpen()) {
                stopServer();
            }
            return;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while(true) {
                    if(connections.size() < 2) {
                        try {
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            Client client = new Client(socketChannel);
                            connections.add(client);
                        } catch (Exception e) {
                            if(serverSocketChannel.isOpen()) {
                                stopServer();
                            }
                            break;
                        }
                    }
                }
            }
        };
        executorService.submit(runnable);
    }

    void stopServer() {
        try {
            Iterator<Client> iterator = connections.iterator();
            while(iterator.hasNext()) {
                Client client = iterator.next();
                client.socketChannel.close();
                iterator.remove();
            }
            if(serverSocketChannel!=null && serverSocketChannel.isOpen()) {
                serverSocketChannel.close();
            }
            if(executorService!=null && !executorService.isShutdown()) {
                executorService.shutdown();
            }
        } catch (Exception e) {}
    }

    class Client {
        SocketChannel socketChannel;
        public Client(SocketChannel socketChannel) {
            this.socketChannel = socketChannel;
            receive();
        }
        void receive() {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    while(true) {
                        try {
                            ByteBuffer byteBuffer = ByteBuffer.allocate(100);

                            int readByteCount = socketChannel.read(byteBuffer);

                            if(readByteCount == -1) {
                                throw new IOException();
                            }

                            byteBuffer.flip();
                            Charset charset = Charset.forName("UTF-8");
                            String data = charset.decode(byteBuffer).toString();

                            for(Client client : connections) {
                                client.send(data);
                            }
                        } catch (Exception e) {
                            try {
                                connections.remove(Client.this);
                                socketChannel.close();
                            } catch (Exception e2) {}
                            break;
                        }
                    }
                }
            };
            executorService.submit(runnable);
        }
        void send(String data) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Charset charset = Charset.forName("UTF-8");
                        ByteBuffer byteBuffer = charset.encode(data);
                        socketChannel.write(byteBuffer);
                    } catch (Exception e) {
                        try {
                            connections.remove(Client.this);
                            socketChannel.close();
                        } catch (Exception e2) {}
                    }
                }
            };
            executorService.submit(runnable);
        }
    }
}

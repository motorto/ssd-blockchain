package group3.ssd.blockchain.p2p.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: Messages.proto")
public final class PeerGrpc {

  private PeerGrpc() {}

  public static final String SERVICE_NAME = "blockchain.Peer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Ping,
      group3.ssd.blockchain.p2p.grpc.Pong> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ping",
      requestType = group3.ssd.blockchain.p2p.grpc.Ping.class,
      responseType = group3.ssd.blockchain.p2p.grpc.Pong.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Ping,
      group3.ssd.blockchain.p2p.grpc.Pong> getPingMethod() {
    io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Ping, group3.ssd.blockchain.p2p.grpc.Pong> getPingMethod;
    if ((getPingMethod = PeerGrpc.getPingMethod) == null) {
      synchronized (PeerGrpc.class) {
        if ((getPingMethod = PeerGrpc.getPingMethod) == null) {
          PeerGrpc.getPingMethod = getPingMethod = 
              io.grpc.MethodDescriptor.<group3.ssd.blockchain.p2p.grpc.Ping, group3.ssd.blockchain.p2p.grpc.Pong>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "blockchain.Peer", "ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Ping.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Pong.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerMethodDescriptorSupplier("ping"))
                  .build();
          }
        }
     }
     return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Block,
      group3.ssd.blockchain.p2p.grpc.Status> getBroadcastBlockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "broadcast_block",
      requestType = group3.ssd.blockchain.p2p.grpc.Block.class,
      responseType = group3.ssd.blockchain.p2p.grpc.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Block,
      group3.ssd.blockchain.p2p.grpc.Status> getBroadcastBlockMethod() {
    io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Block, group3.ssd.blockchain.p2p.grpc.Status> getBroadcastBlockMethod;
    if ((getBroadcastBlockMethod = PeerGrpc.getBroadcastBlockMethod) == null) {
      synchronized (PeerGrpc.class) {
        if ((getBroadcastBlockMethod = PeerGrpc.getBroadcastBlockMethod) == null) {
          PeerGrpc.getBroadcastBlockMethod = getBroadcastBlockMethod = 
              io.grpc.MethodDescriptor.<group3.ssd.blockchain.p2p.grpc.Block, group3.ssd.blockchain.p2p.grpc.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "blockchain.Peer", "broadcast_block"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Block.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerMethodDescriptorSupplier("broadcast_block"))
                  .build();
          }
        }
     }
     return getBroadcastBlockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Transaction,
      group3.ssd.blockchain.p2p.grpc.Status> getBroadcastTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "broadcast_transaction",
      requestType = group3.ssd.blockchain.p2p.grpc.Transaction.class,
      responseType = group3.ssd.blockchain.p2p.grpc.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Transaction,
      group3.ssd.blockchain.p2p.grpc.Status> getBroadcastTransactionMethod() {
    io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.Transaction, group3.ssd.blockchain.p2p.grpc.Status> getBroadcastTransactionMethod;
    if ((getBroadcastTransactionMethod = PeerGrpc.getBroadcastTransactionMethod) == null) {
      synchronized (PeerGrpc.class) {
        if ((getBroadcastTransactionMethod = PeerGrpc.getBroadcastTransactionMethod) == null) {
          PeerGrpc.getBroadcastTransactionMethod = getBroadcastTransactionMethod = 
              io.grpc.MethodDescriptor.<group3.ssd.blockchain.p2p.grpc.Transaction, group3.ssd.blockchain.p2p.grpc.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "blockchain.Peer", "broadcast_transaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Transaction.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerMethodDescriptorSupplier("broadcast_transaction"))
                  .build();
          }
        }
     }
     return getBroadcastTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.BlockChain,
      group3.ssd.blockchain.p2p.grpc.Status> getBroadcastBlockchainMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "broadcast_blockchain",
      requestType = group3.ssd.blockchain.p2p.grpc.BlockChain.class,
      responseType = group3.ssd.blockchain.p2p.grpc.Status.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.BlockChain,
      group3.ssd.blockchain.p2p.grpc.Status> getBroadcastBlockchainMethod() {
    io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.BlockChain, group3.ssd.blockchain.p2p.grpc.Status> getBroadcastBlockchainMethod;
    if ((getBroadcastBlockchainMethod = PeerGrpc.getBroadcastBlockchainMethod) == null) {
      synchronized (PeerGrpc.class) {
        if ((getBroadcastBlockchainMethod = PeerGrpc.getBroadcastBlockchainMethod) == null) {
          PeerGrpc.getBroadcastBlockchainMethod = getBroadcastBlockchainMethod = 
              io.grpc.MethodDescriptor.<group3.ssd.blockchain.p2p.grpc.BlockChain, group3.ssd.blockchain.p2p.grpc.Status>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "blockchain.Peer", "broadcast_blockchain"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.BlockChain.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.Status.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerMethodDescriptorSupplier("broadcast_blockchain"))
                  .build();
          }
        }
     }
     return getBroadcastBlockchainMethod;
  }

  private static volatile io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.FindNode,
      group3.ssd.blockchain.p2p.grpc.KBucket_GRPC> getFindNodesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "findNodes",
      requestType = group3.ssd.blockchain.p2p.grpc.FindNode.class,
      responseType = group3.ssd.blockchain.p2p.grpc.KBucket_GRPC.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.FindNode,
      group3.ssd.blockchain.p2p.grpc.KBucket_GRPC> getFindNodesMethod() {
    io.grpc.MethodDescriptor<group3.ssd.blockchain.p2p.grpc.FindNode, group3.ssd.blockchain.p2p.grpc.KBucket_GRPC> getFindNodesMethod;
    if ((getFindNodesMethod = PeerGrpc.getFindNodesMethod) == null) {
      synchronized (PeerGrpc.class) {
        if ((getFindNodesMethod = PeerGrpc.getFindNodesMethod) == null) {
          PeerGrpc.getFindNodesMethod = getFindNodesMethod = 
              io.grpc.MethodDescriptor.<group3.ssd.blockchain.p2p.grpc.FindNode, group3.ssd.blockchain.p2p.grpc.KBucket_GRPC>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "blockchain.Peer", "findNodes"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.FindNode.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  group3.ssd.blockchain.p2p.grpc.KBucket_GRPC.getDefaultInstance()))
                  .setSchemaDescriptor(new PeerMethodDescriptorSupplier("findNodes"))
                  .build();
          }
        }
     }
     return getFindNodesMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static PeerStub newStub(io.grpc.Channel channel) {
    return new PeerStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static PeerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new PeerBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static PeerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new PeerFutureStub(channel);
  }

  /**
   */
  public static abstract class PeerImplBase implements io.grpc.BindableService {

    /**
     * <pre>
     *Main server actions
     * </pre>
     */
    public void ping(group3.ssd.blockchain.p2p.grpc.Ping request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Pong> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    public void broadcastBlock(group3.ssd.blockchain.p2p.grpc.Block request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getBroadcastBlockMethod(), responseObserver);
    }

    /**
     */
    public void broadcastTransaction(group3.ssd.blockchain.p2p.grpc.Transaction request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getBroadcastTransactionMethod(), responseObserver);
    }

    /**
     */
    public void broadcastBlockchain(group3.ssd.blockchain.p2p.grpc.BlockChain request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status> responseObserver) {
      asyncUnimplementedUnaryCall(getBroadcastBlockchainMethod(), responseObserver);
    }

    /**
     */
    public void findNodes(group3.ssd.blockchain.p2p.grpc.FindNode request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.KBucket_GRPC> responseObserver) {
      asyncUnimplementedUnaryCall(getFindNodesMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                group3.ssd.blockchain.p2p.grpc.Ping,
                group3.ssd.blockchain.p2p.grpc.Pong>(
                  this, METHODID_PING)))
          .addMethod(
            getBroadcastBlockMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                group3.ssd.blockchain.p2p.grpc.Block,
                group3.ssd.blockchain.p2p.grpc.Status>(
                  this, METHODID_BROADCAST_BLOCK)))
          .addMethod(
            getBroadcastTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                group3.ssd.blockchain.p2p.grpc.Transaction,
                group3.ssd.blockchain.p2p.grpc.Status>(
                  this, METHODID_BROADCAST_TRANSACTION)))
          .addMethod(
            getBroadcastBlockchainMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                group3.ssd.blockchain.p2p.grpc.BlockChain,
                group3.ssd.blockchain.p2p.grpc.Status>(
                  this, METHODID_BROADCAST_BLOCKCHAIN)))
          .addMethod(
            getFindNodesMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                group3.ssd.blockchain.p2p.grpc.FindNode,
                group3.ssd.blockchain.p2p.grpc.KBucket_GRPC>(
                  this, METHODID_FIND_NODES)))
          .build();
    }
  }

  /**
   */
  public static final class PeerStub extends io.grpc.stub.AbstractStub<PeerStub> {
    private PeerStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PeerStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeerStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PeerStub(channel, callOptions);
    }

    /**
     * <pre>
     *Main server actions
     * </pre>
     */
    public void ping(group3.ssd.blockchain.p2p.grpc.Ping request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Pong> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void broadcastBlock(group3.ssd.blockchain.p2p.grpc.Block request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBroadcastBlockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void broadcastTransaction(group3.ssd.blockchain.p2p.grpc.Transaction request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBroadcastTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void broadcastBlockchain(group3.ssd.blockchain.p2p.grpc.BlockChain request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getBroadcastBlockchainMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void findNodes(group3.ssd.blockchain.p2p.grpc.FindNode request,
        io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.KBucket_GRPC> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getFindNodesMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class PeerBlockingStub extends io.grpc.stub.AbstractStub<PeerBlockingStub> {
    private PeerBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PeerBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeerBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PeerBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     *Main server actions
     * </pre>
     */
    public group3.ssd.blockchain.p2p.grpc.Pong ping(group3.ssd.blockchain.p2p.grpc.Ping request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public group3.ssd.blockchain.p2p.grpc.Status broadcastBlock(group3.ssd.blockchain.p2p.grpc.Block request) {
      return blockingUnaryCall(
          getChannel(), getBroadcastBlockMethod(), getCallOptions(), request);
    }

    /**
     */
    public group3.ssd.blockchain.p2p.grpc.Status broadcastTransaction(group3.ssd.blockchain.p2p.grpc.Transaction request) {
      return blockingUnaryCall(
          getChannel(), getBroadcastTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public group3.ssd.blockchain.p2p.grpc.Status broadcastBlockchain(group3.ssd.blockchain.p2p.grpc.BlockChain request) {
      return blockingUnaryCall(
          getChannel(), getBroadcastBlockchainMethod(), getCallOptions(), request);
    }

    /**
     */
    public group3.ssd.blockchain.p2p.grpc.KBucket_GRPC findNodes(group3.ssd.blockchain.p2p.grpc.FindNode request) {
      return blockingUnaryCall(
          getChannel(), getFindNodesMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class PeerFutureStub extends io.grpc.stub.AbstractStub<PeerFutureStub> {
    private PeerFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private PeerFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected PeerFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new PeerFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     *Main server actions
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<group3.ssd.blockchain.p2p.grpc.Pong> ping(
        group3.ssd.blockchain.p2p.grpc.Ping request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<group3.ssd.blockchain.p2p.grpc.Status> broadcastBlock(
        group3.ssd.blockchain.p2p.grpc.Block request) {
      return futureUnaryCall(
          getChannel().newCall(getBroadcastBlockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<group3.ssd.blockchain.p2p.grpc.Status> broadcastTransaction(
        group3.ssd.blockchain.p2p.grpc.Transaction request) {
      return futureUnaryCall(
          getChannel().newCall(getBroadcastTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<group3.ssd.blockchain.p2p.grpc.Status> broadcastBlockchain(
        group3.ssd.blockchain.p2p.grpc.BlockChain request) {
      return futureUnaryCall(
          getChannel().newCall(getBroadcastBlockchainMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<group3.ssd.blockchain.p2p.grpc.KBucket_GRPC> findNodes(
        group3.ssd.blockchain.p2p.grpc.FindNode request) {
      return futureUnaryCall(
          getChannel().newCall(getFindNodesMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PING = 0;
  private static final int METHODID_BROADCAST_BLOCK = 1;
  private static final int METHODID_BROADCAST_TRANSACTION = 2;
  private static final int METHODID_BROADCAST_BLOCKCHAIN = 3;
  private static final int METHODID_FIND_NODES = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final PeerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(PeerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_PING:
          serviceImpl.ping((group3.ssd.blockchain.p2p.grpc.Ping) request,
              (io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Pong>) responseObserver);
          break;
        case METHODID_BROADCAST_BLOCK:
          serviceImpl.broadcastBlock((group3.ssd.blockchain.p2p.grpc.Block) request,
              (io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status>) responseObserver);
          break;
        case METHODID_BROADCAST_TRANSACTION:
          serviceImpl.broadcastTransaction((group3.ssd.blockchain.p2p.grpc.Transaction) request,
              (io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status>) responseObserver);
          break;
        case METHODID_BROADCAST_BLOCKCHAIN:
          serviceImpl.broadcastBlockchain((group3.ssd.blockchain.p2p.grpc.BlockChain) request,
              (io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.Status>) responseObserver);
          break;
        case METHODID_FIND_NODES:
          serviceImpl.findNodes((group3.ssd.blockchain.p2p.grpc.FindNode) request,
              (io.grpc.stub.StreamObserver<group3.ssd.blockchain.p2p.grpc.KBucket_GRPC>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class PeerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    PeerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return group3.ssd.blockchain.p2p.grpc.BlockchainGRPCProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Peer");
    }
  }

  private static final class PeerFileDescriptorSupplier
      extends PeerBaseDescriptorSupplier {
    PeerFileDescriptorSupplier() {}
  }

  private static final class PeerMethodDescriptorSupplier
      extends PeerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    PeerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (PeerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new PeerFileDescriptorSupplier())
              .addMethod(getPingMethod())
              .addMethod(getBroadcastBlockMethod())
              .addMethod(getBroadcastTransactionMethod())
              .addMethod(getBroadcastBlockchainMethod())
              .addMethod(getFindNodesMethod())
              .build();
        }
      }
    }
    return result;
  }
}

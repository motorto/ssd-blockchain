// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Messages.proto

package group3.ssd.blockchain.p2p.grpc;

public interface PingOrBuilder extends
    // @@protoc_insertion_point(interface_extends:blockchain.Ping)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string id = 1;</code>
   */
  java.lang.String getId();
  /**
   * <code>string id = 1;</code>
   */
  com.google.protobuf.ByteString
      getIdBytes();

  /**
   * <code>string ip = 2;</code>
   */
  java.lang.String getIp();
  /**
   * <code>string ip = 2;</code>
   */
  com.google.protobuf.ByteString
      getIpBytes();

  /**
   * <code>int32 port = 3;</code>
   */
  int getPort();

  /**
   * <code>int32 proof = 4;</code>
   */
  int getProof();

  /**
   * <code>string pubKey = 5;</code>
   */
  java.lang.String getPubKey();
  /**
   * <code>string pubKey = 5;</code>
   */
  com.google.protobuf.ByteString
      getPubKeyBytes();
}
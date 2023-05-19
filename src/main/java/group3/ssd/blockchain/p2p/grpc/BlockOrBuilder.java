// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Messages.proto

package group3.ssd.blockchain.p2p.grpc;

public interface BlockOrBuilder extends
    // @@protoc_insertion_point(interface_extends:blockchain.Block)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>string hashId = 1;</code>
   */
  java.lang.String getHashId();
  /**
   * <code>string hashId = 1;</code>
   */
  com.google.protobuf.ByteString
      getHashIdBytes();

  /**
   * <code>string hash = 2;</code>
   */
  java.lang.String getHash();
  /**
   * <code>string hash = 2;</code>
   */
  com.google.protobuf.ByteString
      getHashBytes();

  /**
   * <code>string previousHash = 3;</code>
   */
  java.lang.String getPreviousHash();
  /**
   * <code>string previousHash = 3;</code>
   */
  com.google.protobuf.ByteString
      getPreviousHashBytes();

  /**
   * <code>.blockchain.TransactionsList transactionsList = 4;</code>
   */
  boolean hasTransactionsList();
  /**
   * <code>.blockchain.TransactionsList transactionsList = 4;</code>
   */
  group3.ssd.blockchain.p2p.grpc.TransactionsList getTransactionsList();
  /**
   * <code>.blockchain.TransactionsList transactionsList = 4;</code>
   */
  group3.ssd.blockchain.p2p.grpc.TransactionsListOrBuilder getTransactionsListOrBuilder();

  /**
   * <code>int32 nonce = 5;</code>
   */
  int getNonce();

  /**
   * <code>int64 timestamp = 6;</code>
   */
  long getTimestamp();

  /**
   * <code>string publicKey = 7;</code>
   */
  java.lang.String getPublicKey();
  /**
   * <code>string publicKey = 7;</code>
   */
  com.google.protobuf.ByteString
      getPublicKeyBytes();

  /**
   * <code>string nodeId = 8;</code>
   */
  java.lang.String getNodeId();
  /**
   * <code>string nodeId = 8;</code>
   */
  com.google.protobuf.ByteString
      getNodeIdBytes();
}

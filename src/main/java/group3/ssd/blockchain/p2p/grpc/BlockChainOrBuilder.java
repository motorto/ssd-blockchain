// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: Messages.proto

package group3.ssd.blockchain.p2p.grpc;

public interface BlockChainOrBuilder extends
    // @@protoc_insertion_point(interface_extends:blockchain.BlockChain)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>repeated .blockchain.Block chain = 1;</code>
   */
  java.util.List<group3.ssd.blockchain.p2p.grpc.Block> 
      getChainList();
  /**
   * <code>repeated .blockchain.Block chain = 1;</code>
   */
  group3.ssd.blockchain.p2p.grpc.Block getChain(int index);
  /**
   * <code>repeated .blockchain.Block chain = 1;</code>
   */
  int getChainCount();
  /**
   * <code>repeated .blockchain.Block chain = 1;</code>
   */
  java.util.List<? extends group3.ssd.blockchain.p2p.grpc.BlockOrBuilder> 
      getChainOrBuilderList();
  /**
   * <code>repeated .blockchain.Block chain = 1;</code>
   */
  group3.ssd.blockchain.p2p.grpc.BlockOrBuilder getChainOrBuilder(
      int index);

  /**
   * <code>.blockchain.TransactionsList pendingTransactions = 2;</code>
   */
  boolean hasPendingTransactions();
  /**
   * <code>.blockchain.TransactionsList pendingTransactions = 2;</code>
   */
  group3.ssd.blockchain.p2p.grpc.TransactionsList getPendingTransactions();
  /**
   * <code>.blockchain.TransactionsList pendingTransactions = 2;</code>
   */
  group3.ssd.blockchain.p2p.grpc.TransactionsListOrBuilder getPendingTransactionsOrBuilder();
}
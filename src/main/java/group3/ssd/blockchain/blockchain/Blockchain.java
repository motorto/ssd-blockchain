package group3.ssd.blockchain.blockchain;

import group3.ssd.blockchain.p2p.KadClient;
import group3.ssd.blockchain.transactions.Transaction;
import group3.ssd.blockchain.transactions.Wallet;
import group3.ssd.blockchain.util.Config;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class Blockchain {
    public static ArrayList<Block> chain = new ArrayList<>(); //blocos em cadeia
    public static ArrayList<Transaction> pendingTransactions = new ArrayList<>(); //transações pendentes

    public Blockchain(Wallet genesisWallet) throws InvalidKeySpecException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException {
        createGenesisBlock(genesisWallet);
    }

    // criar vazio
    public Blockchain() throws InvalidKeySpecException, NoSuchAlgorithmException {
        chain = new ArrayList<>();
        pendingTransactions = new ArrayList<>();
    }

    // criar cópia de outra blockchain
    public Blockchain(Blockchain oldBlockchain) {
        setChain(oldBlockchain.getChain());
        setPendingTransactions(oldBlockchain.getPendingTransactions());

    }

    // define transações pendentes e faz uma copia (evita referencias partilhadas)
    public void setPendingTransactions(ArrayList<Transaction> oldTransactionArrayList) {
        for (Transaction curTransaction : oldTransactionArrayList) {
            pendingTransactions.add(new Transaction(curTransaction.hash, curTransaction.senderPK, curTransaction.receiverPK, curTransaction.signature, curTransaction.timestamp, curTransaction.amount, curTransaction.misc));
        }
    }

    public ArrayList<Transaction> getPendingTransactions() {
        return pendingTransactions;
    }

    public int getPendingTransactionsLength() {
        return pendingTransactions.size();
    }

    // define a cadeia de blocos com base numa lista antiga e faz copia
    public void setChain(ArrayList<Block> oldChain) {
        for (Block curBlock : oldChain) {
            ArrayList<Transaction> newTransactionList = new ArrayList<>();
            for (Transaction oldTransaction : curBlock.getTransactionList()) {
                newTransactionList.add(new Transaction(oldTransaction.hash, oldTransaction.senderPK, oldTransaction.receiverPK, oldTransaction.signature, oldTransaction.timestamp, oldTransaction.amount, oldTransaction.misc));
            }
            chain.add(new Block(curBlock.hashId, curBlock.hash, curBlock.previousHash, newTransactionList, curBlock.nonce, curBlock.timestamp, curBlock.publicKey));
        }
    }

    public ArrayList<Block> getChain() {
        return chain;
    }

    // genesis: peça inicial da construção da blockchain
    // inicializa com uma wallet e um valor 
    public void createGenesisBlock(Wallet genesisWallet) throws NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, InvalidKeyException, UnsupportedEncodingException {
        Wallet tempWallet = new Wallet();
        this.addTransaction(tempWallet, genesisWallet, 100);
        this.minePendingTransactions(genesisWallet);
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public boolean addBlock(Block newBlock) {
        if (!newBlock.verify()) {
            System.out.println("ERRO"); //hash nao corresponde
            return false;
        }
        int transactionSize = newBlock.getTransactionListSize();
        for (int i = 0; i < transactionSize; i++) {
            System.out.println(pendingTransactions.size());
            if (pendingTransactions.size() == 0) {
                return false;
            } else if (!pendingTransactions.get(i).equals(newBlock.transactionsList.get(i))) {
                System.out.println("ERRO"); //a transação nao corresponde
                pendingTransactions.get(i).printTransaction();
                newBlock.transactionsList.get(i).printTransaction();
                return false;
            }
        }

        pendingTransactions.subList(0, transactionSize).clear();
        newBlock.previousHash = this.getLatestBlock().hash;

        chain.add(newBlock);
        System.out.println("ADICIONADO");
        return true;
    }

    // verificar se blockchain é válida
    public boolean isValid() {
        Block cur, prev;
        String hashTarget = new String(new char[Config.MINING_DIFFICULTY]).replace('\0', '0');

        for (int i = 1; i < chain.size(); i++) {
            cur = chain.get(i);
            prev = chain.get(i - 1);

            //verifica hash
            if (!cur.hash.equals(cur.calculateHash())) {
                return false;
            }

            //verifica parente
            if (!cur.previousHash.equals(prev.hash)) {
                return false;
            }

            //verifica se a hash é mined
            if (!cur.hash.substring(0, Config.MINING_DIFFICULTY).equals(hashTarget)) {
                return false;
            }

            //verifica transações
            for (int j = 0; j < cur.getTransactionListSize(); j++) {
                Transaction curTransaction = cur.getTransactionList().get(j);
                if (!curTransaction.hash.equals(curTransaction.calculateHash())) {
                    return false;
                }
            }
        }
        return true;

    }

    //imprime e diz se é valida
    public void printBlockChain() {
        System.out.println("blockchain:");
        for (Block currentBlock : chain) {
            currentBlock.printBlock();
        }

        String isCValid = this.isValid() ? "True" : "False";
        if (isCValid.equals("False")) {
            System.out.println("Cadeia não é válida");
        } else {
            System.out.println("Cadeia é válida");
        }
    }

    //Adiciona uma transação: quem envia, quem recebe e  valor
    public Transaction addTransaction(Wallet sender, Wallet receiver, int amount) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, InvalidKeySpecException, UnsupportedEncodingException {
        Transaction newTransaction = new Transaction(sender.getPublicKey(), receiver.getPublicKey(), amount);
        newTransaction.signTransaction(sender);

        pendingTransactions.add(newTransaction);
        System.out.println("Valor " + amount + " enviado por " + sender.getPublicKey() + " para " + receiver.getPublicKey());

        return newTransaction;
    }

    //Minera as transações pendentes num bloco novo
    public Block minePendingTransactions(Wallet miner) {
        int pendingTransactionsLength = getPendingTransactionsLength();
        if (pendingTransactionsLength == 0) {
            System.out.println("Não há transações pendentes");
            return null;
        }

        ArrayList<Transaction> newBlockTransactions = new ArrayList<>();
        if (pendingTransactionsLength >= Config.MAX_TRANSACTIONS_PER_BLOCK) {
            for (int i = 0; i < Config.MAX_TRANSACTIONS_PER_BLOCK; i++) {
                newBlockTransactions.add(pendingTransactions.get(i));
            }
        } else {
            System.out.println("Não há transições suficientes (" + Config.MAX_TRANSACTIONS_PER_BLOCK + "), há apenas " + pendingTransactionsLength);
            newBlockTransactions.addAll(pendingTransactions);
        }

        //criar novo bloco com as transações pendentes e adicona á cadeia
        Block newBlock;
        if (chain.size() == 0) {
            newBlock = new Block(0 + "", newBlockTransactions, "", KadClient.wallet);
        } else {
            newBlock = new Block(chain.size() + "", newBlockTransactions, this.getLatestBlock().hash, KadClient.wallet);
        }
        newBlock.mineBlock();
        chain.add(newBlock);
        pendingTransactions.subList(0, newBlockTransactions.size()).clear();
        System.out.println("FEITO");
        return newBlock;

    }
}

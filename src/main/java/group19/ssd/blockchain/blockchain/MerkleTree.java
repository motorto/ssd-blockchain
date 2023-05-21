package group19.ssd.blockchain.blockchain;

import group19.ssd.blockchain.transactions.Transaction;
import group19.ssd.blockchain.util.Config;
import group19.ssd.blockchain.util.Misc;

import java.util.ArrayList;

//Merkle para verificar integridade dos dados
public class MerkleTree {
    ArrayList<Transaction> transactionsList; //lista de transações
    String root; //raiz da tree

    public MerkleTree(ArrayList<Transaction> transactionsList) {
        this.transactionsList = transactionsList;
        root = ""; //definir a raiz como vazio
    }

    // gerar a raiz da tree
    public void genRoot() {

        if (transactionsList.size() != Config.MAX_TRANSACTIONS_PER_BLOCK) return; //raiz vai ser vazia
        //se nao calcula a hash de cada transação 
        ArrayList<String> oldHashList = new ArrayList<>();

        for (Transaction transaction : transactionsList)
            oldHashList.add(transaction.calculateHash());

        ArrayList<String> newHashList = getNewHashList(oldHashList);
        while (newHashList.size() != 1) { //até chegar á raiz
            newHashList = getNewHashList(newHashList);
        }

        this.root = newHashList.get(0);
    }

    //recebe uma hash temp e gera uma nova lista com os valores de ambos os lados
    private ArrayList<String> getNewHashList(ArrayList<String> tempHashList) {

        ArrayList<String> newHashList = new ArrayList<String>();
        int index = 0;
        while (index < tempHashList.size()) {
            // esquerda
            String left = tempHashList.get(index);
            index++;

            // direita
            String right = "";
            if (index != tempHashList.size()) {
                right = tempHashList.get(index);
            }

            String all = Misc.applyEncryption(left + right);
            newHashList.add(all);
            index++;

        }
        return newHashList;
    }

    public String getRoot() {
        return this.root;
    }


}

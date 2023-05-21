package group3.ssd.blockchain;

import group3.ssd.blockchain.p2p.KadClient;
import group3.ssd.blockchain.p2p.KadServer;
import group3.ssd.blockchain.p2p.Kademlia;
import group3.ssd.blockchain.transactions.Transaction;
import group3.ssd.blockchain.util.Config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Objects;
import java.util.Scanner;

public class App {

    public static void printMenu() {
        int i = 1;
        System.out.println("Opções:\n" +
                (i++) + "   Informação Pessoal\n" +
                (i++) + "   Iniciar Mining\n" +
                (i++) + "   Saldo\n" +
                (i++) + "   Enviar Coins\n" +
                (i++) + "   Ver Blockchain\n" +
                (i++) + "   Ver Bucket\n" +
                (i) + "   Sair\n" +
                "Insira um número de 1 a 6:\n");
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {

        int port = 8086;

        if (Objects.equals(Config.knownNode, "")) {
            port = 8080;
        }

        KadClient kadClient = new KadClient();
        kadClient.setup(port, "localhost");
        KadClient.startPinging();

        KadServer server = new KadServer("localhost", port);
        server.start();

        Kademlia.findNode(KadClient.id);

        Scanner scan = new Scanner(System.in);

        while (true) {
            printMenu();
            int option = scan.nextInt();
            scan.nextLine();
            switch (option) {
                case 1: //informação pessoal
                    System.out.println("ID: " + KadClient.id);
                    System.out.println(KadClient.ip + ":" + KadClient.port);
                    System.out.println("Chave Publica: " + KadClient.publicKey);
                    break;
                case 2: //iniciar mining
                    KadClient.startMining();
                    break;
                case 3: //saldo
                    KadClient.wallet.printWalletBalance();
                    break;
                case 4: // enviar coin
                    System.out.println("Inserir chave publica do recetor");
                    String receiverPK = scan.nextLine();
                    if (receiverPK.equals(KadClient.publicKey)) { //se tentar enviar para si proprio
                        System.out.println("ERRO" + "\n\n\n");
                    } else {
                        System.out.println("Quanto quer enviar?");
                        int numberOfCoins = scan.nextInt();

                        if (KadClient.wallet.getBalance() < numberOfCoins) { //Se nao tiver coins suficientes
                            System.out.println("ERRO." + "\n\n\n");
                        } else {
                            System.out.println("RPK: " + receiverPK);
                            Transaction transaction = new Transaction(KadClient.publicKey, receiverPK, numberOfCoins);
                            KadClient.shareTransaction(transaction, KadClient.id);
                        }
                    }
                    break;
                case 5: //blockchain
                    KadClient.blockchain.printBlockChain();
                    break;
                case 6: //ver bucket
                    KadClient.kbucket.print();
                    break;
                case 7: // Sair
                    System.exit(0);
                    break;
                default:
                    System.out.println("ERRO" + "\n\n\n");
                    break;
            }
        }
    }
}



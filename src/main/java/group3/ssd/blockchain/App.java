package group3.ssd.blockchain;

import group3.ssd.blockchain.blockchain.Transaction;
import group3.ssd.blockchain.p2p.KadServer;
import group3.ssd.blockchain.p2p.Kademlia;
import group3.ssd.blockchain.p2p.User;
import group3.ssd.blockchain.util.Config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
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
                "Insira um número de 1 a 6:\n");
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, InterruptedException {

        int port = 8086;

        if (Config.knownNode == "") {
            port = 8080;
        }

        User user = new User();
        user.setup(port, "localhost");
        User.startPinging();

        KadServer server = new KadServer("localhost", port);
        server.start();

        Kademlia.findNode(User.id);

        Scanner scan = new Scanner(System.in);

        while (true) {
            printMenu();
            int option = scan.nextInt();
            scan.nextLine();
            switch (option) {
                case 1: //informação pessoal
                    System.out.println("ID: " + User.id);
                    System.out.println(User.ip + ":" + User.port);
                    System.out.println("Chave Publica: " + User.publicKey);
                    break;
                case 2: //iniciar mining
                    User.startMining();
                    break;
                case 3: //saldo
                    User.wallet.printWalletBalance();
                    break;
                case 4: // enviar coin
                    System.out.println("Inserir chave publica do recetor");
                    String receiverPK = scan.nextLine();
                    if (receiverPK.equals(User.publicKey)) { //se tentar enviar para si proprio
                        System.out.println("ERRO" + "\n\n\n");
                    } else {
                        System.out.println("Quando quer enviar?");
                        int numberOfCoins = scan.nextInt();

                        if (User.wallet.getBalance() < numberOfCoins) { //Se nao tiver coins suficientes
                            System.out.println("ERRO." + "\n\n\n");
                        } else {
                            System.out.println("RPK: " + receiverPK);
                            Transaction transaction = new Transaction(User.publicKey, receiverPK, numberOfCoins);
                            User.shareTransaction(transaction, User.id);
                        }
                    }
                    break;
                case 5: //blockchain
                    User.blockchain.printBlockChain();
                    break;
                case 6: //ver bucket
                    User.kbucket.print();
                    break;
                default:
                    System.out.println("ERRO" + "\n\n\n");
                    break;
            }
        }
    }
}



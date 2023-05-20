package group3.ssd.blockchain;

import group3.ssd.blockchain.blockchain.Transaction;
import group3.ssd.blockchain.p2p.Kademlia;
import group3.ssd.blockchain.p2p.PeerServer;
import group3.ssd.blockchain.p2p.User;
import group3.ssd.blockchain.util.Config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class App {

    public static void printMenu() {
        int i = 1;
        System.out.println("#     MENU\n" +
                "# " + (i++) + "   Get Personal Information\n" +
                "# " + (i++) + "   Start Mining\n" +
                "# " + (i++) + "   Go to Auction\n" +
                "# " + (i++) + "   Get Balance\n" +
                "# " + (i++) + "   Print Blockchain\n" +
                "# " + (i++) + "   Send Coins to User\n" +
                "# " + (i++) + "   Print Bucket\n" +
                "# Please insert an option.\n");
    }

    public static void main(String[] args) throws InvalidKeySpecException, NoSuchAlgorithmException, IOException, InterruptedException {

        int port = 8086;

        if (Config.knownNode == "") {
            port = 8080;
        }


        User user = new User();
        user.setup(port, "localhost");
        User.startPinging();

        PeerServer server = new PeerServer("localhost", port);
        server.start();

        Kademlia.findNode(User.id);

        Scanner scan = new Scanner(System.in);

        while (true) {
            printMenu();
            int option = scan.nextInt();
            scan.nextLine();
            switch (option) {
                case 1:
                    System.out.println("ID: " + User.id);
                    System.out.println(User.ip + ":" + User.port);
                    System.out.println("PublicKey: " + User.publicKey);
                    break;
                case 2:
                    User.startMining();
                    break;
                case 3:
                    //TODO implement and run auction
                    break;
                case 4:
                    User.wallet.printWalletBalance();
                    break;
                case 5:
                    User.blockchain.printBlockChain();
                    break;
                case 6:
                    System.out.println("Introduce Receiver Public Key");
                    String receiverPK = scan.nextLine();
                    if (receiverPK.equals(User.publicKey)) {
                        System.out.println("You can not send funds to your own wallet." + "\n\n\n");
                    } else {
                        System.out.println("Please insert the amount of coins you wish to send.");
                        int numberOfCoins = scan.nextInt();

                        if (User.wallet.getBalance() < numberOfCoins) {
                            System.out.println("You do not have enough funds." + "\n\n\n");
                        } else {
                            System.out.println("RPK: " + receiverPK);
                            Transaction transaction = new Transaction(User.publicKey, receiverPK, numberOfCoins);
                            User.shareTransaction(transaction, User.id);
                        }
                    }
                    break;
                case 7:
                    User.kbucket.print();
                    break;
                default:
                    System.out.println("Chosen option is not valid" + "\n\n\n");
                    break;
            }
        }
    }
}


import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private static final int PORT = 1234;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();


    public static void serveriCalistir(){
        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            System.out.println("Server is running and waiting for connections..");

            // Gelen bağlantıları kabul edelim
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket);

                // Her isteyici için clientHandler oluşturalım
                Server.ClientHandler clientHandler = new Server.ClientHandler(clientSocket);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Tum kullancılara mesajı göndermek için metod oluşturalım
    public static void broadcast(Gonderi gonderi, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                try {
                    client.sendMessage(gonderi);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    //Sadece tek kullancıya gonderi gondermek için metod da lazım


    //Bağlantıları ayarlamak için iç sınıf oluşturalım
    protected static class ClientHandler implements Runnable {
        private Socket clientSocket;
        private ObjectOutputStream out;
        private ObjectInputStream in;
        private User user;

        // Constructor
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;

            try {
                // İletişim için input/output oluşturalım
                out = new ObjectOutputStream(clientSocket.getOutputStream());
                in = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Kullancı ile iletişim için run() metodu
        @Override
        public void run() {
                try {
                    int loginOlduMu = 0;
                    String inputLine;
                    while ((inputLine = (String)in.readObject()) != null)
                    try{
                        //İlk önce kullancının girip girmemiş olduğundan emin olalım
                        if (loginOlduMu !=0 ){
                            Gonderi istek = SifrelemeServer.cevir(inputLine);
                            System.out.println(istek.getMesaj().getMesaj());

                            // Mesaj varsa bunu tum kullancılara gonderelim
                            if (istek.getRequestType() == 3 & istek.getMesaj() != null){
                                Mesaj mesaj = VeriTabanIslemler.mesajEkle(istek.getMesaj());
                                istek.setMesaj(mesaj);
                                istek.setResponseCode(31);
                                broadcast(istek, this);

                            }

                            //Geçmiş yuklemek isteniyorsa geçmişi diziye saklayıp tek tek gönderelim
                            if (istek.getRequestType() == 4){
                                List<Mesaj> mesajlar = VeriTabanIslemler.getAllMessages();
                                for (Mesaj mesaj: mesajlar){
                                    Gonderi gonderi = new Gonderi(4,mesaj);
                                    gonderi.setResponseCode(31);
                                    sendMessage(gonderi);
                                }

                            }

                        }
                        else{
                            User user = SifrelemeServer.userCevir(inputLine);

                            //İsteyiciden gelen Kullancı varMı bilgisine göre kullancı ya üye olur ya giriş yapar
                            if(user.varMi){
                                Gonderi gonderi = new Gonderi(1,null);
                                gonderi.setResponseCode(VeriTabanIslemler.girisYap(user));
                                System.out.println("oldu");
                                sendMessage(gonderi);

                            }
                            else {
                                Gonderi gonderi = new Gonderi(2,null);
                                gonderi.setResponseCode(VeriTabanIslemler.kullanciOlustur(user));
                                sendMessage(gonderi);

                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        // Remove the client handler from the list
                        clients.remove(this);

                        // Close the input and output streams and the client socket
                        in.close();
                        out.close();
                        clientSocket.close();
                    }

                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }


        public void sendMessage(Gonderi gonderi) throws IOException {
            synchronized (this) {
                try {
                    String responseString = SifrelemeServer.sifrele(gonderi);
                    out.writeObject(responseString);
                    out.reset();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}

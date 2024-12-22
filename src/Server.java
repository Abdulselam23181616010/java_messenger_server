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
    public static void broadcast(Response gonderi, ClientHandler sender) {
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
                //İlk önce kullancının girip girmemiş olduğundan emin olalım
                int loginOlduMu = 0;
                try{
                    String inputLine = (String) in.readObject();
                    User user = SifrelemeServer.userCevir(inputLine);
                    if(user.varMi)
                        loginOlduMu = VeriTabanIslemler.girisYap(user);
                    else
                        VeriTabanIslemler.kullanciOlustur(user);
                    if (loginOlduMu==1){
                        String inputLine1 = (String) in.readObject();
                        Gonderi istek = SifrelemeServer.cevir(inputLine1);
                        RequestSolver istekCozucu = (RequestSolver)istek;
                        Response donus = (Response)istek;

                        // İstemciden gönderi almayı devam et
                        while (istekCozucu != null) {
                            // Mesaj varsa bunu tum kullancılara gonderelim
                            if (istekCozucu.requestType == 3 & istekCozucu.mesaj != null)
                                broadcast(donus, this);

                            //Geçmiş yuklemek isteniyorsa geçmişi diziye saklayıp tek tek gönderelim
                            if (istekCozucu.requestType == 4){
                                List<Mesaj> mesajlar = VeriTabanIslemler.getAllMessages();
                                for (Mesaj mesaj: mesajlar){
                                    Response response = new Response(4,mesaj);
                                    response.setResponseCode(31);
                                    sendMessage(response);
                                }

                            }
                        }

                    }
                    else{
                        Response response = new Response(2,null);
                        response.setResponseCode(20);
                        sendMessage(response);
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

            } catch (IOException  e) {
                e.printStackTrace();
            }
        }
        public void sendMessage(Response response) throws IOException {
            String responseString = SifrelemeServer.sifrele(response);
            out.writeObject(responseString);

        }

    }
}

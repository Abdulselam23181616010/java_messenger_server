import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//VeriTabanConnector adındaki sınıf sunucumuzun veritabanla iletişime geçmesini sağlar
public class VeriTabanIslemler {
    private static Connection veritabanaBaglan() throws SQLException {
        // Alttaki değerlerle sql kutuphanesindn aldığımız Connection sınıfından nesneyle veritabana bağlanalım
        String url = "jdbc:mysql://localhost:3306/messenger?autoReconnect=true&useSSL=false";
        String dbUsername = "root";
        String password = "test123";

        Connection connection = DriverManager.getConnection(url, dbUsername, password);
        //Bağlantıyı dondurelim
        return connection;
    }

    //Veri tabanda kullancıyı oluşturmamıza yardımcı olacak metod yazalım
    public static int kullanciOlustur(User user) {
        try (Connection connection = veritabanaBaglan()) {
            if (connection.isClosed()){
                System.out.println("Baglantı yok");
                return 0;
            }
            //SQL injection'dan korunmak için PreparedStatement kullanalım
            PreparedStatement preparedStatement;
            String sql = "INSERT INTO users (username, isim, soyisim, sifre) VALUES (?, ?, ?, ? )";

            // Log the connection state before query execution
            System.out.println("Preparing to execute query with connection: " + (connection.isClosed() ? "Closed" : "Open"));
            preparedStatement = connection.prepareStatement(sql);

            //Sifreyi guvenlik nedenle hash şeklinde saklayalım
            String sifreHash = HashOlustur.md5HashOlustur(user.sifre);

            preparedStatement.setString(1, user.username);
            preparedStatement.setString(2, user.isim);
            preparedStatement.setString(3, user.soyisim);
            preparedStatement.setString(4, sifreHash);

            //Son olarak kullancı oluşturma işlemimizi sqlde çalıştıralım
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Kullanci olusturuldu");

            //Her şey sorunsuz olursa ve kullancımız oluşturursa 1 donelim
            return 1;

        } catch (Exception e) {
            //Hata oluşursa hata mesajını yazdırıp 0 donelim
            e.printStackTrace();
            return 0;

        }
    }

    public static int girisYap(User user) {
        try(Connection connection = veritabanaBaglan()){
            if (connection.isClosed()){
                System.out.println("Baglantı yok");
                return 0;
            }

            PreparedStatement preparedStatement;
            String sql = "SELECT sifre FROM users WHERE username = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.username);

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            String veridekiHash = rs.getString("sifre");

            String sifreHash = HashOlustur.md5HashOlustur(user.sifre);
            if (veridekiHash.equals(sifreHash)){
                System.out.println("Giris Basarili");
                return 1;
            }
            else
                return 0;

        } catch (Exception e){
            e.printStackTrace();
            return 0;

        }

    }

    public static List<Mesaj> getAllMessages() {
        List<Mesaj> messages = new ArrayList<>();

        String query = "SELECT * FROM messages";

        try (Connection connection = veritabanaBaglan();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String gonderici = resultSet.getString("gonderici");
                String mesaj = resultSet.getString("mesaj");
                Timestamp time = resultSet.getTimestamp("time");

                // Create a Message object and add it to the list
                messages.add(new Mesaj(id, gonderici, mesaj, time));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

}





import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//VeriTabanConnector adındaki sınıf sunucumuzun veritabanla iletişime geçmesini sağlar
public class VeriTabanConnector {
    public static Connection veritabanaBaglan() throws SQLException {
        // Alttaki değerlerle sql kutuphanesindn aldığımız Connection sınıfından nesneyle veritabana bağlanalım
        String url = "jdbc:mysql://localhost:3306/messenger?autoReconnect=true&useSSL=false";
        String dbUsername = "roo";
        String password = "test123";

        Connection connection = DriverManager.getConnection(url, dbUsername, password);
        //Bağlantıyı dondurelim
        return connection;
    }

    //Veri tabanda kullancıyı oluşturmamıza yardımcı olacak metod yazalım
    public static int kullanciOlustur(String username, String isim, String soyisim, String sifre) {



        PreparedStatement preparedStatement;
        try (Connection connection = veritabanaBaglan()) {
            if (connection.isClosed()){
                System.out.println("Baglantı yok");
                return 0;
            }
            //SQL injection'dan korunmak için PreparedStatement kullanalım
            String sql = "INSERT INTO users (username, isim, soyisim, sifreHash) VALUES (?, ?, ?, ? )";

            // Log the connection state before query execution
            System.out.println("Preparing to execute query with connection: " + (connection.isClosed() ? "Closed" : "Open"));
            preparedStatement = connection.prepareStatement(sql);
            //Sifreyi guvenlik nedenle hash şeklinde saklayalım
            String hash = Sifreleme.md5HashOlustur(sifre);

            preparedStatement.setString(1, username);
            preparedStatement.setString(2, isim);
            preparedStatement.setString(3, soyisim);
            preparedStatement.setString(4, hash);

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
}





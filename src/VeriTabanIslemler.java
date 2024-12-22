import java.sql.*;
import java.time.LocalDateTime;
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
            String sql = "INSERT INTO users (username, isim, soyisim, sifreHash) VALUES (?, ?, ?, ? )";

            // Log the connection state before query execution
            System.out.println("Preparing to execute query with connection: " + (connection.isClosed() ? "Closed" : "Open"));
            preparedStatement = connection.prepareStatement(sql);

            //Sifreyi guvenlik nedenle hash şeklinde saklayalım
            String sifreHash = HashOlustur.md5HashOlustur(user.getSifre());

            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getIsim());
            preparedStatement.setString(3, user.getSoyisim());
            preparedStatement.setString(4, sifreHash);

            //Son olarak kullancı oluşturma işlemimizi sqlde çalıştıralım
            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Kullanci olusturuldu");

            //Her şey sorunsuz olursa ve kullancımız oluşturursa 1 donelim
            return 21;

        } catch (Exception e) {
            //Hata oluşursa hata mesajını yazdırıp hata kodunu döndürelim
            e.printStackTrace();
            return 20;

        }
    }

    public static int girisYap(User user) {
        try(Connection connection = veritabanaBaglan()){
            if(user.getUsername()=="" | user.getSifre()=="")
                return 10;
            PreparedStatement preparedStatement;
            String sql = "SELECT sifreHash FROM users WHERE username = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getUsername());

            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            String veridekiHash = rs.getString("sifreHash");

            String sifreHash = HashOlustur.md5HashOlustur(user.getSifre());
            if (veridekiHash.equals(sifreHash)){
                System.out.println("Giris Basarili");
                return 11;
            }
            else
                return 10;

        } catch (Exception e){
            e.printStackTrace();
            return 10;

        }

    }

    public static Mesaj mesajEkle(Mesaj mesaj){
        try(Connection connection = veritabanaBaglan()){
            PreparedStatement preparedStatementInsert;
            String sql =  "INSERT INTO messages (gonderici, mesaj,time) VALUES (?, ?, ?) ";

            preparedStatementInsert = connection.prepareStatement(sql);
            preparedStatementInsert.setString(1, mesaj.getGonderici());
            preparedStatementInsert.setString(2, mesaj.getMesaj());
            preparedStatementInsert.setObject(3, LocalDateTime.now());
            int rowsAffected = preparedStatementInsert.executeUpdate();
            System.out.println("mesaj oluşturuldu");

            PreparedStatement preparedStatementSelect;
            String sql1 = "SELECT id, gonderici, mesaj, time FROM messages WHERE gonderici = ? AND mesaj = ? ORDER BY time DESC LIMIT 1";

            preparedStatementSelect = connection.prepareStatement(sql1);
            preparedStatementSelect.setString(1, mesaj.getGonderici());
            preparedStatementSelect.setString(2, mesaj.getMesaj());
            ResultSet rs = preparedStatementSelect.executeQuery();

            rs.next();

            int id = rs.getInt("id");
            String gonderici = rs.getString("gonderici");
            String string = rs.getString("mesaj");
            LocalDateTime time = rs.getObject("time",LocalDateTime.class);
            return new Mesaj(id,gonderici,string,time);


        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    //Gecmişi yuklemek için Veritabandan tum mesajları isteyiciye göndereceğiz
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
                LocalDateTime time = resultSet.getObject("time", LocalDateTime.class);

                // Create a Message object and add it to the list
                messages.add(new Mesaj(id, gonderici, mesaj, time));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return messages;
    }

}





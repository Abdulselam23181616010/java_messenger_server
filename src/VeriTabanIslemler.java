import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//VeriTabanConnector adındaki sınıf sunucumuzun veritabanla iletişime geçmesini sağlar
public class VeriTabanIslemler {

    //Diğer metodlarda kullanacağımız private bir metodtur. Bununla veri tabana bağlamamızı sağlarız
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

            preparedStatement = connection.prepareStatement(sql);

            //Sifreyi guvenlik nedenle hash şeklinde saklayalım
            String sifreHash = HashOlustur.md5HashOlustur(user.getSifre());

            //Degerleri querymize girerim
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

    //Giriş yapmak için bir metodtur. Gelen kullancı bilfgileri veri setindeki bilgilerle karşılaşır
    public static int girisYap(User user) {
        try(Connection connection = veritabanaBaglan()){
            //Giriş boşsa griş yapmasını istemediğimiz için if-statement ekleuyelim
            if(user.getUsername()=="" | user.getSifre()=="")
                return 10;

            //SQL injection'dan korunmak için PreparedStatement kullanalım
            PreparedStatement preparedStatement;
            String sql = "SELECT sifreHash FROM users WHERE username = ?";

            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1,user.getUsername());

            //İşlemden çıkan verileri okuyalım
            ResultSet rs = preparedStatement.executeQuery();
            rs.next();
            String veridekiHash = rs.getString("sifreHash");

            //Şifre hashlarını kontrol edelim beziyorsa giriş yapmasına izin verelim
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

    //Uygulamanın ana kısımlardan biri. Gelen measajları veri tabanımızda saklayalım sonra dönüş olarak vereceğiz ve broadcast edeceğiz
    public static Mesaj mesajEkle(Mesaj mesaj){
        try(Connection connection = veritabanaBaglan()){

            //SQL injection'dan korunmak için PreparedStatement kullanalım
            PreparedStatement preparedStatementInsert;
            String sql =  "INSERT INTO messages (gonderici, mesaj,time) VALUES (?, ?, ?) ";

            //sutunlerden biri olan time - zaman değerini oluşturmak için LocalDateTime formatlayıp kullanalım.
            LocalDateTime currentTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentTime.format(formatter);

            preparedStatementInsert = connection.prepareStatement(sql);
            preparedStatementInsert.setString(1, mesaj.getGonderici());
            preparedStatementInsert.setString(2, mesaj.getMesaj());
            preparedStatementInsert.setObject(3, formattedDateTime);
            int rowsAffected = preparedStatementInsert.executeUpdate();

            //Şİmdi mesajı dönmeek için veritabandan mesajı çekeceğiz. SQL injection'dan korunmak için PreparedStatement kullanalım
            PreparedStatement preparedStatementSelect;
            String sql1 = "SELECT id, gonderici, mesaj, time FROM messages WHERE gonderici = ? AND mesaj = ? ORDER BY time DESC LIMIT 1";


            preparedStatementSelect = connection.prepareStatement(sql1);
            preparedStatementSelect.setString(1, mesaj.getGonderici());
            preparedStatementSelect.setString(2, mesaj.getMesaj());
            ResultSet rs = preparedStatementSelect.executeQuery();

            rs.next();
            //burdaki değerlerle mesaj nesnesini oluşsturup döndüreceğiz
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
        //Geçmişteki mesajları dizide saklayalım
        List<Mesaj> messages = new ArrayList<>();

        String sql = "SELECT * FROM messages";
        try (Connection connection = veritabanaBaglan();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            //Döngü ile mesaj nesneleri oluşturup diziye ekleyelim
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String gonderici = resultSet.getString("gonderici");
                String mesaj = resultSet.getString("mesaj");
                LocalDateTime time = resultSet.getObject("time", LocalDateTime.class);
                messages.add(new Mesaj(id, gonderici, mesaj, time));

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //son olarak da diziyi dödürelim
        return messages;
    }

}





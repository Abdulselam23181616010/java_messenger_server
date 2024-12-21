public class RequestSolver extends Gonderi{
    //Constructorumuzu oluşturalım
    public RequestSolver(int requestType,Mesaj mesaj) {
        super(requestType, mesaj);

    }

    public int islemYap(User user){
        //request'e gore işlem yapmasını sağlamak için switch case kullanalım
        try{
            switch (requestType){
                case 1:
                    return 1;
                default:
                    System.out.println("islem tanımlanmadi");
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }
}

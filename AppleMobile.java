public class AppleMobile implements Mobile{
    private String desc;

    public AppleMobile(String desc){
        this.desc = desc;

    }

    @Override
    public void getDesc(){
        System.out.println(desc);
    }
}

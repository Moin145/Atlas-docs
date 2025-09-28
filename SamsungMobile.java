public class SamsungMobile implements Mobile{
    private String desc;
    public SamsungMobile(String desc){
        this.desc = desc;

    }

    @Override
    public void getDesc() {
        System.out.println(desc);

    }
}

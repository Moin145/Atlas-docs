public class OnePlusMobile implements Mobile{
    private String desc;

    public OnePlusMobile(String desc){
        this.desc = desc;

    }

    @Override
    public void getDesc() {
        System.out.println(desc);
    }
}

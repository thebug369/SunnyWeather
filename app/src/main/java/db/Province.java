package db;


import org.litepal.crud.LitePalSupport;

//id	是每个实体类中都应该有的字段，provinceName	记录省的名字，provinceCode 记录省的代号


public class Province extends LitePalSupport {

    private int id;
    private String provinceName;
    private String provinceCode;

    public int getId(){
        return id;
    }

    public String getProvinceName(){
        return provinceName;
    }

    public String getProvinceCode(){
        return provinceCode;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setProvinceName(String provinceName){
        this.provinceName=provinceName;
    }

    public void setProvinceCode(String provinceCode){
        this.provinceCode=provinceCode;
    }
}

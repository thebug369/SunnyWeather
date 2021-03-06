package db;

import org.litepal.crud.LitePalSupport;


//，cityName	记录市的名字，cityCode	记录市的代号，provinceId	记录当前市所属省 的id值


public class City extends LitePalSupport {

    private int id;
    private int cityCode;
    private String cityName;
    private int provinceId;

    public int getId(){
        return id;
    }

    public int getCityCode(){
        return cityCode;
    }

    public String getCityName(){
        return cityName;
    }

    public int getProvinceId(){
        return provinceId;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setCityCode(int cityCode){
        this.cityCode=cityCode;
    }

    public void setCityName(String cityName){
        this.cityName=cityName;
    }

    public void setProvinceId(int provinceId){
        this.provinceId=provinceId;
    }
}

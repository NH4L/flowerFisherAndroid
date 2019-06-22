package cn.aysst.www.flowerfish.beans;


public class UsersInfo {
    public int _id;
    public String name;
    public String password;
    public int age;
    public String info;
    public String email;

    public UsersInfo(){

    }

    public UsersInfo(String name, String email){
        this.name = name;
        this.email = email;
    }
    public UsersInfo(String name,String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public UsersInfo(String name,int age,String email, String info){
        this.name = name;
        this.age = age;
        this.info = info;
        this.email = email;
    }
    public UsersInfo(String name,String password,int age,String email, String info){
        this.name = name;
        this.password = password;
        this.age = age;
        this.info = info;
        this.email = email;
    }
}

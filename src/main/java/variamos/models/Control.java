package variamos.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Control {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(length = 3000) 
    private String data;
    
    private String name;

    public Control() {  }

    public Control(String data, String name) {
        this.setdata(data);
        this.setName(name);
      
    }
    
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
    		this.id = id;
    }

    public String getdata() {
        return this.data;
    }

    public void setdata(String data) {
        this.data = data;
    }
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
   
}
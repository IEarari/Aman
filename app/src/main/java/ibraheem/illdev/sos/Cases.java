package ibraheem.illdev.sos;

public class Cases {
    String Name , cell_number , Status , ID , Address , Dep;

    public String getDep() {
        return Dep;
    }

    public void setDep(String dep) {
        Dep = dep;
    }

    public Cases(String name, String cell_number, String status, String ID, String address , String Dep) {
        Name = name;
        this.cell_number = cell_number;
        Status = status;
        this.ID = ID;
        Address = address;
        this.Dep = Dep;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCell_number() {
        return cell_number;
    }

    public void setCell_number(String cell_number) {
        this.cell_number = cell_number;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}

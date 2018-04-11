/**
 * Created by Han Yu on 2018-04-09.
 */
public class Records {
    String name, value, type;

    public Records(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType(){
        return type;
    }
}
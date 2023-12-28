package somepackage;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Table {
    @Id
    private Long id;

    private Integer intField;

    private String stringField;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIntField() {
        return intField;
    }

    public void setIntField(Integer intField) {
        this.intField = intField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }
}

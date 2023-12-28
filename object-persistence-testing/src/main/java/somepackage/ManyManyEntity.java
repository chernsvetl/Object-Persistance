package somepackage;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class ManyManyEntity {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

}

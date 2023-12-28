package com.github.object.persistence.persistence.sql.impl;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TestEntity {
    @Id
    private Long id;

    private Date date;

    public TestEntity() {
    }
}
